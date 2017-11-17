package com.jthinking.monitor.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * java.util.Properties扩展，更适合Web应用
 * @author JiaBochao
 * @version 2017-11-17 10:31:57
 */
public class ClasspathProperties extends Properties {

    public ClasspathProperties() {
        super();
    }

    public ClasspathProperties(String path) throws IOException {
        super();
        load(path);
    }

    public void load(String path) throws IOException {
        if (path.startsWith("classpath:")) {
            path = path.substring("classpath:".length());
            URL resource = ClasspathProperties.class.getClassLoader().getResource(path);
            if (resource == null)
                throw new FileNotFoundException(path);
            super.load(resource.openStream());
        } else {
            super.load(new FileInputStream(path));
        }
    }
}
