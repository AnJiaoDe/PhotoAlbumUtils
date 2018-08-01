package com.cy.cyphotoalbumutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cy.cyphotoalbumutils.bitmap.GlideUtils;

import java.util.List;

/**
 * Created by cy on 2018/2/25.
 */

public abstract class CyPagerAdapter<T> extends PagerAdapter {
    private List<T> list_bean;

    public CyPagerAdapter(List<T> list_bean) {
        this.list_bean = list_bean;
    }

    @Override
    public int getCount() {
        return list_bean.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // 是否有缓存

        // view 显示的View
        // object : instantiateItem 返回的标记
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view= LayoutInflater.from(container.getContext()).inflate(getItemLayoutID(), container, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position,list_bean.get(position));
            }
        });
        bindDataToView(new MyViewHolder(view),position,list_bean.get(position));
        // 创建加载view的时候的回调


        // 添加
        container.addView(view);

        // 返回值，返回的是页面的标记
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 销毁view的时候的回调

        // position = position % mImgList.size();

        // 给viewpager移除imageView
        // ImageView iv = mImgList.get(position);
        // mPager.removeView(iv);

        container.removeView((View) object);
    }
    //填充数据
    public abstract void bindDataToView(MyViewHolder holder, int position, T bean);
    /*
     取得ItemView的布局文件
     @return
    */
    public abstract int getItemLayoutID();

      /*
      ItemView的单击事件

      @param position
     */
      public List<T> getList_bean() {
          return list_bean;
      }
    public abstract void onItemClick(int position, T bean);
    public void clear() {
        list_bean.clear();
        notifyDataSetChanged();
    }

    public int addAll(List<T> beans) {
        list_bean.addAll(beans);

        notifyDataSetChanged();
        return beans.size();
    }
    public int clearAddAll(List<T> beans) {
        list_bean.clear();
        list_bean.addAll(beans);

        notifyDataSetChanged();
        return beans.size();
    }

    public static class MyViewHolder {
        private View itemView;
        private SparseArray<View> array_view;

        public MyViewHolder(View itemView) {
            this.itemView = itemView;

            array_view = new SparseArray<View>();

        }

        public View getItemView() {
            return itemView;
        }

        public <T extends View> T getView(int viewId) {

            View view = array_view.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                array_view.put(viewId, view);
            }
            return (T) view;
        }
        public void setImage(int iv_id, String url) {
            ImageView iv = getView(iv_id);

            GlideUtils.loadImageByGlide(itemView.getContext(), url, iv);
        }
        public void setImageBitmap(int v_id, Bitmap bitmap) {
            ImageView view = getView(v_id);
            view.setImageBitmap(bitmap);
        }
        public void setImageResource(int v_id, int resID) {
            ImageView view = getView(v_id);
            view.setImageResource(resID);
        }
        public void setImage(int iv_id, String url, int width, int height, int default_res) {
            ImageView iv = getView(iv_id);

            GlideUtils.loadImageByGlide(itemView.getContext(), url, iv, width, height);
        }

        public void setImage(int iv_id, String url, int default_res) {
            ImageView iv = getView(iv_id);

            GlideUtils.loadImageByGlide(itemView.getContext(), url, iv);
        }
        public void setImage(Context context,int iv_id, String url) {
            ImageView iv = getView(iv_id);

            GlideUtils.loadImageByGlide(context, url, iv);
        }

        public void setImage(int iv_id, String url, int width, int height) {
            ImageView iv = getView(iv_id);

            GlideUtils.loadImageByGlide(itemView.getContext(), url, iv, width, height);
        }

        public void setText(int tv_id, String text) {
            TextView tv = getView(tv_id);


            tv.setText(nullToString(text));
        }
        public String nullToString(Object object) {
            return object == null ? "" : object.toString();
        }
        public void setOnClickListener(int res_id, View.OnClickListener onClickListener) {
            getView(res_id).setOnClickListener(onClickListener);
        }
    }
}
