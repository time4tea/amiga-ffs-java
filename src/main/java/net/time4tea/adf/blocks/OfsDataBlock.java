package net.time4tea.adf.blocks;

public class OfsDataBlock extends DataBlock {

    public OfsDataBlock(byte[] bytes) {
        super(bytes, 0);
    }

    public byte[] dataBytes() {
        return getBytesFromOffset(24);
    }

    public int dataSize() {
        return bytes().length - 24;
    }
}
