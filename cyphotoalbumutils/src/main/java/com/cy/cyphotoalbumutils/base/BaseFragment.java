package com.cy.cyphotoalbumutils.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cy.cyphotoalbumutils.R;


/**
 * Created by lenovo on 2017/6/22.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {





    public View returnView(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_back);
        if (imageView != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupFragment();
                }
            });
        }
        initData();
        return view;
    }


    public BaseActivity myActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = (BaseActivity) getActivity();



    }




    //??????????????????????????????????????????????????????????????????????
    public String nullToString(Object object) {
        return object == null ? "" : object.toString();
    }


    //??????????????????????????????????????????????????????????????????????

    public abstract void initData();


    public void startAppcompatActivity(Class<?> cls) {
        startActivity(new Intent(getContext(), cls));
    }


    public void popupFragment() {

        if (myActivity.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finishActivity();
            return;
        }
        myActivity.getSupportFragmentManager().popBackStack();

    }


    public void replaceFragment(int framelayout_id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = myActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        fragmentTransaction.replace(framelayout_id, fragment).addToBackStack(null).commit();

    }
    public void addFragment(int framelayout_id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = myActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        fragmentTransaction.add(framelayout_id, fragment).addToBackStack(null).commit();

    }

    public void replaceFragmentNotToBack(int framelayout_id, Fragment fragment) {
        FragmentTransaction fragmentTransaction = myActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        fragmentTransaction.replace(framelayout_id, fragment).commit();

    }

    public void finishActivity() {
        myActivity.finish();

    }

    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int string_id) {
        Toast.makeText(getContext(), getContext().getResources().getString(string_id), Toast.LENGTH_SHORT).show();
    }


    public Fragment createFragment(int position) {
        return null;
    }

}
