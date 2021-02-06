package net.time4tea.adf;

import net.time4tea.adf.blocks.ADFBlock;
import net.time4tea.adf.blocks.RawFile;

import java.io.IOException;

public class TextFinder {

    public static void main(String[] args) throws IOException {

        String filename = "/data/emulate/amiga/disks/amigahd.dd.image";
        AmigaFS loader = new AmigaFS(new RawFile(filename));

        int blockNumber = 0;

        String searchString = "gfxmacros.h";

        while (true) {
            ADFBlock block = loader.dataBlock(blockNumber);

            String content = block.asString();

            if (content.contains(searchString)) {
                System.out.println("block.number = " + block.getBlockNumber());
                System.out.println("block.asHexDump() = " + block.asHexDump());
            }
            blockNumber++;
        }

    }
}
