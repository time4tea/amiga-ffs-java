package net.time4tea.adf.blocks;

public class UnrecognisedSpecialBlockException extends FilesystemException {

    public UnrecognisedSpecialBlockException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public UnrecognisedSpecialBlockException(Throwable throwable) {
        super(throwable);
    }

    public UnrecognisedSpecialBlockException(String reason) {
        super(reason);
    }
}
