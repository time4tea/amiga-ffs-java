package net.time4tea.adf.blocks;

import java.time.LocalDateTime;

public abstract class DataBlock extends ADFBlock {
    public DataBlock(byte[] bytes, int blockIndexOffset) {
        super(bytes, blockIndexOffset);
    }

    public LocalDateTime getModifiedTime() {
        throw new UnsupportedOperationException();
    }

    public abstract byte[] dataBytes();

    public abstract int dataSize();

    protected byte[] getBytesFromOffset(int startoffset) {
        byte[] data = new byte[bytes.length - startoffset];
        if (bytes.length - startoffset >= 0) {
            System.arraycopy(bytes, startoffset, data, 0, bytes.length - startoffset);
        }
        return data;
    }

    public String getType() {
        return "Data";
    }

    public String describe() {
        return "Data Block\n";
    }
}
