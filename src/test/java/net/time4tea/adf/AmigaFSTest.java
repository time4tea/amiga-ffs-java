package net.time4tea.adf;

import net.time4tea.adf.blocks.FilesystemException;
import net.time4tea.adf.blocks.RawFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AmigaFSTest {

    private AmigaFS fs;

    @BeforeEach
    void setUp() throws Exception {
        fs = new AmigaFS(new RawFile("src/test/resources/public-domain-space-case.adf"));
    }

    @Test
    public void testCanHashSomeEntries() {
        assertEquals(0, fs.hashCodeFor("EuphoniX.text"));
        assertEquals(7, fs.hashCodeFor(".info"));
        assertEquals(4, fs.hashCodeFor("9fingers.disk2"));
        assertEquals(3, fs.hashCodeFor("9fingers.disk1"));
    }

    @Test
    public void testCanReadDirectoryEntriesFromRootDirectory() throws Exception {
        assertTrue(fs.exists("/"));
        assertTrue(fs.exists("/ACEDEMO!"));
        assertTrue(fs.exists("/C"));
        assertTrue(fs.exists("/C/FastMemFirst"));
        assertTrue(fs.exists("/DATA"));
        assertTrue(fs.exists("/DATA/BOING1.SMP"));
    }

    @Test
    public void testCanReadFileFromSubdirectory() throws Exception {
        assertTrue(fs.exists("/s/startup-sequence"));
    }

    @Test
    public void testCanDetermineThatAFileIsNotADirectory() throws Exception {
        File file = fs.getFile("/ACEDEMO!");
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
    }

    @Test
    public void testCanReadAFile() throws Exception {
        InputStream inputStream = fs.inputStreamForFile("/DATA/AF.IFF");
        InputStreamReader is = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        char[] form = new char[4];
        is.read(form);

        assertEquals(new String(form), "FORM");
    }

    @Test
    public void testCanReadRootDirectory() throws Exception {
        File file = fs.getFile("/");
        assertTrue(file.exists());
        assertTrue(file.isDirectory());

        List<String> strings = file.list();
        assertTrue(strings.contains("C"));
    }

    @Test
    public void testCanReadADirectory() throws Exception {

        File file = fs.getFile("S");
        assertTrue(file.exists());
        assertTrue(file.isDirectory());

        List<String> strings = file.list();
        assertTrue(strings.contains("Startup-Sequence"));
    }

    @Test
    public void testCanFindTheParentDirectoryForAFIle() throws Exception {

        File file = fs.getFile("S");
        assertTrue(file.isDirectory());
        File file2 = new File(file, "Startup-Sequence");

        assertEquals(file, file2.getParentFile());
        assertEquals(file.getName(), file2.getParent());
    }

    public static List<String> getFileListing(File dir, FileFilter fileFilter) throws IOException {
        List<String> result = new ArrayList<String>();

        for (File entry : dir.listFiles(fileFilter)) {
            result.add(entry.getAbsolutePath());
            if (entry.isDirectory()) {
                result.addAll(getFileListing(entry, fileFilter));
            }
        }
        Collections.sort(result);
        return result;
    }

    @Test
    public void testCanDoRecursiveListing() throws Exception {
        for (String filename : getFileListing(new File(fs, "/"), file -> true)) {
            System.out.println("filename = " + filename);
        }
    }
}
