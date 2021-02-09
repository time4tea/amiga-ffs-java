package net.time4tea.adf.blocks;

public class ByteUtils {
    public static int asULong(byte[] bytes, int offset) {
        offset = offset(bytes, offset);

        int number = b(bytes, offset) << 24;
        number += b(bytes, offset + 1) << 16;
        number += b(bytes, offset + 2) << 8;
        number += b(bytes, offset + 3);

        return number;
    }

    private static int b(byte[] bytes, int offset) {
        return bytes[offset] & 0xFF;
    }

    private static int offset(byte[] bytes, int offset) {
        if (offset < 0) {
            offset = bytes.length + offset;
        }
        return offset;
    }

    public static int getByte(byte[] bytes, int offset) {
        return b(bytes, offset(bytes, offset));
    }

    public static String string(byte[] bytes, int start, int len) {
        StringBuilder sb = new StringBuilder(len);
        for ( int i = 0 ; i < len; i++ ) {
            sb.append((char) bytes[start + i]);
        }
        return sb.toString();
    }

    public static String asString(byte[] bytes, int stringOffset, int lengthOffset) {
        int numChars = getByte(bytes, lengthOffset);
        StringBuilder sb = new StringBuilder(numChars);
        for (int i = 0; i < numChars; i++) {
            sb.append((char) getByte(bytes, stringOffset + i));
        }

        return sb.toString();
    }

    public static String byteArrayToHexString(byte[] in) {

        if (in == null || in.length <= 0) {
            return "null";
        }

        int offset = 0;

        StringBuilder out = new StringBuilder();

        while (offset < in.length) {

            StringBuilder hexLine = new StringBuilder();
            StringBuilder charLine = new StringBuilder();

            int bytesPerLine = 32;

            hexLine.append(Integer.toHexString(offset));
            hexLine.append(": ");

            for (int i = 0; i < bytesPerLine; i++) {
                int actualoffset = offset + i;
                if (actualoffset < in.length) {
                    byte b = in[actualoffset];
                    hexLine.append(byteToHex(b));
                    charLine.append(printableCharFor(b));
                } else {
                    hexLine.append("  ");
                }
                hexLine.append(" ");
            }

            offset += bytesPerLine;
            out.append(hexLine);
            out.append(" :: ");
            out.append(charLine);
            out.append("\n");
        }

        return out.toString();

    }

    private static char printableCharFor(byte b) {
        char character = (char) b;
        if (Character.isLetterOrDigit(character) || Character.getType(character) == Character.OTHER_PUNCTUATION) {
            return character;
        }
        return '.';
    }

    private static String byteToHex(byte b) {
        char[] string = new char[2];
        char[] pseudo = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte ch;
        ch = (byte) (b & 0xF0);
        ch = (byte) (ch >>> 4);
        ch = (byte) (ch & 0x0F);
        string[0] = (pseudo[ch]);
        ch = (byte) (b & 0x0F);
        string[1] = (pseudo[ch]);
        return new String(string);
    }

    public static String byteArrayToString(byte[] bytes) {
        StringBuilder b = new StringBuilder();
        for (byte b1 : bytes) {
            b.append(printableCharFor(b1));
        }
        return b.toString();
    }
}
