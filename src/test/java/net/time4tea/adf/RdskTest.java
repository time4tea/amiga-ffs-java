package net.time4tea.adf;

import net.time4tea.adf.blocks.RawFile;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class RdskTest {

    @BeforeEach
    void setUp() {
        Assumptions.assumeTrue(new File("amigahd.dd.image").exists());
    }

    @Test
    void something() throws IOException {
        AmigaFS fs = new AmigaFS(new RawFile("amigahd.dd.image"), AmigaFS.FilesystemCharacteristics.harddisk(87091, 119));

        AmigaFile file = new AmigaFile(fs, "/s/startup-sequence");
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

//        List<String> list = AmigaFSTest.getFileListing(new File(fs, "/"), x -> true);
//        System.out.println("list = " + list);

    }

    @Test
    public void blah() throws Exception {
        AmigaFS fs = new AmigaFS(new RawFile("amigahd.dd.image"), AmigaFS.FilesystemCharacteristics.harddisk(87091, 119));

        AmigaFile from = new AmigaFile(fs, "/Clib/frog");

        boolean exists = from.exists();
        System.out.println("exists = " + exists);
    }

    @Test
    void somethingElse() throws IOException {
        AmigaFS fs = new AmigaFS(new RawFile("amigahd.dd.image"), AmigaFS.FilesystemCharacteristics.harddisk(87091, 119));

        AmigaFile from = new AmigaFile(fs, "/");

        File to = new File("/home/richja/tmp/amiga");

        copyRecursively(from, to);
    }

    private void copyRecursively(AmigaFile from, File to) throws IOException {
        if (from.isDirectory()) {

            System.out.println("Dir: " + from);

            if (!to.exists()) {
                if (!to.mkdir()) {
                    throw new IOException("Cannot create " + to);
                }
            }
            for (AmigaFile amigaFile : from.listFiles()) {
                copyRecursively(amigaFile, new File(to, amigaFile.getName()));
            }
        } else {
            System.out.println("File: " + from);
            try {
                try (InputStream inputStream = from.getInputStream()) {
                    Files.copy(inputStream, to.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            catch (IOException e ) {
                System.out.println("Error copying " + from + " " + e);
            }
        }
    }

}
