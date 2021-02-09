package net.time4tea.adf.blocks;

public class FfsDataBlock extends DataBlock {

    public FfsDataBlock(byte[] bytes) {
        super(bytes, 0);
    }

    public byte[] dataBytes() {
        return getBytesFromOffset(0);
    }

    public int dataSize() {
        return bytes().length;
    }
}
