package net.time4tea.adf.blocks;

import java.time.LocalDateTime;

public abstract class ADFBlock implements Block {
    private final byte[] bytes;
    private final int blockNumber;
    private final int blockIndexOffset;

    public ADFBlock(byte[] bytes, int blockNumber, int blockIndexOffset) {
        this.bytes = bytes;
        this.blockNumber = blockNumber;
        this.blockIndexOffset = blockIndexOffset;
    }

    public int size() {
        return bytes.length;
    }

    public long getPrimaryType() {
        return ByteUtils.asULong(bytes, 0);
    }

    public long getSecondaryType() {
        return ByteUtils.asULong(bytes, -4);
    }

    public byte[] bytes() {
        return bytes;
    }

    protected LocalDateTime fsDate(int offset) {
        byte[] bytes = bytes();

        int rdays = ByteUtils.asULong(bytes, offset);
        int rmins = ByteUtils.asULong(bytes, offset + 4);
        int rticks = ByteUtils.asULong(bytes, offset + 8);

        return DateCalculator.calculate(rdays, rmins, rticks);
    }

    public int nextBlockWithSameHash() {
        return doPtr(ByteUtils.asULong(bytes(), -16));
    }

    public String getName() {
        return ByteUtils.asString(bytes(), -79, -80);
    }

    public long getHeaderKey() {
        return ByteUtils.asULong(bytes(), 4);
    }

    public long getHighSeq() {
        return ByteUtils.asULong(bytes(), 8);
    }

    protected int[] getPointers(int length, int startOffset) {
        int[] entries = new int[length];

        for (int i = 0; i < length; i++) {
            entries[i] = doPtr(ByteUtils.asULong(bytes(), startOffset + (i * 4)));
        }
        return entries;
    }

    protected int doPtr(int blockNumber) {
        if (blockNumber != 0) {
            return blockNumber + blockIndexOffset;
        }
        return 0;
    }

    public abstract LocalDateTime getModifiedTime();

    public abstract String getType();

    public int getParent() {
        return doPtr(ByteUtils.asULong(bytes(), -12));
    }

    public String asHexDump() {
        return ByteUtils.byteArrayToHexString(bytes());
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public String asString() {
        return ByteUtils.byteArrayToString(bytes);
    }
}
