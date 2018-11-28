package com.example.cx.unlietaskdemo.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by hly on 2017/10/9.
 * email hly910206@gmail.com
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;

    /**
     * @param space 传入的值，其单位视为dp
     */
    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        outRect.left = mSpace;
//        outRect.right = mSpace;
//        outRect.bottom = mSpace;
        if (mSpace != 0) {
            outRect.bottom = mSpace;
        }
    }
}
