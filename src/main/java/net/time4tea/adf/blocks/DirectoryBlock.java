package net.time4tea.adf.blocks;

public class DirectoryBlock extends DirectoryLikeBlock {
    public DirectoryBlock(byte[] bytes, int blockIndexOffset) {
        super(bytes, blockIndexOffset);
    }

    public String getType() {
        return "Directory";
    }

    @Override
    public int getHashTableSize() {
        return (bytes.length / 4) - 56;
    }

    public String describe() {
        StringBuilder b = new StringBuilder();

        b.append("Directory Block");
        b.append("\tName " + getName());
        b.append("\tParent " + getParent());
        b.append("\tEntries in hashtable " + getHashtable().length);
        b.append("\tContinued " + nextBlockWithSameHash());
        b.append("\n");

        return b.toString();
    }

}
