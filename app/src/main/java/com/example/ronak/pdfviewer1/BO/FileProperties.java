package com.example.ronak.pdfviewer1.BO;

public class FileProperties {
    String FileName;
    String FilePath;
    String FontType;
    String FontSize;
    int PageNumber;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getFontType() {
        return FontType;
    }

    public void setFontType(String fontType) {
        FontType = fontType;
    }

    public String getFontSize() {
        return FontSize;
    }

    public void setFontSize(String fontSize) {
        FontSize = fontSize;
    }

    public int getPageNumber() {
        return PageNumber;
    }

    public void setPageNumber(int pageNumber) {
        PageNumber = pageNumber;
    }
}
