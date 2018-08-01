package com.cy.cyphotoalbumutils.bitmap;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cy.cyphotoalbumutils.R;

/**
 * Created by cy on 2016/10/9.
 */
public class GlideUtils {
    /*
    glide加载图片
     */
    public static void loadImageByGlide(Context context, String url, ImageView mImageView) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_pic)
                .into(mImageView);

    }

    /*
    glide加载图片并压缩
     */
    public static void loadImageByGlide(Context context, String url, ImageView mImageView, int width, int height) {

        Glide.with(context)
                .load(url)
                .override(width, height)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
    }
    /*
    glide加载图片并压缩
     */
    public static void loadImageUseDefaultRes(Context context, String url, ImageView mImageView, int width, int height) {

        Glide.with(context)
                .load(url)
                .override(width, height)
                .dontAnimate()
                .placeholder(R.drawable.default_pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
    }
    /*
    glide加载图片并压缩
     */
    public static void loadImageByGlide(Context context, String url, ImageView mImageView,int resID_default, int width, int height) {

        Glide.with(context)
                .load(url)
                .override(width, height)
                .dontAnimate()
                .placeholder(resID_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
    }


}
