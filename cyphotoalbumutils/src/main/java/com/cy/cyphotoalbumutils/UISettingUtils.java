package com.cy.cyphotoalbumutils;

import android.content.Intent;

/**
 * Created by leifeng on 2018/7/26.
 */

public class UISettingUtils {

    public static final String KEY_INTENT_IV_BACK = "KEY_INTENT_SELECT_IV_BACK";// 返回箭头
    public static final String KEY_INTENT_THEME = "KEY_INTENT_SELECT_THEME";// 主题背景
    public static final String KEY_INTENT_BUTTON_BG = "KEY_INTENT_SELECT_BUTTON_BG";// 按钮背景
    public static final String KEY_INTENT_TV_COLOR_UNENABLE = "KEY_INTENT_SELECT_TV_COLOR_UNENABLE";// 按钮不可用的文字颜色
    public static final String KEY_INTENT_TV_COLOR_ENABLE = "KEY_INTENT_SELECT_TV_COLOR_ENABLE";// 按钮可用的文字颜色
    public static final String KEY_INTENT_TV_COLOR = "KEY_INTENT_SELECT_TV_COLOR";// 字体颜色
    public static final String KEY_INTENT_IV_SELECT = "KEY_INTENT_SELECT_IV_SELECT";// 选择文件夹的三角图标
    public static final String KEY_INTENT_CB_NORMAL = "KEY_INTENT_SELECT_CB_NORMAL";// checkbox未选中图片
    public static final String KEY_INTENT_CB_CHECKED = "KEY_INTENT_SELECT_CB_CHECKED";// checkbox选中图片
    public static final String KEY_INTENT_FOLDER_BG = "KEY_INTENT_FOLDER_BG";// 文件夹背景
    public static final String KEY_INTENT_IV_DUIGOU = "KEY_INTENT_IV_DUIGOU";// 文件夹选中对勾图片


    public static void set_iv_back(Intent intent ,int resID){
        intent.putExtra(KEY_INTENT_IV_BACK,resID);
    }

    public static void get_iv_back(Intent intent ){
        intent.getIntExtra(KEY_INTENT_IV_BACK, 100);
    }
    public static void set_theme(Intent intent ,int resID){
        intent.putExtra(KEY_INTENT_THEME,resID);
    }

    public static void get_theme(Intent intent ){
        intent.getIntExtra(KEY_INTENT_THEME, 100);
    }
    public static void set_button_bg(Intent intent ,int resID){
        intent.putExtra(KEY_INTENT_BUTTON_BG,resID);
    }

    public static void get_button_bg(Intent intent ){
        intent.getIntExtra(KEY_INTENT_BUTTON_BG, 100);
    }
    public static void set_tv_color_unEnable(Intent intent ,int resID){
        intent.putExtra(KEY_INTENT_TV_COLOR_UNENABLE,resID);
    }

    public static void get_tv_color_unEnable(Intent intent ){
        intent.getIntExtra(KEY_INTENT_TV_COLOR_UNENABLE, 100);
    }
    public static void set_color_unEnable(Intent intent ,int resID){
        intent.putExtra(KEY_INTENT_TV_COLOR_UNENABLE,resID);
    }

    public static void get_color_unEnable(Intent intent ){
        intent.getIntExtra(KEY_INTENT_TV_COLOR_UNENABLE, 100);
    }
}
