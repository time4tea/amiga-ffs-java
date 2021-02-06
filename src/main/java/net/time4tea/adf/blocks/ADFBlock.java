package net.time4tea.adf.blocks;


import java.util.Calendar;
import java.util.Date;

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

    protected Date fsDate(int offset) {
        int rdays = ByteUtils.asULong(bytes(), offset);
        int rmins = ByteUtils.asULong(bytes(), offset + 4);
        int rticks = ByteUtils.asULong(bytes(), offset + 8);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.YEAR, 1978);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.DATE, rdays);
        cal.add(Calendar.MINUTE, rmins);
        cal.add(Calendar.SECOND, rticks / 50);
        cal.add(Calendar.MILLISECOND, (rticks % 50) * (1000 / 50));

        return cal.getTime();
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

    public abstract Date getModifiedTime();

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
