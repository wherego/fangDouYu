package com.cokus.fangdouyu.util;

import android.support.v7.widget.RecyclerView;

/**
 * Created by chenzhuo on 2017/4/26.
 */

public class RecyclerViewUtil {
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
}
