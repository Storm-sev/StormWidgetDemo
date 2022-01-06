package com.example.customnewdemo.common.holder;

import android.util.SparseArray;
import android.view.View;

/**
 * @author storm_z
 * @date @{DATE}
 * @email zq329051@outlook.com
 * @Describe listview  he Gridview
 */
public class LvViewHolder {


    public static <T extends View> T getView(View view, int id) {

        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if(viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);
        if(childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;

    }
}
