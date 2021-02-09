package net.time4tea.adf;

import net.time4tea.adf.blocks.FilesystemException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class File implements Comparable<File> {
    private final AmigaFS amigaFS;
    private final String filePath;

    public File(AmigaFS amigaFS, String filePath) {
        this.amigaFS = amigaFS;
        this.filePath = filePath;
    }

    public int compareTo(File o) {
        return this.filePath.compareTo(o.filePath);
    }

    public File(File parentFile, String filename) {
        this.amigaFS = parentFile.amigaFS;
        if (parentFile.filePath.equals("/")) {
            this.filePath = parentFile.filePath + filename;
        } else {
            this.filePath = parentFile.filePath + "/" + filename;
        }
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
        return list(new FileFilter() {
            public boolean accept(File file) throws IOException {
                return true;
            }
        });
    }

    public List<String> list(FileFilter filter) throws IOException {
        List<String> filesToReturn = new ArrayList<String>();
        String[] filenames = amigaFS.listDirectory(filePath);
        for (String filename : filenames) {
            File file = new File(this, filename);
            if (filter.accept(file)) {
                filesToReturn.add(filename);
            }
        }
        return filesToReturn;
    }

    public List<File> listFiles() throws IOException {
        return listFiles(file -> true);
    }

    public List<File> listFiles(FileFilter filter) throws IOException {
        List<File> filesToReturn = new ArrayList<File>();
        String[] filenames = amigaFS.listDirectory(filePath);
        for (String filename : filenames) {
            File file = new File(this, filename);
            if (filter.accept(file)) {
                filesToReturn.add(file);
            }
        }
        return filesToReturn;
    }

    public String getName() {
        return filePath;
    }

    public File getParentFile() {
        if (!filePath.equals("/")) {
            String newPath = filePath.substring(0, filePath.lastIndexOf("/"));
            return new File(amigaFS, newPath);
        }
        return this;
    }

    public String getParent() {
        return getParentFile().getName();
    }

    @Override
    public boolean equals(Object anotherFile) {
        return filePath.equals(((File) anotherFile).filePath);
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

    public File getRoot() {
        return new File(amigaFS, "/");
    }
}
