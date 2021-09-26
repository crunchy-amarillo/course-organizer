package com.iu.course_organizer.common;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    public RecyclerViewTouchListener(Context context, final RecyclerView recyclerView,
            final ClickListener clickListener
    ) {
        this.clickListener = clickListener;
        gestureDetector =
                new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent event) {
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent event) {
                        View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
                        if (child != null && clickListener != null) {
                            clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                        }
                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {

        View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(event)) {
            clickListener.onClick(child, recyclerView.getChildPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}