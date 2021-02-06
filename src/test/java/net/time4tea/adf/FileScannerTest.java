package net.time4tea.adf;

import net.time4tea.adf.blocks.ADFBlock;
import net.time4tea.adf.blocks.ADFBlockFactory;
import net.time4tea.adf.blocks.FileHeaderBlock;
import net.time4tea.adf.blocks.RawFile;
import net.time4tea.adf.blocks.DataBlock;

public class FileScannerTest {
    /** translator.h = 157728 **/

    /**
     * File memory.h 157845
     * Directory graphics 157850
     * File text.h 157851
     * File clip.h 157859
     * File regions.h 157865
     * File copper.h 157867
     * File gfxbase.h 157873
     * File display.h 157878
     * File sprite.h 157882
     * File view.h 157884
     * File gfxmacros.h 157889
     * File rastport.h 157893
     * File layers.h 157902
     * File gfx.h 157906
     * File graphint.h 157909
     * File gels.h 157911
     * File collide.h 157931
     * File stdarg.h 157935
     * File stdio.h 157937
     * File stddef.h 157947
     * File assert.h 157949
     *
     */
    public void testSomething() throws Exception {

        String filename = "src/test/resources/public-domain-space-case.adf";
        AmigaFS filesystem = new AmigaFS(new RawFile(filename));

        ADFBlock block = (ADFBlock) filesystem.specialBlock(157889);

        System.out.println(block.getType() + " " + block.getName() + " " + block.getBlockNumber());

        FileHeaderBlock header = (FileHeaderBlock) block;

        System.out.println("header.getName() = " + header.getName());
        System.out.println("header.getFileSize() = " + header.getFileSize());

        System.out.println("headerdata = " + header.asHexDump());

        for (int dataBlock : header.getDataBlocks()) {
            if (dataBlock != 0) {
                DataBlock data = filesystem.dataBlock(dataBlock);
                System.out.println("data.getBlockNumber() = " + Integer.toHexString(data.getBlockNumber()));
                System.out.println("data.asHexDump() = " + data.asHexDump());
            }
        }
    }
}


