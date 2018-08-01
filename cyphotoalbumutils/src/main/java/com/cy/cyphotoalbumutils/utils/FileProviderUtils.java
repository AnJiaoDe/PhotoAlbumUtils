package com.cy.cyphotoalbumutils.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by lenovo on 2018/1/15.
 */

public class FileProviderUtils {

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }
    public static String getFileProviderAuthority(Context context) {
        return context.getPackageName()+".fileprovider";
    }
    public static Uri getUriForFile(Context context, File file){

        return FileProvider.getUriForFile(context,getFileProviderAuthority(context),file);

    }
}
