package net.time4tea.adf.blocks;

import net.time4tea.adf.AmigaFS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class FileBlock extends ADFBlock {
    public FileBlock(byte[] bytes, int blockNumber, int blockIndexOffset) {
        super(bytes, blockNumber, blockIndexOffset);
    }

    public int[] getDataBlocks() {
        int length = (size() / 4) - 56;
        int[] pointers = getPointers(length, 24);

        int start = 0;

        while (pointers[start] == 0) {
            start++;
        }

        int[] reversed = new int[pointers.length - start];

        int p = 0;

        for (int i = pointers.length - 1; i >= start; i--) {
            reversed[p++] = pointers[i];
        }

        return reversed;
    }

    public int getExtensionBlock() {
        return doPtr(ByteUtils.asULong(bytes(), -8));
    }

    public List<Integer> getFileBlocksFromFileHeaderBlock(AmigaFS amigaFS) throws IOException {

        List<Integer> blockList = new ArrayList<Integer>();

        int[] dataBlocks = getDataBlocks();

        do {
            for (int block : dataBlocks) {
                blockList.add(block);
            }

            dataBlocks = null;

            int extensionBlock = getExtensionBlock();

            if (extensionBlock != 0) {
                dataBlocks = ((FileBlock) amigaFS.specialBlock(extensionBlock)).getDataBlocks();
            }

        }
        while (dataBlocks != null);

        return blockList;
    }
}
