//定義package名稱
package com.example.express_nfc;

//宣告class Converter(處理16進制轉碼)
public class Converter {
    // Hex help
    private static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1',
            (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
            (byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
            (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };
    public static StringBuilder getHexString(byte[] raw, int len) {
        byte[] hex = new byte[2 * len];
        int index = 0;
        int pos = 0;
        for (byte b : raw) {
            if (pos >= len)
                break;
            pos++;
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        }
        return new StringBuilder(new String(hex));
    }
}