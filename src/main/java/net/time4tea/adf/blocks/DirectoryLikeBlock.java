package net.time4tea.adf.blocks;

import java.util.Date;

public abstract class DirectoryLikeBlock extends ADFBlock {
    public DirectoryLikeBlock(byte[] bytes, int blockNumber, int blockIndexOffset) {
        super(bytes, blockNumber, blockIndexOffset);
    }

    public int getHashTableSize() {
        return ByteUtils.asULong(bytes(), 12);
    }

    public int[] getHashtable() {
        return getPointers(getHashTableSize(), 24);
    }

    public Date getModifiedTime() {
        return fsDate(-92);
    }


}
