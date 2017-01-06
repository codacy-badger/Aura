package com.bleadingsmile.util.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Use this class to quick implement vertical recyclerView`s drag to swap touch behavior.
 *
 * see {@link ItemTouchHelper}
 * 
 * Created by larryhsiao on 2017/1/5.
 */
public abstract class DragToSwap extends ItemTouchHelper.Callback {

    public DragToSwap() {
        super();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        onSwap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        onSwapFinish();
    }

    protected abstract void onSwap(int from, int to);
    protected abstract void onSwapFinish();
}
