package net.time4tea.adf.blocks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RawFile {
    private final RandomAccessFile file;

    public RawFile(String filename) throws FileNotFoundException {
        this.file = new RandomAccessFile(filename, "r");
    }

    public Blocks blocksize(int blockSize) {
        return new Blocks(blockSize);
    }

    public class Blocks {
        private final int blockSize;

        public Blocks(int blockSize) {
            this.blockSize = blockSize;
        }

        public byte[] bytesFor(int blockNumber) throws IOException {
            int offset = blockNumber * blockSize;
            file.seek(offset);

            byte[] bytes = new byte[blockSize];
            if (!(file.read(bytes) == blockSize)) {
                throw new NoDataAtBlockException("Unable to read " + blockSize + " bytes at offset " + offset);
            }
            return bytes;
        }

        public int blockSize() {
            return blockSize;
        }
    }
}
