package net.time4tea.adf.blocks;

import java.util.Date;

public abstract class DataBlock extends ADFBlock {
    public DataBlock(byte[] bytes, int blockNumber, int blockIndexOffset) {
        super(bytes, blockNumber, blockIndexOffset);
    }

    public Date getModifiedTime() {
        throw new UnsupportedOperationException();
    }

    public abstract byte[] getBytes();

    public abstract int getDataBytesInBlock();

    protected byte[] getBytesFromOffset(int startoffset) {
        byte[] bytes = bytes();
        byte[] data = new byte[bytes.length - startoffset];
        if (bytes.length - startoffset >= 0)
            System.arraycopy(bytes, startoffset, data, 0, bytes.length - startoffset);
        return data;
    }

    public String getType() {
        return "Data";
    }

    public String describe() {
        return "Data Block\n";
    }
}
