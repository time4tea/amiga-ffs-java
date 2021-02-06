package net.time4tea.adf;

import net.time4tea.adf.blocks.ADFBlock;
import net.time4tea.adf.blocks.ADFBlockFactory;
import net.time4tea.adf.blocks.Block;
import net.time4tea.adf.blocks.RawFile;
import net.time4tea.adf.blocks.UnrecognisedSpecialBlockException;
import net.time4tea.adf.blocks.FilesystemException;

import java.io.IOException;

public class DiskScanner {

    public static void main(String[] args) throws IOException {

        String filename = "src/test/resources/public-domain-space-case.adf";
        AmigaFS loader = new AmigaFS(new RawFile(filename));

        int blockNumber = 0;

        while (true) {
            Block block = loader.block(blockNumber);

            System.out.println(block.describe());
            blockNumber++;
        }

    }
}
