package com.example.customnewdemo.common.holder;

import android.content.Context;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author storm_z
 * @date @{DATE}
 * @email zq329051@outlook.com
 * @Describe
 */
public class RvViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    private SparseArray<View> mViews;

    public RvViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mItemView = itemView;
        mViews = new SparseArray<View>();
    }

    public  <T extends View> T getView(@IdRes int viewID) {

        View childView = mViews.get(viewID);

        if (childView == null) {
            childView = mItemView.findViewById(viewID);
            mViews.put(viewID, childView);
        }

        return (T) childView;
    }

    public View itemView(){
        return mItemView;
    }

    public void setText(@IdRes int viewId, String content) {
        TextView view = getView(viewId);
        view.setText(content);
    }

    public void setText(TextView textView, String content) {

        textView.setText(content);
    }

    public static RvViewHolder create(View view) {
        return new RvViewHolder(view);
    }

    public static RvViewHolder create(Context context, @LayoutRes int layoutId, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return RvViewHolder.create(view);
    }


    /**
     * 设置bg
     *
     * @param rl_item
     */
    public void setBg(Context context ,int rl_item,  int colorId) {
        View view = getView(rl_item);
        view.setBackgroundColor(ContextCompat.getColor(context,colorId));
    }
}
