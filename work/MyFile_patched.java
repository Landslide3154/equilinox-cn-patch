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
        for (String part : paths) {
            this.path = this.path + "/" + part;
        }
        String[] dirs = this.path.split("/");
        this.name = dirs[dirs.length - 1];
    }

    public MyFile(MyFile file, String subFile) {
        this.path = file.path + "/" + subFile;
        this.name = subFile;
    }

    public MyFile(MyFile file, String ... subFiles) {
        this.path = file.path;
        for (String part : subFiles) {
            this.path = this.path + "/" + part;
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
            InputStreamReader isr = new InputStreamReader(this.getInputStream(), "UTF-8");
            return new BufferedReader(isr);
        } catch (Exception e) {
            System.err.println("Couldn't get reader for " + this.path);
            throw e;
        }
    }

    public String getName() {
        return this.name;
    }
}
