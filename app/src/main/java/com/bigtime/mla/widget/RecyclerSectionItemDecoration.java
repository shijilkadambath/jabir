package com.bigtime.mla.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigtime.mla.R;


/**
 * Created by paetztm on 2/6/2017.
 */

public class RecyclerSectionItemDecoration extends RecyclerView.ItemDecoration {

    private final int headerOffset;
    private final boolean sticky;
    private final SectionCallback sectionCallback;

    private View headerView;
    private TextView header;

    public RecyclerSectionItemDecoration(int headerHeight, boolean sticky, @NonNull SectionCallback sectionCallback) {
        headerOffset = headerHeight;
        this.sticky = sticky;
        this.sectionCallback = sectionCallback;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int pos = parent.getChildAdapterPosition(view);
        /*if (sectionCallback.isSection(pos)) {
            outRect.top =  headerOffset+headerOffset;;
        }*/

        if (sectionCallback.isSection(pos) && sectionCallback.isUnReadSection(pos)) {
            outRect.top = headerOffset + headerOffset;
        } else if (sectionCallback.isSection(pos) || sectionCallback.isUnReadSection(pos)) {
            outRect.top = headerOffset;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //Log.e("onDrawOver","onDrawOver||");

        if (headerView == null) {
            headerView = inflateHeaderView(parent);
            header = headerView.findViewById(R.id.textView);
            //fixLayoutSize(headerView, parent);
        }


        //View headerView = inflateHeaderView(parent);

        // CharSequence previousHeader = "";
        for (int i = 0; i < parent.getChildCount(); i++) {

            View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);

            //if (!previousHeader.equals(title) || sectionCallback.isSection(position)) {


            if (sectionCallback.isSection(position) || sectionCallback.isUnReadSection(position)) {


                int headerHeight = 0;
                if (sectionCallback.isSection(position) && sectionCallback.isUnReadSection(position)) {
                    headerHeight = headerOffset + headerOffset;
                    CharSequence title = sectionCallback.getSectionHeader(position);
                    header.setText(title);
                    //unreadHeader.setText((position + 1) + " UNREAD MESSAGES");

                    header.setVisibility(View.VISIBLE);
                    //unreadView.setVisibility(View.VISIBLE);

                } else if (sectionCallback.isSection(position)) {
                    headerHeight = headerOffset;
                    CharSequence title = sectionCallback.getSectionHeader(position);
                    header.setText(title);
                    //unreadHeader.setText("");

                    header.setVisibility(View.VISIBLE);
                    //unreadView.setVisibility(View.GONE);

                } else if (sectionCallback.isUnReadSection(position)) {
                    headerHeight = headerOffset;
                    header.setText("");
                    //unreadHeader.setText((position + 1) + " UNREAD MESSAGES");

                    header.setVisibility(View.GONE);
                    //unreadView.setVisibility(View.VISIBLE);
                }

                //headerHeight = headerOffset+headerOffset;

                fixLayoutSize(headerView, parent, headerHeight);

                drawHeader(c, child, headerView);
                //previousHeader = title;
                //float width = header.getPaint().measureText(title, 0, title.length());

            }
        }
    }

    private void drawHeader(Canvas c, View child, View headerView) {
        c.save();
        if (sticky) {
            c.translate(0, Math.max(0, child.getTop() - headerView.getHeight()));
        } else {
            c.translate(0, child.getTop() - headerView.getHeight());
        }
        headerView.draw(c);
        c.restore();
    }

    private View inflateHeaderView(RecyclerView parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_header, parent, false);
    }

    /**
     * Measures the header view to make sure its size is greater than 0 and will be drawn
     * https://yoda.entelect.co.za/view/9627/how-to-android-recyclerview-item-decorations
     */
    private void fixLayoutSize(View view, ViewGroup parent, int headerHeight) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

        int childWidth = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(),
                view.getLayoutParams().width);
        int childHeight = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(),
                view.getLayoutParams().height);

        view.measure(childWidth, childHeight);

        //view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight()+50);
        view.layout(0, 0, view.getMeasuredWidth(), headerHeight);

        //Log.e("height",headerOffset+"||"+view.getMeasuredHeight());
    }

    public interface SectionCallback {

        boolean isSection(int position);

        boolean isUnReadSection(int position);

        CharSequence getSectionHeader(int position);
    }
}