package net.time4tea.adf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AmigaFile implements Comparable<AmigaFile> {
    private final AmigaFS amigaFS;
    private final String filePath;

    public AmigaFile(AmigaFS amigaFS, String filePath) {
        this.amigaFS = amigaFS;
        this.filePath = filePath;
    }

    public int compareTo(AmigaFile o) {
        return this.filePath.compareTo(o.filePath);
    }

    public AmigaFile(AmigaFile parentFile, String filename) {
        this.amigaFS = parentFile.amigaFS;
        if (parentFile.filePath.equals("/")) {
            this.filePath = parentFile.filePath + filename;
        } else {
            this.filePath = parentFile.filePath + "/" + filename;
        }
    }

    public boolean isFile() throws IOException {
        return exists() && ! isDirectory();
    }

    public boolean exists() throws IOException {
        return amigaFS.exists(filePath);
    }

    public InputStream getInputStream() throws IOException {
        return amigaFS.inputStreamForFile(filePath);
    }

    public boolean isDirectory() throws IOException {
        return amigaFS.isDirectory(filePath);
    }

    public List<String> list() throws IOException {
        return list(file -> true);
    }

    public List<String> list(FileFilter filter) throws IOException {
        List<String> filesToReturn = new ArrayList<String>();
        String[] filenames = amigaFS.listDirectory(filePath);
        for (String filename : filenames) {
            AmigaFile file = new AmigaFile(this, filename);
            if (filter.accept(file)) {
                filesToReturn.add(filename);
            }
        }
        return filesToReturn;
    }

    public List<AmigaFile> listFiles() throws IOException {
        return listFiles(file -> true);
    }

    public List<AmigaFile> listFiles(FileFilter filter) throws IOException {
        List<AmigaFile> filesToReturn = new ArrayList<AmigaFile>();
        String[] filenames = amigaFS.listDirectory(filePath);
        for (String filename : filenames) {
            AmigaFile file = new AmigaFile(this, filename);
            if (filter.accept(file)) {
                filesToReturn.add(file);
            }
        }
        return filesToReturn;
    }

    public String getName() {
        int index = filePath.lastIndexOf("/");
        return filePath.substring(index + 1);
    }

    public AmigaFile getParentFile() {
        if (!filePath.equals("/")) {
            String newPath = filePath.substring(0, filePath.lastIndexOf("/"));
            return new AmigaFile(amigaFS, newPath);
        }
        return this;
    }

    public String getParent() {
        return getParentFile().getName();
    }

    @Override
    public boolean equals(Object anotherFile) {
        return filePath.equals(((AmigaFile) anotherFile).filePath);
    }


    @Override
    public String toString() {
        return "File: " + filePath;
    }

    public String getAbsolutePath() {
        return filePath;
    }

    public int size() throws IOException {
        return amigaFS.size(filePath);
    }

    public AmigaFile getRoot() {
        return new AmigaFile(amigaFS, "/");
    }
}
