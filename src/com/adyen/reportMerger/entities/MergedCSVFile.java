package com.adyen.reportMerger.entities;

import java.io.File;


//CLass not used for now
public class MergedCSVFile {

    private File file;
    private String path;
    private MergedCSVFile instance;

    private MergedCSVFile(){

    }

    public MergedCSVFile getInstance() {
        if (instance == null) {
            instance = new MergedCSVFile();
        }
        return instance;
    }

    private void createFile(){
        if (file == null) {
            file = new File(path);
        }
    }




}
