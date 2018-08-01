package com.cy.cyphotoalbumutils.albumselect.model;

import java.io.File;

/**
 * Created by cy on 2018/3/2.
 */

public class OnPhotoFinishedEvent {
    private File file;

    public OnPhotoFinishedEvent(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
