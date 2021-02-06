package net.time4tea.adf.blocks;

public class OfsDataBlock extends DataBlock {

    public OfsDataBlock(byte[] bytes, int blockNumber) {
        super(bytes, blockNumber, 0);
    }

    public byte[] getBytes() {
        return getBytesFromOffset(24);
    }

    public int getDataBytesInBlock() {
        return bytes().length - 24;
    }
}
