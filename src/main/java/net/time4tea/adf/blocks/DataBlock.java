package net.time4tea.adf.blocks;

import java.time.LocalDateTime;

public abstract class DataBlock extends ADFBlock {
    public DataBlock(byte[] bytes, int blockNumber, int blockIndexOffset) {
        super(bytes, blockNumber, blockIndexOffset);
    }

    public LocalDateTime getModifiedTime() {
        throw new UnsupportedOperationException();
    }

    public abstract byte[] dataBytes();

    public abstract int dataSize();

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
