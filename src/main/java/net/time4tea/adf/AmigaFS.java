package net.time4tea.adf;

import net.time4tea.adf.blocks.ADFBlock;
import net.time4tea.adf.blocks.ADFBlockFactory;
import net.time4tea.adf.blocks.Block;
import net.time4tea.adf.blocks.BlockFactory;
import net.time4tea.adf.blocks.DataBlock;
import net.time4tea.adf.blocks.DirectoryLikeBlock;
import net.time4tea.adf.blocks.FileHeaderBlock;
import net.time4tea.adf.blocks.NoDataAtBlockException;
import net.time4tea.adf.blocks.RawFile;
import net.time4tea.adf.blocks.RootBlock;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class AmigaFS {
    private final RawFile.Blocks blocks;
    private final FilesystemCharacteristics characteristics;
    private final BlockFactory factory;

    public static class FilesystemCharacteristics {
        public final int rootblock;
        public final boolean ffs;
        public final int blockOffset;

        public FilesystemCharacteristics(int rootblock, boolean ffs, int blockOffset) {
            this.rootblock = rootblock;
            this.ffs = ffs;
            this.blockOffset = blockOffset;
        }

        public static FilesystemCharacteristics floppy() {
            return new FilesystemCharacteristics(880, false, 0);
        }

        public static FilesystemCharacteristics harddisk(int rootblock, int blockOffset) {
            return new FilesystemCharacteristics(rootblock, true, blockOffset);
        }
    }

    public AmigaFS(RawFile rawFile) {
        this(rawFile, FilesystemCharacteristics.floppy());
    }

    public AmigaFS(RawFile rawFile, FilesystemCharacteristics characteristics) {
        this.blocks = rawFile.blocksize(512);
        this.characteristics = characteristics;
        this.factory = new ADFBlockFactory(characteristics.ffs,  characteristics.blockOffset);
    }

    public Block specialBlock(int blockNumber) throws IOException {
        return factory.blockFor(loadBlock(blockNumber));
    }

    public DataBlock dataBlock(int blockNumber) throws IOException {
        return factory.dataBlockFor(loadBlock(blockNumber));
    }

    public RootBlock getRootblock(int rootblockNumber) throws IOException {
        return (RootBlock) specialBlock(rootblockNumber);
    }

    public Block block(int block) throws IOException {
        return factory.blockFor(loadBlock(block));
    }

    private byte[] loadBlock(int blockNumber) throws IOException {
        try {
            return blocks.bytesFor(blockNumber);
        } catch (IOException e) {
            throw new NoDataAtBlockException("Unable to load block", blockNumber, e);
        }
    }

    public AmigaFile getFile(String path) {
        return new AmigaFile(this, path);
    }

    public boolean exists(String filePath) throws IOException {
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }

        if (filePath.length() == 0) {
            //root
            return true;
        }

        try {
            findBlockForPath(filePath);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }


    private ADFBlock findBlockForPath(String path) throws IOException {
        DirectoryLikeBlock directoryBlock = getRootblock(characteristics.rootblock);

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        if (path.length() == 0) {
            return directoryBlock;
        }

        String[] components = path.split("/");

        if (components.length == 0) {
            return directoryBlock;
        }

        ADFBlock foundBlock = null;
        for (String pathComponent : components) {
            foundBlock = findDirectoryBlockFor(pathComponent, directoryBlock);
            if (foundBlock instanceof DirectoryLikeBlock) {
                directoryBlock = (DirectoryLikeBlock) foundBlock;
            }
        }

        return foundBlock;
    }

    private ADFBlock findDirectoryBlockFor(String pathComponent, DirectoryLikeBlock directoryBlock) throws IOException {

        int hashCode = hashCodeFor(pathComponent);
        int nextBlock = directoryBlock.getHashtable()[hashCode];

        if (nextBlock != 0) {
            ADFBlock block = (ADFBlock) specialBlock(nextBlock);
            while (!block.getName().equalsIgnoreCase(pathComponent) && block.nextBlockWithSameHash() != 0) {
                block = (ADFBlock) specialBlock(block.nextBlockWithSameHash());
            }
            if (block.getName().equalsIgnoreCase(pathComponent)) {
                return block;
            }
        }

        throw new FileNotFoundException(pathComponent);
    }

    public int hashCodeFor(String name) {
        int hash;                /* sizeof(int)>=2 */

        hash = name.length();
        for (int i = 0; i < name.length(); i++) {
            hash = hash * 13;
            hash = hash + Character.toUpperCase(name.charAt(i));    /* not case sensitive */
            hash = hash & 0x7ff;
        }

        hash = hash % ((blockSize() / 4) - 56);        /* 0 < hash < 71 in the case of 512 byte blocks */

        return hash;
    }

    public InputStream inputStreamForFile(String filePath) throws IOException {
        FileHeaderBlock fileHeaderblock = (FileHeaderBlock) findBlockForPath(filePath);

        List<Integer> blocks = fileHeaderblock.getFileBlocksFromFileHeaderBlock(this);

        int fileSize = fileHeaderblock.getFileSize();

        byte[] bytes = new byte[fileSize];

        int count = 0;

        for (int block : blocks) {
            int remaining = fileSize - count;
            DataBlock dataBlock = dataBlock(block);
            int dataBytesInBlock = dataBlock.dataSize();
            System.arraycopy(dataBlock.dataBytes(), 0, bytes, count, Math.min(remaining, dataBytesInBlock));
            count += dataBytesInBlock;
        }

        return new ByteArrayInputStream(bytes);

    }

    private int blockSize() {
        return blocks.blockSize();
    }

    public boolean isDirectory(String filePath) throws IOException {
        if (exists(filePath)) {
            return findBlockForPath(filePath) instanceof DirectoryLikeBlock;
        }
        return false;
    }

    public String[] listDirectory(String filePath) throws IOException {

        List<String> filenames = new ArrayList<String>();

        if (isDirectory(filePath)) {
            DirectoryLikeBlock directoryBlock = (DirectoryLikeBlock) findBlockForPath(filePath);
            listDirectoryInternal(directoryBlock, filenames);
        } else {
            throw new IOException("not a directory");
        }

        return filenames.toArray(new String[0]);
    }

    private void listDirectoryInternal2(ADFBlock aBlock, List<String> filenames) throws IOException {

        filenames.add(aBlock.getName());

        if (aBlock.nextBlockWithSameHash() != 0) {
            listDirectoryInternal2((ADFBlock) specialBlock(aBlock.nextBlockWithSameHash()), filenames);
        }
    }

    private void listDirectoryInternal(DirectoryLikeBlock directoryBlock, List<String> filenames) throws IOException {
        for (int blockNumber : directoryBlock.getHashtable()) {
            if (blockNumber != 0) {
                try {
                    listDirectoryInternal2((ADFBlock) specialBlock(blockNumber), filenames);
                } catch (NoDataAtBlockException e) {
                    System.out.println("Problem loading data for " + blockNumber + " corrupt?");
                }
            }
        }

        if (directoryBlock.nextBlockWithSameHash() != 0) {
            listDirectoryInternal2((ADFBlock) specialBlock(directoryBlock.nextBlockWithSameHash()), filenames);
        }
    }


    public AmigaFile getFile(String directory, String filename) {
        if ("/".equals(directory)) {
            return getFile("/" + filename);
        }

        return getFile(directory + "/" + filename);
    }

    public int size(String filePath) throws IOException {
        return ((FileHeaderBlock) findBlockForPath(filePath)).getFileSize();
    }
}
