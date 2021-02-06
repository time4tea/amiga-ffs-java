package net.time4tea.adf.blocks;

public interface BlockFactory {
    Block blockFor(byte[] bytes, int blockNumber) throws UnrecognisedSpecialBlockException;

    DataBlock dataBlockFor(byte[] bytes, int blockNumber);
}
