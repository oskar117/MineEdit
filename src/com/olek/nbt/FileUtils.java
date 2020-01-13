package com.olek.nbt;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class FileUtils {

    public FileUtils () {

    }

    public static FileOutputStream decompress(FileOutputStream out, FileInputStream in ) {
        byte[] buffer = new byte[1024];
        try {
            GZIPInputStream is = new GZIPInputStream(in);
            int totalSize;
            while((totalSize = is.read(buffer)) > 0 ) {
                out.write(buffer, 0, totalSize);
            }

            out.close();
            is.close();

            System.out.println("File Successfully decompressed");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static String decompressZlib(byte[] zbytes) throws IOException {
            String unzipped = null;
            try {
                // Add extra byte to array when Inflater is set to true
                byte[] input = new byte[zbytes.length + 1];
                System.arraycopy(zbytes, 0, input, 0, zbytes.length);
                input[zbytes.length] = 0;
                ByteArrayInputStream bin = new ByteArrayInputStream(input);
                InflaterInputStream in = new InflaterInputStream(bin);
                ByteArrayOutputStream bout = new ByteArrayOutputStream(512);
                int b;
                while ((b = in.read()) != -1) {
                    bout.write(b);
                }
                bout.close();
                unzipped = bout.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return unzipped;
    }
}
