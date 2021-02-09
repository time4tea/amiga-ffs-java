package net.time4tea.adf;

import net.time4tea.adf.blocks.ByteUtils;
import net.time4tea.adf.blocks.RawFile;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;

public class RdskTest {

    @BeforeEach
    void setUp() {
        Assumptions.assumeTrue(new java.io.File("amigahd.dd.image").exists());
    }

    @Test
    void something() throws IOException {
        AmigaFS fs = new AmigaFS(new RawFile("amigahd.dd.image"), AmigaFS.FilesystemCharacteristics.harddisk(87091, 119));

        File file = new File(fs, "/s/startup-sequence");
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        while((line = reader.readLine()) != null ) {
            System.out.println(line);
        }

//        List<String> list = AmigaFSTest.getFileListing(new File(fs, "/"), x -> true);
//        System.out.println("list = " + list);

    }
}
