package net.time4tea.adf.blocks;

import java.io.IOException;

public class FilesystemException extends IOException {
    public FilesystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilesystemException(Throwable cause) {
        super(cause);
    }

    public FilesystemException(String cause) {
        super(cause);
    }
}
