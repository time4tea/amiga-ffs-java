package net.time4tea.adf.blocks;

public interface BlockFactory {
    Block blockFor(byte[] bytes) throws UnrecognisedSpecialBlockException;

    DataBlock dataBlockFor(byte[] bytes);
}
