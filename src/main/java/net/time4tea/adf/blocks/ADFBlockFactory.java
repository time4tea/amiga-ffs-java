package net.time4tea.adf.blocks;


import static net.time4tea.adf.blocks.ByteUtils.asULong;

public class ADFBlockFactory implements BlockFactory {
    private final boolean FFS;
    private final int blockIndexOffset;

    public ADFBlockFactory(boolean isFFS, int blockIndexOffset) {
        this.FFS = isFFS;
        this.blockIndexOffset = blockIndexOffset;
    }

    public DataBlock dataBlockFor(byte[] bytes) {
        if (FFS) {
            return new FfsDataBlock(bytes);
        } else {
            return new OfsDataBlock(bytes);
        }
    }

    public Block blockFor(byte[] bytes) throws UnrecognisedSpecialBlockException {

        int type = asULong(bytes, 0);
        int secondaryType = asULong(bytes, -4);
        if (type == Types.T_HEADER) {

            if (secondaryType == Types.ST_ROOT) {
                return new RootBlock(bytes, blockIndexOffset);
            } else if (secondaryType == Types.ST_FILE) {
                return new FileHeaderBlock(bytes, blockIndexOffset);
            } else if (secondaryType == Types.ST_USERDIR) {
                return new DirectoryBlock(bytes, blockIndexOffset);
            }
        } else if (type == Types.T_FEXT) {
            return new FileExtensionBlock(bytes, blockIndexOffset);
        } else if ( type == Types.T_DATA ) {
            return new OfsDataBlock(bytes);
        }

        throw new UnrecognisedSpecialBlockException("Unknown type " + type);
    }


}
