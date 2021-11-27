package net.time4tea.adf.blocks;

import net.time4tea.adf.AmigaFS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class FileBlock extends ADFBlock {
    public FileBlock(byte[] bytes, int blockIndexOffset) {
        super(bytes, blockIndexOffset);
    }

    public int[] getDataBlocks() {
        int length = (size() / 4) - 56;
        int[] pointers = getPointers(length, 24);

        int start = 0;

        while (start < pointers.length && pointers[start] == 0) {
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

    private void add(List<Integer> list, int[] items) {
        for (int item : items) {
            list.add(item);
        }
    }

    public List<Integer> getFileBlocksFromFileHeaderBlock(AmigaFS amigaFS) throws IOException {

        List<Integer> blockList = new ArrayList<>();

        int[] dataBlocks = getDataBlocks();

        add(blockList, dataBlocks);

        int extensionBlock = getExtensionBlock();

        while (extensionBlock != 0) {
            FileExtensionBlock fileExtensionBlock = (FileExtensionBlock) amigaFS.specialBlock(extensionBlock);
            add(blockList, fileExtensionBlock.getDataBlocks());
            extensionBlock = fileExtensionBlock.getExtensionBlock();
        }

        return blockList;
    }
}
