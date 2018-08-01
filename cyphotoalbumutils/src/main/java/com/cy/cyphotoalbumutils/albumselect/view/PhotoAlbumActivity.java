package com.cy.cyphotoalbumutils.albumselect.view;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.cy.cyphotoalbumutils.base.BaseActivity;
import com.cy.cyphotoalbumutils.utils.FileProviderUtils;
import com.cy.dialog.BaseDialog;

import java.io.File;
import java.io.IOException;

public abstract class PhotoAlbumActivity extends BaseActivity {
    private File filePhoto;
    private BaseDialog dialog_progress;
    private final int INTENT_PHOTO = 10;//相机intent请求码
    private final int INTENT_ALBUM = 11;//相册intent请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

     /*
    相册选择后获取的原图文件
     */

    private File activityResultOfAlbum(Intent data) {

        Uri uri = data.getData();

        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return new File(result);
    }

    /*
           创建调用相机的Intent,适配7.0
          */
    public void createIntentOfPhoto(String name_photo, BaseDialog dialog_progress) {

        this.dialog_progress = dialog_progress;
        filePhoto = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()
                + "/photo" + name_photo + ".jpg");
        if (!filePhoto.exists()) {
            try {
                filePhoto.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // 使用Intent启动系统自带的拍照程序
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 这里不能直接放入path，要转换成Uri对象
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProviderUtils.getUriForFile(this, filePhoto));

        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePhoto));

        }
        startActivityForResult(intent, INTENT_PHOTO);
    }


    /*
    创建相册选择的Intent
     */
    public void createIntentOfAlbum(String name_cut, BaseDialog dialog_progress) {
        this.dialog_progress = dialog_progress;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, INTENT_ALBUM);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            File file = null;

            switch (requestCode) {
                //相册选择返回值处理
                case INTENT_PHOTO:
                    //拍照返回值处理
                    file = filePhoto;
                    Log.e("file相机", "-----------------------------------???" + file.length() / 1024 + "kb");


                    onPhotoFinished(file);
                    break;
                //相册选择返回值处理
                case INTENT_ALBUM:
                    file = activityResultOfAlbum(data);
                    Log.e("file相册", "-----------------------------------???" + file.length() / 1024 + "kb");

                    onAlbumFinished(file);
                    break;
//                case INTENT_CUT:
//                    file = fileCut;
//                    Log.e("file裁剪", "-----------------------------------???" + file.length() / 1024 + "kb");
//
//                    //大于100K压缩
//                    if (file != null && file.length() / 1024 > 100) {
//
//                        if (dialog_progress != null) {
//                            dialog_progress.show();
//                            BitmapUtils.compressFile(file, 500, 500, new BitmapUtils.OnBitmapListener() {
//                                @Override
//                                public void onCompressFileFinish(File file) {
//                                    super.onCompressFileFinish(file);
//                                    dialog_progress.dismiss();
//
//                                    Log.e("file压缩", "-----------------------------------???" + file.length() / 1024 + "kb");
//
//                                    onCompressFinish(file);
//
//
//                                }
//                            });
//                        }
//
//
//                    } else {
//                        onCompressFinish(file);
//
//                    }
//                    break;
            }
        }
    }

    public abstract void onPhotoFinished(File file);
    public abstract void onAlbumFinished(File file);
    public abstract void onCompressFinished(File file);


}
