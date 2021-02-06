package net.time4tea.adf.blocks;

public class NoDataAtBlockException extends FilesystemException {
    public NoDataAtBlockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDataAtBlockException(Throwable cause) {
        super(cause);
    }

    public NoDataAtBlockException(String cause) {
        super(cause);
    }
}
