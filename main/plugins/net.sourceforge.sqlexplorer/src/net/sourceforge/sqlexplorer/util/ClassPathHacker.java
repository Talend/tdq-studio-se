package net.sourceforge.sqlexplorer.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassPathHacker {

    private static final Class[] parameters = new Class[] { URL.class };

    public static void addFile(String s) throws IOException {
        File f = new File(s);
        addFile(f);
    }// end method

    public static void addFile(File f) throws IOException {
        addURL(f.toURL());
    }// end method

    public static void addURL(URL url) throws IOException {
        URL[] urls = new URL[] { url };
        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        URLClassLoader sysloader = new URLClassLoader(urls, threadClassLoader);
        Class sysclass = URLClassLoader.class;

        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { url });
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        } // end try catch

    }// end method

}// end class
