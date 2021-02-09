package net.time4tea.adf.blocks;

public class NoDataAtBlockException extends FilesystemException {

    public final int blockNumber;

    public NoDataAtBlockException(String message, int blockNumber, Throwable cause) {
        super(message, cause);
        this.blockNumber = blockNumber;
    }

    public NoDataAtBlockException(String cause, int blockNumber) {
        super(cause);
        this.blockNumber = blockNumber;
    }
}
