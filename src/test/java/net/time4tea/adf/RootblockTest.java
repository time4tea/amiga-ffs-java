package net.time4tea.adf;

import net.time4tea.adf.blocks.ADFBlock;
import net.time4tea.adf.blocks.FileBlock;
import net.time4tea.adf.blocks.FileHeaderBlock;
import net.time4tea.adf.blocks.RawFile;
import net.time4tea.adf.blocks.RootBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RootblockTest {
    private AmigaFS file;
    private RootBlock rootBlock;

    @BeforeEach
    protected void setUp() throws Exception {
        file = new AmigaFS(new RawFile("src/test/resources/public-domain-space-case.adf"));
        rootBlock = file.getRootblock(880);
    }

    @Test
    public void testCanIdentifyRootblockFromDiskGeometry() throws Exception {
        assertEquals(RootBlock.PRIMARY_TYPE, rootBlock.getPrimaryType());
        assertEquals(RootBlock.SECONDARY_TYPE, rootBlock.getSecondaryType());
    }

    @Test
    public void testCanGetTheNameOfTheDisk() throws Exception {
        assertEquals("ACE!", rootBlock.getName());
        System.out.println("Created: " + rootBlock.getFilesystemCreationDate());
        System.out.println("Root Modified: " + rootBlock.getModifiedTime());
        System.out.println("Disk Modified: " + rootBlock.getDiskModifiedTime());
    }

    @Test
    public void testCanFollowRootBlockToFirstFile() throws Exception {
        int[] hashtable = rootBlock.getHashtable();
        for (int l : hashtable) {
            if (l != 0) {
                ADFBlock block = (ADFBlock) file.specialBlock(l);
                if (block instanceof FileBlock) {
                    FileHeaderBlock fileHeader = (FileHeaderBlock) block;
                    System.out.println(fileHeader.getName() + " " + fileHeader.getFileSize() + " " + fileHeader.getModifiedTime());
                    for (int dataBlockId : fileHeader.getDataBlocks()) {
                        file.specialBlock(dataBlockId);
                    }
                }
                break;
            }
        }
    }
}
