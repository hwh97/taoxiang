package cn.hwwwwh.taoxiang.CoustomView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by 97481 on 2017/1/31.
 */

public abstract class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener {

    private int mScrollThreshold;

    public abstract void onScrollUp();

    public abstract void onScrollDown();

    public abstract void onScrollTop();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        //设置移动3后才触发事件
        setScrollThreshold(3);
        boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
        if (isSignificantDelta) {
            if (dy > 0) {
                onScrollUp();
            } else {
                if(getScrolledDistance(recyclerView)>1000){
                    onScrollDown();
                }else{
                    onScrollTop();
                }
            }
        }
    }

    private int getScrolledDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        View firstVisibleItem = recyclerView.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemHeight = firstVisibleItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
        return (firstItemPosition + 1) * itemHeight - firstItemBottom;
    }

    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }
}