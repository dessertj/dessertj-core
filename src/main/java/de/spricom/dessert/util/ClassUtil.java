package de.spricom.dessert.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

public final class ClassUtil {

    private ClassUtil(){}

    public static URI getURI(Class<?> clazz) {
        URL url = clazz.getResource(clazz.getSimpleName() + ".class");
        assert url != null : "Cannot find resource for " + clazz;
        try {
            return url.toURI();
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Cannot convert '" + url + "' to URI", ex);
        }
    }

    public static final File getRootFile(Class<?> clazz) {
        String filename = "/" + clazz.getName().replace('.', '/') + ".class";
        URL url = clazz.getResource(filename);
        assert url != null : "Resource " + filename + " not found!";
        if ("file".equals(url.getProtocol())) {
            assert url.getFile().endsWith(filename) : url + " does not end with " + filename;
            return new File(url.getFile().substring(0, url.getFile().length() - filename.length()));
        } else if ("jar".equals(url.getProtocol())) {
            assert url.getFile().startsWith("file:") : url + " does not start with jar:file";
            assert url.getFile().endsWith(".jar!" + filename) : url + " does not end with .jar!" + filename;
            try {
                return new File(URLDecoder.decode(url.getFile().substring("file:".length(), url.getFile().length() - filename.length() - 1), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new IllegalStateException("UTF-8 encoding not supported!", ex);
            }
        } else {
            throw new IllegalArgumentException("Unknown protocol in " + url);
        }
    }
}
