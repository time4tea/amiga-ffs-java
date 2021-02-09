package net.time4tea.adf;

import net.time4tea.adf.blocks.ADFBlock;
import net.time4tea.adf.blocks.RawFile;

import java.io.IOException;

public class TextFinder {

    public static void main(String[] args) throws IOException {

        AmigaFS loader = new AmigaFS(new RawFile("src/test/resources/public-domain-space-case.adf"));

        int blockNumber = 0;

        String searchString = "gfxmacros.h";

        while (true) {
            ADFBlock block = loader.dataBlock(blockNumber);

            String content = block.asString();

            if (content.contains(searchString)) {
                System.out.println("block.asHexDump() = " + block.asHexDump());
            }
            blockNumber++;
        }

    }
}
