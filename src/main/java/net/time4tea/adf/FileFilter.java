package net.time4tea.adf;

import java.io.IOException;

public interface FileFilter {
    boolean accept(AmigaFile file) throws IOException;
}
