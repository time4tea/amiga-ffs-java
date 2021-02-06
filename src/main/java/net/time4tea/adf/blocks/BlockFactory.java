package net.time4tea.adf.blocks;

public interface BlockFactory {
    Block blockFor(byte[] bytes, int blockNumber) throws UnrecognisedSpecialBlockException;

    Block dataBlockFor(byte[] bytes, int blockNumber);
}
