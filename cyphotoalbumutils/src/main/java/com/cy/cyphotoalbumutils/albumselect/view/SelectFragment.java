package com.cy.cyphotoalbumutils.albumselect.view;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cy.cyphotoalbumutils.R;
import com.cy.cyphotoalbumutils.albumselect.model.AlbumConstant;
import com.cy.cyphotoalbumutils.albumselect.model.ImageFolder;
import com.cy.cyphotoalbumutils.albumselect.model.ImageItem;
import com.cy.cyphotoalbumutils.albumselect.model.OnPhotoFinishedEvent;
import com.cy.cyphotoalbumutils.albumselect.model.PreviewSelectEvent;
import com.cy.cyphotoalbumutils.albumselect.model.SelectNoCropEvent;
import com.cy.cyphotoalbumutils.base.BaseActivity;
import com.cy.cyphotoalbumutils.base.BaseFragment;
import com.cy.cyphotoalbumutils.recyclerview.GridRecyclerView;
import com.cy.cyphotoalbumutils.recyclerview.RVAdapter;
import com.cy.cyphotoalbumutils.recyclerview.VerticalRecyclerView;
import com.cy.cyphotoalbumutils.utils.ScreenUtils;
import com.cy.cyphotoalbumutils.utils.TimeAndAgeUtils;
import com.cy.dialog.BaseDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectFragment extends BaseFragment {
    private View view;


    private RVAdapter<ImageItem> rvAdapter_item;
    private RVAdapter<ImageFolder> rvAdapter_folder;
    private BaseDialog dialog_all;

    private TextView tv_all;

    private Button btn_complete, btn_preview;


    private LoaderManager loaderManager;
    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型  1450518608
    private List<ImageItem> list_item = new ArrayList<>();

    private List<ImageFolder> list_folder = new ArrayList<>();

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(OnPhotoFinishedEvent event) {
        File file = event.getFile();
        //封装实体
        ImageItem imageItem = new ImageItem(file.getName(), file.getAbsolutePath(), file.length(), 500, 500, "image/jpeg",
                TimeAndAgeUtils.getCureentTimeStampLong(), false);


        rvAdapter_item.addToHead(imageItem);
        ImageFolder imageFolder_first = ((List<ImageFolder>) rvAdapter_folder.getList_bean()).get(0);
        imageFolder_first.getList().add(0, imageItem);
        imageFolder_first.setCover(imageItem);
        rvAdapter_folder.notifyDataSetChanged();

    }

    @Subscribe
    public void onEventMainThread(PreviewSelectEvent event) {

        rvAdapter_item.notifyDataSetChanged();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);


        dialog_all = new BaseDialog(getContext());


        dialog_all.setOffset(0,getContext().getResources().getDimensionPixelSize(R.dimen.height_title));
        dialog_all.config(R.layout.dialog_all_pic, Gravity.BOTTOM, BaseDialog.AnimInType.BOTTOM,
                ScreenUtils.getScreenWidth(getContext()), (int) (ScreenUtils.getScreenHeight(getContext())*0.6), true);


        //?????????????????????????????????????????????????????????????????????????????????
        rvAdapter_item = new RVAdapter<ImageItem>(new ArrayList<ImageItem>(), true) {

            @Override
            public void onItemHeadClick() {
                super.onItemHeadClick();
                myActivity.checkPermission(new String[]{Manifest.permission.CAMERA},
                        "相机权限已被禁用", new BaseActivity.OnPermissionHaveListener() {
                            @Override
                            public void onPermissionHave() {
                                ((SelectActivity) myActivity).createIntentOfPhoto(TimeAndAgeUtils.getCureentTimeStamp(),
                                        ((SelectActivity) myActivity).getCyProgressDialog());

                            }
                        });

            }

            @Override
            public void bindDataToHeadView(MyViewHolder holder) {
                super.bindDataToHeadView(holder);
            }

            @Override
            public void bindDataToView(final MyViewHolder holder, int position, final ImageItem bean, boolean isSelected) {


                holder.setImage(R.id.iv_thumb, bean.getPath(), 300, 300);

                holder.setVisibility(R.id.view_shade, bean.isChecked());
                holder.setImageResource(R.id.iv_check, bean.isChecked() ? R.drawable.album_cb_selec : R.drawable.album_cb);

                select(bean);

                holder.setOnClickListener(R.id.iv_check, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (((SelectActivity) myActivity).getList_select().size()
                                == ((SelectActivity) myActivity).getCount_pick() && !bean.isChecked()) {
                            showToast("最多只能选择" + ((SelectActivity) myActivity).getCount_pick() + "张图片");
                            return;
                        }

                        bean.setChecked(!bean.isChecked());
                        holder.setImageResource(R.id.iv_check, bean.isChecked() ? R.drawable.album_cb_selec : R.drawable.album_cb);
                        holder.setVisibility(R.id.view_shade, bean.isChecked());

                        select(bean);


                    }
                });

            }

            @Override
            public int getItemLayoutID(int viewType) {
                if (isHaveHeadView()) {

                    switch (viewType) {
                        case 0:
                            return R.layout.head_camera;
                        case 1:
                            return R.layout.item_album_item;

                    }
                }
                return R.layout.item_album_item;
            }

            @Override
            public int getItemViewType(int position) {
                if (isHaveHeadView()) {

                    switch (position) {
                        case 0:
                            return 0;

                    }
                }
                return 1;
            }

            @Override
            public void onItemClick(int position, ImageItem bean) {

                ((SelectActivity) myActivity).setList_preview(rvAdapter_item.getList_bean());

                ((SelectActivity) myActivity).setPosition_current(position - 1);
                addFragment(R.id.framelayout, new PreviewFragment());

            }
        };

        //?????????????????????????????????????????????????????????????????????????????????
        rvAdapter_folder = new RVAdapter<ImageFolder>(new ArrayList<ImageFolder>()) {
            @Override
            public void bindDataToView(MyViewHolder holder, int position, ImageFolder bean, boolean isSelected) {

                holder.setImage(R.id.iv_cover, bean.getCover().getPath(), 300, 300);

                holder.setText(R.id.tv_folder_name, bean.getName());
                holder.setText(R.id.tv_image_count, "共" + bean.getList().size() + "张");
                holder.setVisibility(R.id.iv_check, isSelected);
            }

            @Override
            public int getItemLayoutID(int viewType) {
                return R.layout.item_album_folder;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public void onItemClick(int position, final ImageFolder bean) {


                tv_all.setText(bean.getName());
                dialog_all.dismiss();

                rvAdapter_item.setHaveHeadView(position == 0 ? true : false);

                rvAdapter_item.clearAddAll(bean.getList());
            }
        };


    }

    private void select(ImageItem bean) {
        if (bean.isChecked()) {
            if (!((SelectActivity) myActivity).getList_select().contains(bean)) {
                ((SelectActivity) myActivity).getList_select().add(bean);
            }
            btn_complete.setEnabled(true);
            btn_complete.setTextColor(getResources().getColor(R.color.theme));
            btn_complete.setText("完成(" + ((SelectActivity) myActivity).getList_select().size() + "/" + ((SelectActivity) myActivity).getCount_pick() + ")");

            btn_preview.setEnabled(true);
            btn_preview.setTextColor(getResources().getColor(R.color.white));

            btn_preview.setText("预览(" + ((SelectActivity) myActivity).getList_select().size() + ")");
        } else {
            if (((SelectActivity) myActivity).getList_select().contains(bean)) {
                ((SelectActivity) myActivity).getList_select().remove(bean);
            }
            if (((SelectActivity) myActivity).getList_select().size() == 0) {
                btn_complete.setEnabled(false);
                btn_complete.setTextColor(getResources().getColor(R.color.text_tint));

                btn_complete.setText("完成");

                btn_preview.setEnabled(false);
                btn_preview.setText("预览");
                btn_preview.setTextColor(getResources().getColor(R.color.text_tint));

                return;
            }
            btn_complete.setText("完成(" + ((SelectActivity) myActivity).getList_select().size() + "/" + ((SelectActivity) myActivity).getCount_pick() + ")");

            btn_preview.setText("预览(" + ((SelectActivity) myActivity).getList_select().size() + ")");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_select, container, false);

        btn_complete = (Button) view.findViewById(R.id.btn_complete);
        btn_preview = (Button) view.findViewById(R.id.btn_preview);

        btn_complete.setOnClickListener(this);
        btn_preview.setOnClickListener(this);
        tv_all = (TextView) view.findViewById(R.id.tv_all);
        tv_all.setOnClickListener(this);

        if (((SelectActivity) myActivity).getList_select().size() == 0) {
            btn_complete.setEnabled(false);
            btn_complete.setTextColor(getResources().getColor(R.color.text_tint));

            btn_complete.setText("完成");

            btn_preview.setEnabled(false);
            btn_preview.setText("预览");
            btn_preview.setTextColor(getResources().getColor(R.color.text_tint));

        } else {
            btn_complete.setEnabled(true);
            btn_complete.setTextColor(getResources().getColor(R.color.theme));
            btn_complete.setText("完成(" + ((SelectActivity) myActivity).getList_select().size() + "/" + ((SelectActivity) myActivity).getCount_pick() + ")");

            btn_preview.setEnabled(true);
            btn_preview.setTextColor(getResources().getColor(R.color.white));

            btn_preview.setText("预览(" + ((SelectActivity) myActivity).getList_select().size() + ")");


        }

        ((GridRecyclerView) view.findViewById(R.id.grv)).setAdapter(rvAdapter_item, 4, RecyclerView.VERTICAL);
        ((VerticalRecyclerView) dialog_all.findViewById(R.id.vrv)).setAdapter(rvAdapter_folder);


        return returnView(view);

    }


    @Override
    public void initData() {
        loaderManager = myActivity.getSupportLoaderManager();
        myActivity.checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                "存储权限已被禁用", new BaseActivity.OnPermissionHaveListener() {
                    @Override
                    public void onPermissionHave() {
                        //加载所有
                        loaderManager.initLoader(0, null, new LoaderCallbacksAll());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_all) {
            dialog_all.show();

        } else if (i == R.id.btn_complete) {
            if(((SelectActivity) myActivity).getIntent().getBooleanExtra(AlbumConstant.KEY_INTENT_IS_TO_CROP,true)){
                addFragment(R.id.framelayout, new CropFragment());

            }else {


                EventBus.getDefault().post(new SelectNoCropEvent(((SelectActivity) myActivity).getList_select()));
                finishActivity();

            }



        } else if (i == R.id.btn_preview) {
            List<ImageItem> list_preview = new ArrayList<>();
            list_preview.addAll(((SelectActivity) myActivity).getList_select());
            ((SelectActivity) myActivity).setList_preview(list_preview);

            ((SelectActivity) myActivity).setPosition_current(0);
            addFragment(R.id.framelayout, new PreviewFragment());

        }
    }

    private class LoaderCallbacksAll implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = null;
            //扫描所有图片
            cursorLoader = new CursorLoader(getContext(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null,
                    IMAGE_PROJECTION[6] + " DESC");
            //扫描某个图片文件夹
//            if (id == LOADER_CATEGORY)
//                cursorLoader = new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'",
// null, IMAGE_PROJECTION[6] + " DESC");

            return cursorLoader;
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            rvAdapter_folder.clear();
            rvAdapter_item.clear();
            if (data != null) {
//                                    List<ImageItem> allImages = new ArrayList<>();   //所有图片的集合,不分文件夹
                while (data.moveToNext()) {
                    //查询数据
                    String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));

                    File file = new File(imagePath);
                    if (!file.exists() || file.length() <= 0) {
                        continue;
                    }

                    long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                    int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                    int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                    String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                    long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                    //封装实体
                    /*
                      private String name;       //图片的名字
    private String path;       //图片的路径
    private long size;         //图片的大小
    private int width;         //图片的宽度
    private int height;        //图片的高度
    private String mimeType;   //图片的类型
    private long addTime;      //图片的创建时间

    private boolean checked;
                     */
                    ImageItem imageItem = new ImageItem(imageName, imagePath, imageSize, imageWidth, imageHeight,
                            imageMimeType, imageAddTime, false);
                    list_item.add(imageItem);
                    //根据父路径分类存放图片
                    /*
                     private String name;  //当前文件夹的名字
    private String path;  //当前文件夹的路径
    private ImageItem cover;   //当前文件夹需要要显示的缩略图，默认为最近的一次图片
    private List<ImageItem> list;  //当前文件夹下所有图片的集合

                     */
//                                        File imageFile = new File(imagePath);
                    File file_parent = file.getParentFile();
                    ImageFolder imageFolder = new ImageFolder();
                    imageFolder.setName(file_parent.getName());
                    imageFolder.setPath(file_parent.getAbsolutePath());


                    if (!list_folder.contains(imageFolder)) {
                        List<ImageItem> list_ii = new ArrayList<>();
                        list_ii.add(imageItem);
                        imageFolder.setCover(imageItem);
                        imageFolder.setList(list_ii);
                        list_folder.add(imageFolder);
                    } else {
                        list_folder.get(list_folder.indexOf(imageFolder)).getList().add(imageItem);
                    }
                }
                //防止没有图片报异常
                if (data.getCount() > 0 && list_item.size() > 0) {
                    //构造所有图片的集合
                    ImageFolder allImagesFolder = new ImageFolder("所有图片", "/",
                            list_item.get(0), list_item);
                    list_folder.add(0, allImagesFolder);  //确保第一条是所有图片
                }
            }

            rvAdapter_item.clearAddAll(list_item);
            rvAdapter_folder.clearAddAll(list_folder);

        }
    }


}
