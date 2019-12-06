package com.olek.nbt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class FileUtils {

    public FileUtils () {

    }

    public static FileOutputStream decompress(FileOutputStream out, FileInputStream in ) {
        byte[] buffer = new byte[1024];
        try {
            GZIPInputStream is = new GZIPInputStream(in);
            int totalSize;
            while((totalSize = is.read(buffer)) > 0 )
            {
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
}
