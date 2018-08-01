package com.cy.cyphotoalbumutils.albumselect.model;

import java.util.List;

/**
 * Created by cy on 2018/3/3.
 */

public class SelectNoCropEvent {
    private List<ImageItem> list_imageItem;

    public SelectNoCropEvent(List<ImageItem> list_imageItem) {
        this.list_imageItem = list_imageItem;
    }

    public List<ImageItem> getList_imageItem() {
        return list_imageItem;
    }

    public void setList_imageItem(List<ImageItem> list_imageItem) {
        this.list_imageItem = list_imageItem;
    }
}
