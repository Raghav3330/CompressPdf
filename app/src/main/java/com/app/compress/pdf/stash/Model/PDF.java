package com.app.compress.pdf.stash.Model;

import java.io.File;
import java.io.Serializable;

public class PDF implements Serializable {
    private static final long id = 1L;
    public PDF(){
        
    }
    private String filename;
    private String size;
    private String date;
    private File sourcepath;
    private String compareDate;

    public String getCompareDate() {
        return compareDate;
    }

    public void setCompareDate(String compareDate) {
        this.compareDate = compareDate;
    }

    public File getSourcepath() {
        return sourcepath;
    }

    public void setSourcepath(File sourcepath) {
        this.sourcepath = sourcepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
