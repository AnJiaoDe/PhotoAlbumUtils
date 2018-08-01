package com.cy.cyphotoalbumutils.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cy.cyphotoalbumutils.R;
import com.cy.translucentparent.StatusNavUtils;


/**
 * Created by lenovo on 2017/4/25.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private String toast_permission;
    private OnPermissionHaveListener onPermissionHaveListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        StatusNavUtils.setStatusBarColor(this,0x00000000);
    }



    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ImageView imageView = (ImageView) findViewById(R.id.iv_back);
        if (imageView != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }

    /*
            Acitivity跳转
             */
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int string_id) {
        Toast.makeText(this, getResources().getString(string_id), Toast.LENGTH_SHORT).show();
    }


    public void startAppcompatActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }







    public void initData() {
    }
    /*

     */

    public void replaceFragment(int framelayout_id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(framelayout_id, fragment).commitAllowingStateLoss();
    }


    public Fragment createFragment(int position) {
        return null;
    }

    /*
          6.0权限检查
           */
    public void checkPermission(String[] permission, String toast_permission, OnPermissionHaveListener onPermissionHaveListener) {
        ActivityCompat.requestPermissions(this, permission, 1);
        this.toast_permission = toast_permission;

        this.onPermissionHaveListener = onPermissionHaveListener;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                showToast(toast_permission);
                return;
            }
        }
        if (onPermissionHaveListener != null) {
            onPermissionHaveListener.onPermissionHave();
        }


    }

    public interface OnPermissionHaveListener {
        public void onPermissionHave();
    }
}
