/*
 * Decompiled with CFR 0.152.
 */
package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import utils.CSVReader;

public class MyFile {
    private String path;
    private String name;

    public MyFile(String path) {
        this.path = "/" + path;
        String[] dirs = path.split("/");
        this.name = dirs[dirs.length - 1];
    }

    public MyFile(String ... paths) {
        this.path = "";
        String[] stringArray = paths;
        int n = paths.length;
        int n2 = 0;
        while (n2 < n) {
            String part = stringArray[n2];
            this.path = String.valueOf(this.path) + "/" + part;
            ++n2;
        }
        String[] dirs = this.path.split("/");
        this.name = dirs[dirs.length - 1];
    }

    public MyFile(MyFile file, String subFile) {
        this.path = String.valueOf(file.path) + "/" + subFile;
        this.name = subFile;
    }

    public MyFile(MyFile file, String ... subFiles) {
        this.path = file.path;
        String[] stringArray = subFiles;
        int n = subFiles.length;
        int n2 = 0;
        while (n2 < n) {
            String part = stringArray[n2];
            this.path = String.valueOf(this.path) + "/" + part;
            ++n2;
        }
        String[] dirs = this.path.split("/");
        this.name = dirs[dirs.length - 1];
    }

    public String getPath() {
        return this.path;
    }

    public String toString() {
        return this.getPath();
    }

    public InputStream getInputStream() {
        return this.getClass().getResourceAsStream(this.path);
    }

    public URL getUrl() {
        return this.getClass().getResource(this.path);
    }

    public CSVReader openCsvReader() throws Exception {
        return new CSVReader(this);
    }

    public BufferedReader getReader() throws Exception {
        try {
            InputStreamReader isr = new InputStreamReader(this.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            return reader;
        }
        catch (Exception e) {
            System.err.println("Couldn't get reader for " + this.path);
            throw e;
        }
    }

    public String getName() {
        return this.name;
    }
}

