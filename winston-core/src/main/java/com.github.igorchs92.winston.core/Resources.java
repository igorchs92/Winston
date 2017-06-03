package com.github.igorchs92.winston.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * The type Resources.
 *
 * @author Created by Igor Chernomorets on 25-11-2016.
 */
public final class Resources {

    private static final File root = new File(System.getProperty("user.dir") + File.separator + "bin");

    // private constructor to avoid unnecessary instantiation of the class
    private Resources() {

    }

    /**
     * Gets resource.
     *
     * @param name the name
     * @return the resource
     */
    public static URL getResource(String name) {
        return getClassLoader().getResource(name);
    }

    public static Enumeration<URL> getResources(String name) throws IOException {
        return getClassLoader().getResources(name);
    }

    public static URL getSystemResource(String name) {
        return ClassLoader.getSystemResource(name);
    }

    public static Enumeration<URL> getSystemResources(String name) throws IOException {
        return ClassLoader.getSystemResources(name);
    }

    public static InputStream getResourceAsStream(String name) {
        return getClassLoader().getResourceAsStream(name);
    }

    public static InputStream getSystemResourceAsStream(String name) {
        return ClassLoader.getSystemResourceAsStream(name);
    }

    /**
     * Gets resource as file.
     *
     * @param name the name
     * @return the resource as file
     */
    public static File getFile(String name) {
        return new File(root, name);
    }

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


}
