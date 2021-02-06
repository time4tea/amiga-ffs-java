package net.time4tea.adf.blocks;


import static net.time4tea.adf.blocks.ByteUtils.asULong;

public class ADFBlockFactory implements BlockFactory {
    private final boolean FFS;
    private final int blockIndexOffset;

    public ADFBlockFactory(boolean isFFS, int blockIndexOffset) {
        this.FFS = isFFS;
        this.blockIndexOffset = blockIndexOffset;
    }

    public DataBlock dataBlockFor(byte[] bytes, int blockNumber) {
        if (FFS) {
            return new FfsDataBlock(bytes, blockNumber);
        } else {
            return new OfsDataBlock(bytes, blockNumber);
        }
    }

    public Block blockFor(byte[] bytes, int blockNumber) throws UnrecognisedSpecialBlockException {

        int type = asULong(bytes, 0);
        int secondaryType = asULong(bytes, -4);
        if (type == Types.T_HEADER) {

            if (secondaryType == Types.ST_ROOT) {
                return new RootBlock(bytes, blockNumber, blockIndexOffset);
            } else if (secondaryType == Types.ST_FILE) {
                return new FileHeaderBlock(bytes, blockNumber, blockIndexOffset);
            } else if (secondaryType == Types.ST_USERDIR) {
                return new DirectoryBlock(bytes, blockNumber, blockIndexOffset);
            }
        } else if (type == Types.T_FEXT) {
            return new FileExtensionBlock(bytes, blockNumber, blockIndexOffset);
        } else if ( type == Types.T_DATA ) {
            return new OfsDataBlock(bytes, blockNumber);
        }

        throw new UnrecognisedSpecialBlockException("Unknown type " + type);
    }


}
