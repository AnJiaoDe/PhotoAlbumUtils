package com.cy.cyphotoalbumutils.albumselect.view;

import android.os.Bundle;
import android.view.View;

import com.cy.cyphotoalbumutils.R;
import com.cy.cyphotoalbumutils.albumselect.model.AlbumConstant;
import com.cy.cyphotoalbumutils.albumselect.model.ImageItem;
import com.cy.cyphotoalbumutils.albumselect.model.OnPhotoFinishedEvent;
import com.cy.dialog.progress.CYProgressDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends PhotoAlbumActivity {
    private int count_pick = 1;
    private CYProgressDialog cyProgressDialog;
//    private List<ImageItem> list_item = new ArrayList<>();

    private List<ImageItem> list_select = new ArrayList<>();

    private List<ImageItem> list_preview = new ArrayList<>();
    private int position_current = 0;
    private int position_current_select = 0;

//    private List<File> list_file_no_crop = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        count_pick = getIntent().getIntExtra(AlbumConstant.KEY_INTENT_SELECT_COUNT, 1);
        cyProgressDialog = new CYProgressDialog(this);


        replaceFragment(R.id.framelayout, new SelectFragment());

    }


    @Override
    public void initData() {
        super.initData();

    }

    public CYProgressDialog getCyProgressDialog() {
        return cyProgressDialog;
    }

    public int getCount_pick() {
        return count_pick;
    }

    public void setCount_pick(int count_pick) {
        this.count_pick = count_pick;
    }

    public List<ImageItem> getList_select() {
        return list_select;
    }

    public void setList_select(ArrayList<ImageItem> list_select) {
        this.list_select = list_select;
    }

//    public List<ImageItem> getList_item() {
//        return list_item;
//    }
//
//    public void setList_item(List<ImageItem> list_item) {
//        this.list_item = list_item;
//    }

    public int getPosition_current() {
        return position_current;
    }

    public void setPosition_current(int position_current) {
        this.position_current = position_current;
    }

    public int getPosition_current_select() {
        return position_current_select;
    }

    public void setPosition_current_select(int position_current_select) {
        this.position_current_select = position_current_select;
    }

    public List<ImageItem> getList_preview() {
        return list_preview;
    }

    public void setList_preview(List<ImageItem> list_preview) {
        this.list_preview = list_preview;
    }

    @Override
    public void onPhotoFinished(File file) {

        EventBus.getDefault().post(new OnPhotoFinishedEvent(file));

    }

    @Override
    public void onAlbumFinished(File file) {

    }

    @Override
    public void onCompressFinished(File file) {

    }

    @Override
    public void onClick(View v) {
    }


}
