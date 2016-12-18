package com.bleadingsmile.util.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/** gesture identify, for now has itemClick, longPress */
public class SRecyclerViewTouchListener extends RecyclerView.SimpleOnItemTouchListener {
    private OnRecyclerItemClickListener m_listener        = null;
    private GestureDetector             m_gestureDetector = null;

    private View m_accessChild    = null;
    private int  m_accessPosition = -1;

    public interface OnRecyclerItemClickListener {
        void onRecyclerItemClick(View view, int position);

        void onRecyclerItemLongPress(View view, int position);
    }

    public SRecyclerViewTouchListener(
            Context context, OnRecyclerItemClickListener listener) {
        this.m_listener = listener;
        GestureDetector.SimpleOnGestureListener gestureListener =
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        m_listener.onRecyclerItemClick(m_accessChild, m_accessPosition);
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);
                        m_listener.onRecyclerItemLongPress(m_accessChild, m_accessPosition);
                    }
                };
        m_gestureDetector = new GestureDetector(context, gestureListener);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        m_accessChild = rv.findChildViewUnder(e.getX(), e.getY());
        m_accessPosition = rv.getChildLayoutPosition(m_accessChild);
        return null != m_accessChild && m_gestureDetector.onTouchEvent(e);
    }
}