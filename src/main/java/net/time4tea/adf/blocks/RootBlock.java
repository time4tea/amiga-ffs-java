package net.time4tea.adf.blocks;

import java.util.Date;


public class RootBlock extends DirectoryLikeBlock {

    public static final long PRIMARY_TYPE = Types.T_HEADER;
    public static final long SECONDARY_TYPE = Types.ST_ROOT;

    public RootBlock(byte[] bytes, int blockNumber, int blockIndexOffset) {
        super(bytes, blockNumber, blockIndexOffset);
    }

    public long getFirstData() {
        return ByteUtils.asULong(bytes(), 16);
    }

    public long getChecksum() {
        return ByteUtils.asULong(bytes(), 20);
    }

    public Date getDiskModifiedTime() {
        return fsDate(-40);
    }

    public Date getFilesystemCreationDate() {
        return fsDate(-28);
    }

    public String getType() {
        return "Root";
    }

    public String describe() {
        StringBuilder b = new StringBuilder();

        b.append("Root Block");
        b.append("\tName " + getName());
        b.append("\tContinued " + nextBlockWithSameHash());
        return b.toString();
    }
}
