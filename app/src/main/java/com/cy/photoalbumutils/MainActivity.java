package com.cy.photoalbumutils;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cy.cyphotoalbumutils.albumselect.model.AlbumConstant;
import com.cy.cyphotoalbumutils.albumselect.model.CropEvent;
import com.cy.cyphotoalbumutils.albumselect.model.ImageItem;
import com.cy.cyphotoalbumutils.albumselect.model.SelectNoCropEvent;
import com.cy.cyphotoalbumutils.albumselect.view.SelectActivity;
import com.cy.cyphotoalbumutils.base.BaseActivity;
import com.cy.cyphotoalbumutils.bitmap.GlideUtils;
import com.cy.cyphotoalbumutils.recyclerview.GridRecyclerView;
import com.cy.cyphotoalbumutils.recyclerview.RVAdapter;
import com.cy.translucentparent.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ImageView iv_circle;
    private RVAdapter<File> rvAdapter;
    private RVAdapter<ImageItem> rvAdapter_no_crop;
    private int count = 9;

    private boolean isBottom = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(CropEvent event) {
        //注意：图片已裁剪并且压缩
        if (isBottom) {
            GlideUtils.loadImageByGlide(this, event.getList_file().get(0).getAbsolutePath(), iv_circle, 100, 100);
            return;
        }

        rvAdapter.addAllNoNotify(event.getList_file());

        if (rvAdapter.getRealItemCount() == count) {
            rvAdapter.setHaveFootView(false);
        }
        rvAdapter.notifyDataSetChanged();

    }

    @Subscribe
    public void onEventMainThread(SelectNoCropEvent event) {


        //注意：图片尚未压缩
        rvAdapter_no_crop.addAllNoNotify(event.getList_imageItem());

        if (rvAdapter.getRealItemCount() == count) {
            rvAdapter_no_crop.setHaveFootView(false);
        }
        rvAdapter_no_crop.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);

