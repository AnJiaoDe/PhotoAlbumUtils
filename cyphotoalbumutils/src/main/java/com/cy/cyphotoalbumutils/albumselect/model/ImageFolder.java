package com.cy.cyphotoalbumutils.albumselect.model;

import java.io.Serializable;
import java.util.List;

public class ImageFolder implements Serializable {

    private String name;  //当前文件夹的名字
    private String path;  //当前文件夹的路径
    private ImageItem cover;   //当前文件夹需要要显示的缩略图，默认为最近的一次图片
    private List<ImageItem> list;  //当前文件夹下所有图片的集合

    /** 只要文件夹的路径和名字相同，就认为是相同的文件夹 */
    @Override
    public boolean equals(Object o) {
        try {
            ImageFolder other = (ImageFolder) o;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }

    public ImageFolder() {
    }

    public ImageFolder(String name, String path, ImageItem cover, List<ImageItem> list) {
        this.name = name;
        this.path = path;
        this.cover = cover;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ImageItem getCover() {
        return cover;
    }

    public void setCover(ImageItem cover) {
        this.cover = cover;
    }

    public List<ImageItem> getList() {
        return list;
    }

    public void setList(List<ImageItem> list) {
        this.list = list;
    }
}
