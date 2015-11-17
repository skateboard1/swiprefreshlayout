package com.haoxuan.pullrefresh;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by skateboard on 2015/6/6.
 */
public class MyListView extends ListView implements AbsListView.OnScrollListener {

    private int state;
    private static final int DONE=0;
    private static final int PULL_TO_REFRESH=1;
    private static final int RELEASE_TO_REFRESH=2;
    private static final int REFRESHING=3;
    private boolean isRefreshable=true;

    private View headerView;
    private ProgressBar progress;
    private TextView pull;
    private TextView release;
    private ImageView arrow;
    private LinearLayout layout;

    private int starty;
    private int height;

    public OnRefreshListener mOnRefreshListener;



    public MyListView(Context context) {
        super(context);
        state=DONE;
        isRefreshable=false;
        initView();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        state=DONE;
        isRefreshable=false;
        initView();
    }

    private void initView()
    {
        this.setOnScrollListener(this);
        setCacheColorHint(Color.YELLOW);
        headerView=LayoutInflater.from(getContext()).inflate(R.layout.headerlayout,null,false);
        progress=(ProgressBar)headerView.findViewById(R.id.progress);
        pull=(TextView)headerView.findViewById(R.id.pull);
        release=(TextView)headerView.findViewById(R.id.release);
        arrow=(ImageView)headerView.findViewById(R.id.arrow);
        layout=(LinearLayout)headerView.findViewById(R.id.header);
        measureView(headerView);
        height=headerView.getMeasuredHeight();
        headerView.setPadding(0, -height, 0, 0);
        headerView.invalidate();
        System.out.println("headerview"+layout.getMeasuredHeight());
        this.addHeaderView(headerView);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
             if(firstVisibleItem==0)
             {
                 isRefreshable=true;
             }
             else {
                 isRefreshable=false;
             }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isRefreshable) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    starty = (int) ev.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int tempy = (int) ev.getRawY();
                    if (state == DONE) {
                        if (tempy - starty > 0) {
                            state = PULL_TO_REFRESH;
                            changeHeaderViewByState();
                        }

                    }
                    if (state == PULL_TO_REFRESH) {
                        setSelection(0);
                        if(tempy-starty>height)
                        {
                            state=RELEASE_TO_REFRESH;
                            changeHeaderViewByState();
                        }
                        else if(tempy-starty<=0)
                        {
                           state=DONE;
                            changeHeaderViewByState();
                        }
                    }

                    if(state==RELEASE_TO_REFRESH)
                    {
                        setSelection(0);
                        if((tempy-starty)<height && (tempy-starty>0))
                        {
                            state=PULL_TO_REFRESH;
                            changeHeaderViewByState();
                        }
                        else if(tempy-starty<=0)
                        {
                            state=DONE;
                            changeHeaderViewByState();
                        }
                    }

                    if (state == PULL_TO_REFRESH) {
                        headerView.setPadding(0, -height + tempy - starty, 0, 0);
                    }

                    if (state == RELEASE_TO_REFRESH) {
                        headerView.setPadding(0, tempy-starty-height, 0, 0);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(state!=REFRESHING)
                    {
                        if(state==PULL_TO_REFRESH)
                        {
                            state=DONE;
                            changeHeaderViewByState();
                        }
                        else if(state==RELEASE_TO_REFRESH)
                        {
                            state=REFRESHING;
                            changeHeaderViewByState();
                            onRefresh();
                        }
                    }
                    break;


            }
          }
            return super.onTouchEvent(ev);
        }


    private void changeHeaderViewByState()
    {
         switch(state)
         {
             case PULL_TO_REFRESH:
                 headerView.setPadding(0, 0, 0, 0);
                 pull.setVisibility(View.VISIBLE);
                 progress.setVisibility(View.GONE);
                 release.setVisibility(View.GONE);
                 arrow.setVisibility(VISIBLE);
                 break;
             case RELEASE_TO_REFRESH:
                 pull.setVisibility(GONE);
                 progress.setVisibility(GONE);
                 release.setVisibility(VISIBLE);
                 arrow.setVisibility(GONE);
                 break;
             case DONE:
                 headerView.setPadding(0,-height,0,0);
                 break;
             case REFRESHING:
                 headerView.setPadding(0, 0, 0, 0);
                 pull.setVisibility(GONE);
                 progress.setVisibility(VISIBLE);
                 release.setVisibility(GONE);
                 arrow.setVisibility(GONE);
                 break;


         }

    }

    private void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0,
                params.width);
        int lpHeight = params.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }


    private void onRefresh()
    {
        //refresehComplete();
        if(mOnRefreshListener!=null)
            mOnRefreshListener.onRefresh();

    }

    public void refresehComplete()
    {
        state=DONE;
        changeHeaderViewByState();
    }

    public interface OnRefreshListener
    {
        void onRefresh();
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener)
    {
        this.mOnRefreshListener=refreshListener;
    }
}