//        iv_rectangle = (ImageView) findViewById(iv_rectangle);
        iv_circle = (ImageView) findViewById(R.id.iv_circle);
        iv_circle.setOnClickListener(this);

        rvAdapter = new RVAdapter<File>(new ArrayList<File>(), false, true) {
            @Override
            public void bindDataToFootView(MyViewHolder holder) {
                super.bindDataToFootView(holder);
            }


            @Override
            public void onItemFootClick() {
                super.onItemFootClick();
                checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        "存储权限已被禁用", new BaseActivity.OnPermissionHaveListener() {
                            @Override
                            public void onPermissionHave() {
                                isBottom = false;
                                Intent intent = new Intent(MainActivity.this, SelectActivity.class);

                                intent.putExtra(AlbumConstant.KEY_INTENT_SELECT_COUNT, count - rvAdapter.getRealItemCount());
                                //期望保存图片的宽搞要和 聚焦宽搞比例一致
                                intent.putExtra(AlbumConstant.KEY_INTENT_SELECT_WIDTH, 800);
                                intent.putExtra(AlbumConstant.KEY_INTENT_SELECT_HEIGHT, 800);
                                intent.putExtra(AlbumConstant.KEY_INTENT_IS_TO_CROP, true);
                                intent.putExtra(AlbumConstant.KEY_INTENT_CROP_FOCUS_WIDTH, ScreenUtils.getScreenWidth(MainActivity.this) * 2 / 3);
                                intent.putExtra(AlbumConstant.KEY_INTENT_CROP_FOCUS_HEIGHT, ScreenUtils.getScreenWidth(MainActivity.this) * 2 / 3);
                                intent.putExtra(AlbumConstant.KEY_INTENT_SELECT_IS_RECTANGLE, true);
                                startActivity(intent);
                            }
                        });


            }

            @Override
            public void bindDataToView(MyViewHolder holder, int position, File bean, boolean isSelected) {

                holder.setImage(R.id.iv, bean.getAbsolutePath(), 300, 300);
            }

            @Override
            public int getItemLayoutID(int viewType) {

                if ((viewType == 1) && isHaveFootView()) {
                    return R.layout.foot_main;
                }
                return R.layout.item_iv;
            }

            @Override
            public int getItemViewType(int position) {


                if ((position == rvAdapter.getItemCount() - 1) && isHaveFootView()) {
                    return 1;
                }
                return 0;
            }

            @Override
            public void onItemClick(int position, File bean) {


            }
        };
        rvAdapter_no_crop = new RVAdapter<ImageItem>(new ArrayList<ImageItem>(), false, true) {
            @Override
            public void bindDataToFootView(MyViewHolder holder) {
                super.bindDataToFootView(holder);
            }


            @Override
            public void onItemFootClick() {
                super.onItemFootClick();
                checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        "存储权限已被禁用", new BaseActivity.OnPermissionHaveListener() {
                            @Override
                            public void onPermissionHave() {
                                isBottom = false;
                                Intent intent = new Intent(MainActivity.this, SelectActivity.class);

                                intent.putExtra(AlbumConstant.KEY_INTENT_SELECT_COUNT, count - rvAdapter_no_crop.getRealItemCount());
                                //期望保存图片的宽搞要和 聚焦宽搞比例一致
                                intent.putExtra(AlbumConstant.KEY_INTENT_SELECT_WIDTH, 800);
                                intent.putExtra(AlbumConstant.KEY_INTENT_SELECT_HEIGHT, 800);
                                intent.putExtra(AlbumConstant.KEY_INTENT_IS_TO_CROP, false);
                                intent.putExtra(AlbumConstant.KEY_INTENT_CROP_FOCUS_WIDTH, ScreenUtils.getScreenWidth(MainActivity.this) * 2 / 3);
                                intent.putExtra(AlbumConstant.KEY_INTENT_CROP_FOCUS_HEIGHT, ScreenUtils.getScreenWidth(MainActivity.this) * 2 / 3);
                                intent.putExtra(AlbumConstant.KEY_INTENT_SELECT_IS_RECTANGLE, true);
                                startActivity(intent);
                            }
                        });


            }

            @Override
            public void bindDataToView(MyViewHolder holder, int position, ImageItem bean, boolean isSelected) {

                holder.setImage(R.id.iv, bean.getPath(), 300, 300);
            }

            @Override
            public int getItemLayoutID(int viewType) {

                if ((viewType == 1) && isHaveFootView()) {
                    return R.layout.foot_main;
                }
                return R.layout.item_iv;
            }

            @Override
            public int getItemViewType(int position) {


                if ((position == rvAdapter_no_crop.getItemCount() - 1) && isHaveFootView()) {
                    return 1;
                }
                return 0;
            }

            @Override
            public void onItemClick(int position, ImageItem bean) {


            }
        };

        ((GridRecyclerView) findViewById(R.id.grv)).setAdapter(rvAdapter, 3, RecyclerView.VERTICAL);
        ((GridRecyclerView) findViewById(R.id.grv_not_crop)).setAdapter(rvAdapter_no_crop, 3, RecyclerView.VERTICAL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_circle:
                isBottom = true;
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtra(AlbumConstant.KEY_INTENT_SELECT_COUNT, 1);
                //期望保存图片的宽搞要和 聚焦宽搞比例一致
                intent.putExtra(AlbumConstant.KEY_INTENT_SELECT_WIDTH, 800);
                intent.putExtra(AlbumConstant.KEY_INTENT_SELECT_HEIGHT, 800);
                intent.putExtra(AlbumConstant.KEY_INTENT_CROP_FOCUS_WIDTH, ScreenUtils.getScreenWidth(MainActivity.this) * 2 / 3);
                intent.putExtra(AlbumConstant.KEY_INTENT_CROP_FOCUS_HEIGHT, ScreenUtils.getScreenWidth(MainActivity.this) * 2 / 3);
                intent.putExtra(AlbumConstant.KEY_INTENT_SELECT_IS_RECTANGLE, false);
                startActivity(intent);
                break;
        }
    }
}
