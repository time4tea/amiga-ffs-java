package net.time4tea.adf.blocks;

import java.util.Date;

public class FileExtensionBlock extends FileBlock {
    public FileExtensionBlock(byte[] bytes, int blockNumber, int blockIndexOffset) {
        super(bytes, blockNumber, blockIndexOffset);
    }

    public Date getModifiedTime() {
        throw new UnsupportedOperationException();
    }

    public String getType() {
        return "FileExtension";
    }

    public String describe() {
        StringBuilder b = new StringBuilder();

        b.append("File Extension Block");
        b.append("\tName " + getName());
        b.append("\tContinued " + nextBlockWithSameHash());

        return b.toString();
    }
}
