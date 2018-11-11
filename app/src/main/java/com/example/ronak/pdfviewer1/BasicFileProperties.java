package com.example.ronak.pdfviewer1;

import java.io.Serializable;

public class BasicFileProperties implements Serializable {

    public String fileName;
    public String filePath;

    public BasicFileProperties(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
