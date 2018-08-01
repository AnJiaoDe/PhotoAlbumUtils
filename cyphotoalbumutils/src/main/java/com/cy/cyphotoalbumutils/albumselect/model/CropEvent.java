package com.cy.cyphotoalbumutils.albumselect.model;

import java.io.File;
import java.util.List;

/**
 * Created by cy on 2018/3/3.
 */

public class CropEvent {
    private List<File> list_file;

    public CropEvent(List<File> list_file) {
        this.list_file = list_file;
    }

    public List<File> getList_file() {
        return list_file;
    }

    public void setList_file(List<File> list_file) {
        this.list_file = list_file;
    }
}
