package com.threepmanagerapi.threepmanagerapi.settings.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class FileProcessingUtil {
    public static byte[] compressFile(byte[] data) {

        try {
            Deflater deflater = new Deflater();
            deflater.setLevel(Deflater.BEST_COMPRESSION);
            deflater.setInput(data);
            deflater.finish();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);

            byte[] tmp = new byte[4 * 1024];
            while (!deflater.finished()) {
                int size = deflater.deflate(tmp);
                byteArrayOutputStream.write(tmp, 0, size);
            }
            byteArrayOutputStream.close();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(FileProcessingUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static byte[] decompressFile(byte[] data) {

        try {
            Inflater inflater = new Inflater();
            inflater.setInput(data);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
            byte[] tmp = new byte[4 * 1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                byteArrayOutputStream.write(tmp, 0, count);

            }
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException | DataFormatException ex) {
            return null;
        }

    }
}
