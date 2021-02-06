package net.time4tea.adf.blocks;

import java.time.LocalDateTime;

public class FileHeaderBlock extends FileBlock {
    public FileHeaderBlock(byte[] bytes, int blockNumber, int blockIndexOffset) {
        super(bytes, blockNumber, blockIndexOffset);
    }

    public int getUID() {
        return ByteUtils.getByte(bytes(), -196);
    }

    public int getGID() {
        return ByteUtils.getByte(bytes(), -194);
    }

    public int getFileSize() {
        return ByteUtils.asULong(bytes(), -188);
    }

    public LocalDateTime getModifiedTime() {
        return fsDate(-92);
    }

    public String getType() {
        return "File";
    }

    @Override
    public String toString() {
        return getName() + ":" + getFileSize() + " bytes ";
    }

    public String describe() {
        StringBuilder b = new StringBuilder();

        b.append("File Header Block");
        b.append("\tName " + getName());
        b.append("\tSize " + getFileSize());
        b.append("\tContinued " + nextBlockWithSameHash());

        return b.toString();
    }

}
