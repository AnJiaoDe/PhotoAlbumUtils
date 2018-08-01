package com.cy.cyphotoalbumutils.albumselect.view;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cy.cyphotoalbumutils.R;
import com.cy.cyphotoalbumutils.albumselect.model.AlbumConstant;
import com.cy.cyphotoalbumutils.albumselect.model.CropEvent;
import com.cy.cyphotoalbumutils.base.BaseFragment;
import com.cy.cyphotoalbumutils.bitmap.BitmapUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CropFragment extends BaseFragment {
    private View view;
    private CropImageView cropImageView;

    private Button btn_complete;

    private List<File> list_file_crop = new ArrayList<>();
    private int position_current = 0;

    private int width = 100, height = 100, width_focus = 100, height_focus = 100;
    private boolean isRectangle = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        width = ((SelectActivity) myActivity).getIntent().getIntExtra(AlbumConstant.KEY_INTENT_SELECT_WIDTH, 100);
        height = ((SelectActivity) myActivity).getIntent().getIntExtra(AlbumConstant.KEY_INTENT_SELECT_HEIGHT, 100);
        width_focus = ((SelectActivity) myActivity).getIntent().getIntExtra(AlbumConstant.KEY_INTENT_CROP_FOCUS_WIDTH, 100);
        height_focus = ((SelectActivity) myActivity).getIntent().getIntExtra(AlbumConstant.KEY_INTENT_CROP_FOCUS_HEIGHT, 100);
        isRectangle = ((SelectActivity) myActivity).getIntent().getBooleanExtra(AlbumConstant.KEY_INTENT_SELECT_IS_RECTANGLE, true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_crop, container, false);
        btn_complete = (Button) view.findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(this);

        cropImageView = (CropImageView) view.findViewById(R.id.civ);


        cropImageView.setFocusStyle(isRectangle ? CropImageView.Style.RECTANGLE : CropImageView.Style.CIRCLE);
        cropImageView.setFocusWidth(width_focus);
        cropImageView.setFocusHeight(height_focus);

        cropImageView.setImageBitmap(cropImageView.rotate(
                BitmapUtils.decodeBitmapFromFilePath(((SelectActivity) myActivity).getList_select().
                        get(position_current).getPath(), width, height), BitmapUtils.getBitmapDegree
                        (((SelectActivity) myActivity).getList_select().
                                get(position_current).getPath())));

        cropImageView.setOnBitmapSaveCompleteListener(new CropImageView.OnBitmapSaveCompleteListener() {
            @Override
            public void onBitmapSaveSuccess(File file) {
                ((SelectActivity) myActivity).getCyProgressDialog().dismiss();

                list_file_crop.add(file);

                position_current++;

                if (position_current >= ((SelectActivity) myActivity).getList_select().size()) {

                    EventBus.getDefault().post(new CropEvent(list_file_crop));
                    finishActivity();

                } else {
                    cropImageView.setImageBitmap(cropImageView.rotate(
                            BitmapUtils.decodeBitmapFromFilePath(((SelectActivity) myActivity).getList_select().
                                    get(position_current).getPath(), width, height), BitmapUtils.getBitmapDegree
                                    (((SelectActivity) myActivity).getList_select().
                                            get(position_current).getPath())));
                }
            }

            @Override
            public void onBitmapSaveError(File file) {

            }
        });


        return returnView(view);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_complete) {
            ((SelectActivity) myActivity).getCyProgressDialog().show();


            cropImageView.saveBitmapToFile(new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).
                    getAbsolutePath() + "/cut"), width, height, isRectangle ? true : false);


        }

    }
}
