package de.spricom.dessert.resolve;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class IOUtils {

    private IOUtils() {};

    public static byte[] readAll(InputStream in) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int read;
        while ((read = in.read(buf)) != -1) {
            os.write(buf, 0, read);
        }
        os.flush();
        in.close();
        return os.toByteArray();
    }

}
