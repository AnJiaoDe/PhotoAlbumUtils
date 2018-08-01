package com.cy.cyphotoalbumutils.albumselect.model;

import java.util.List;

/**
 * Created by cy on 2018/3/3.
 */

public class OnLoadFinishedEvent {
    private List<ImageItem> list_item;
    private List<ImageFolder> list_folder;

    public OnLoadFinishedEvent(List<ImageItem> list_item, List<ImageFolder> list_folder) {
        this.list_item = list_item;
        this.list_folder = list_folder;
    }

    public List<ImageItem> getList_item() {
        return list_item;
    }

    public void setList_item(List<ImageItem> list_item) {
        this.list_item = list_item;
    }

    public List<ImageFolder> getList_folder() {
        return list_folder;
    }

    public void setList_folder(List<ImageFolder> list_folder) {
        this.list_folder = list_folder;
    }
}
