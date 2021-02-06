package net.time4tea.adf.blocks;

public class FfsDataBlock extends DataBlock {

    public FfsDataBlock(byte[] bytes, int blockNumber) {
        super(bytes, blockNumber, 0);
    }

    public byte[] getBytes() {
        return getBytesFromOffset(0);
    }

    public int getDataBytesInBlock() {
        return bytes().length;
    }
}
