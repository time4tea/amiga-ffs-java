package net.time4tea.adf;

import net.time4tea.adf.blocks.RawFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileTest {

    private AmigaFS fs;

    @BeforeEach
    void setUp()  throws Exception {
        fs = new AmigaFS(new RawFile("src/test/resources/public-domain-space-case.adf"));
    }

    @Test
    public void testCanFindAFile() throws Exception {

        AmigaFile file = new AmigaFile(fs, "s/startup-sequence");

        assertTrue(file.exists());
        InputStream is = file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        assertEquals("FastMemFirst", reader.readLine());
    }
}
