package net.time4tea.adf;

import net.time4tea.adf.blocks.ADFBlock;
import net.time4tea.adf.blocks.ADFBlockFactory;
import net.time4tea.adf.blocks.ByteUtils;
import net.time4tea.adf.blocks.FilesystemException;
import net.time4tea.adf.blocks.RawFile;

import java.io.IOException;

public class DiskDumper {

    public static void main(String[] args) throws IOException {

        AmigaFS loader = new AmigaFS(new RawFile("src/test/resources/public-domain-space-case.adf"));

        int blockNumber = 0;

        while (blockNumber <= 120) {
            ADFBlock block = loader.dataBlock(blockNumber);
            System.out.println(ByteUtils.byteArrayToHexString(block.bytes()));
            blockNumber++;
        }
    }
}
