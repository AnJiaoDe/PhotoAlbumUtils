package com.cy.cyphotoalbumutils.albumselect.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cy.cyphotoalbumutils.CyPagerAdapter;
import com.cy.cyphotoalbumutils.R;
import com.cy.cyphotoalbumutils.albumselect.model.AlbumConstant;
import com.cy.cyphotoalbumutils.albumselect.model.ImageItem;
import com.cy.cyphotoalbumutils.albumselect.model.PreviewSelectEvent;
import com.cy.cyphotoalbumutils.albumselect.model.PreviewSelectSelectedEvent;
import com.cy.cyphotoalbumutils.albumselect.model.SelectNoCropEvent;
import com.cy.cyphotoalbumutils.base.BaseFragment;
import com.cy.cyphotoalbumutils.recyclerview.HorizontalRecyclerView;
import com.cy.cyphotoalbumutils.recyclerview.RVAdapter;
import com.cy.cyphotoalbumutils.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PreviewFragment extends BaseFragment {
    private View view;
    private CyPagerAdapter<ImageItem> cyPagerAdapter;

    private RVAdapter<ImageItem> rvAdapter;
    private ViewPager viewPager;
    private HorizontalRecyclerView horizontalRecyclerView;
    private Button btn_complete;
    private Button btn_selected;

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().post(new PreviewSelectEvent());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe
    public void onEventMainThread(PreviewSelectSelectedEvent event) {

        rvAdapter.notifyDataSetChanged();
        if (((SelectActivity) myActivity).getList_select().size() == 0) {
            btn_complete.setEnabled(false);
            btn_complete.setTextColor(getResources().getColor(R.color.text_tint));

            btn_complete.setText("完成");

            //???????????????????????????????????????
            btn_selected.setEnabled(false);
            btn_selected.setTextColor(getResources().getColor(R.color.text_tint));

            btn_selected.setText("已选择");
        } else {
            btn_complete.setEnabled(true);
            btn_complete.setTextColor(getResources().getColor(R.color.theme));
            btn_complete.setText("完成(" + ((SelectActivity) myActivity).getList_select().size() + "/" + ((SelectActivity) myActivity).getCount_pick() + ")");
            //???????????????????????????????????????????
            btn_selected.setEnabled(true);
            btn_selected.setTextColor(getResources().getColor(R.color.white));
            btn_selected.setText("已选择(" + ((SelectActivity) myActivity).getList_select().size() + "/" + ((SelectActivity) myActivity).getCount_pick() + ")");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        cyPagerAdapter = new CyPagerAdapter<ImageItem>(((SelectActivity) myActivity).getList_preview()) {
            @Override
            public void bindDataToView(final MyViewHolder holder, int position, final ImageItem bean) {

                holder.setImage(R.id.pv, bean.getPath(), ScreenUtils.getScreenWidth(getContext()),
                        ScreenUtils.getScreenHeight(getContext()));


            }


            @Override
            public int getItemLayoutID() {
                return R.layout.item_picture_preview;
            }

            @Override
            public void onItemClick(int position, ImageItem bean) {

            }
        };

        //???????????????????????????????????
        rvAdapter = new RVAdapter<ImageItem>(((SelectActivity) myActivity).getList_preview()) {
            @Override
            public void bindDataToView(final MyViewHolder holder, final int position, final ImageItem bean, boolean isSelected) {
                holder.setImage(R.id.iv, bean.getPath(), 100, 100);
                if (position == ((SelectActivity) myActivity).getPosition_current()) {
                    holder.setVisibility(R.id.view_stroke, true);
                } else {
                    holder.setVisibility(R.id.view_stroke, false);

                }
                holder.setImageResource(R.id.iv_check, bean.isChecked() ? R.drawable.album_cb_selec : R.drawable.album_cb);

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

                        select(bean);

                        rvAdapter.notifyItemChanged(position);

                    }
                });


            }

            @Override
            public int getItemLayoutID(int viewType) {
                return R.layout.item_preview_bottom;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public void onItemClick(int position, ImageItem bean) {


                viewPager.setCurrentItem(position);

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
            btn_complete.setText("完成(" + ((SelectActivity) myActivity).getList_select().size() + "/" +
                    ((SelectActivity) myActivity).getCount_pick() + ")");

            //???????????????????????????????????????????????????
            btn_selected.setEnabled(true);
            btn_selected.setTextColor(getResources().getColor(R.color.white));
            btn_selected.setText("已选择(" + ((SelectActivity) myActivity).getList_select().size() + "/" +
                    ((SelectActivity) myActivity).getCount_pick() + ")");

        } else {
            if (((SelectActivity) myActivity).getList_select().contains(bean)) {
                ((SelectActivity) myActivity).getList_select().remove(bean);


            }
            if (((SelectActivity) myActivity).getList_select().size() == 0) {
                btn_complete.setEnabled(false);
                btn_complete.setTextColor(getResources().getColor(R.color.text_tint));

                btn_complete.setText("完成");
                //?????????????????????????????????????
                btn_selected.setEnabled(false);
                btn_selected.setTextColor(getResources().getColor(R.color.text_tint));

                btn_selected.setText("已选择");

                return;
            }
            btn_complete.setText("完成(" + ((SelectActivity) myActivity).getList_select().size() + "/" + ((SelectActivity) myActivity).getCount_pick() + ")");
            btn_selected.setText("已选择(" + ((SelectActivity) myActivity).getList_select().size() + "/" + ((SelectActivity) myActivity).getCount_pick() + ")");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preview, container, false);

        btn_complete = (Button) view.findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(this);
        //???????????????????????????????????????????????????????????????????????
        btn_selected = (Button) view.findViewById(R.id.btn_selected);
        btn_selected.setOnClickListener(this);
        if (((SelectActivity) myActivity).getList_select().size() == 0) {
            btn_complete.setEnabled(false);
            btn_complete.setTextColor(getResources().getColor(R.color.text_tint));

            btn_complete.setText("完成");

            //???????????????????????????????????????
            btn_selected.setEnabled(false);
            btn_selected.setTextColor(getResources().getColor(R.color.text_tint));

            btn_selected.setText("已选择");
        } else {
            btn_complete.setEnabled(true);
            btn_complete.setTextColor(getResources().getColor(R.color.theme));
            btn_complete.setText("完成(" + ((SelectActivity) myActivity).getList_select().size() + "/" + ((SelectActivity) myActivity).getCount_pick() + ")");
            //???????????????????????????????????????????
            btn_selected.setEnabled(true);
            btn_selected.setTextColor(getResources().getColor(R.color.white));
            btn_selected.setText("已选择(" + ((SelectActivity) myActivity).getList_select().size() + "/" + ((SelectActivity) myActivity).getCount_pick() + ")");
        }

        viewPager = ((ViewPager) view.findViewById(R.id.viewpager));


        horizontalRecyclerView = ((HorizontalRecyclerView) view.findViewById(R.id.hrv));
        horizontalRecyclerView.setAdapter(rvAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                ((SelectActivity) myActivity).setPosition_current(position);
                horizontalRecyclerView.scrollToPosition(position);
                rvAdapter.notifyDataSetChanged();


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(cyPagerAdapter);

        viewPager.setCurrentItem(((SelectActivity) myActivity).getPosition_current());

        return returnView(view);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_complete) {
            if(((SelectActivity) myActivity).getIntent().getBooleanExtra(AlbumConstant.KEY_INTENT_IS_TO_CROP,true)){
                addFragment(R.id.framelayout, new CropFragment());

            }else {


                EventBus.getDefault().post(new SelectNoCropEvent(((SelectActivity) myActivity).getList_select()));
                finishActivity();

            }

            return;
        }
        if (i == R.id.btn_selected) {
            addFragment(R.id.framelayout, new PreviewSelectedFragment());


            return;
        }
    }
}
