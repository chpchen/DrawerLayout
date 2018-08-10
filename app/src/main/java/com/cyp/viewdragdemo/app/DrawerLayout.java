package com.cyp.viewdragdemo.app;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 6/6 0006.
 * <p/>
 */
public class DrawerLayout extends ViewGroup {
    private static final String TAG = "DrawerLayout";
    private static float OFF_SET = 0.6f;
    // dp
    private static final int MIN_DRAWER_MARGIN = 50;
    /**
     * Minimum velocity that will be detected as a fling
     * dips per second
     */
    private static final int MIN_FLING_VELOCITY = 400;

    /**
     * drawer离父容器左边的最小外边距
     */
    private int mMinDrawerMargin;

    private View mRightMenuView;
    private View mContentView;

    private ViewDragHelper mDragHelper;
    private Context mContext;
    private boolean mShown;
    private OnCloseListener mOnCloseListener;

    /**
     * drawer显示出来的占自身的百分比
     */
    private float mRightMenuOnScreen;


    public DrawerLayout(Context context) {
        this(context, null);
        mContext = context;
    }

    public DrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public DrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        float density = getResources().getDisplayMetrics().density;
        //1200
        float minVel = MIN_FLING_VELOCITY * density;
        mMinDrawerMargin = (int) (MIN_DRAWER_MARGIN * density + 0.5f);

        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //捕获该view
                // return child == mDragView || child == mAutoBackView;
                return child == mRightMenuView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.i(TAG, "clampViewPositionHorizontal: left:" + left + "|getWidth:" + getWidth() + "child.getMeasuredWidth():" + child.getMeasuredWidth());
                int newLeft = 0;

                if (left < getWidth() - child.getMeasuredWidth()) {
                    newLeft = getWidth() - child.getMeasuredWidth();
                } else {
                    newLeft = left;
                }
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int topBound = getPaddingTop();
                int bottomBound = getHeight() - child.getHeight() - topBound;
                int newTop = Math.min(Math.max(top, topBound), bottomBound);
                return newTop;
            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                int childWidth = releasedChild.getWidth();
                //0~1f
                float offset = (childWidth - releasedChild.getLeft()) * 1.0f / childWidth;
                Log.i(TAG, "onViewReleased: xvel:" + xvel + "|yvel:" + yvel + "|offset:" + offset);

                if (offset > OFF_SET) {
                    mShown = true;
                } else {
                    mShown = false;
                    if (mOnCloseListener != null) {
                        mOnCloseListener.onClose();
                    }
                }
                //计算重置位置(这个地方控制回弹的)
                mDragHelper.settleCapturedViewAt(offset > OFF_SET ? getWidth() - childWidth : getWidth(), releasedChild.getTop());
                invalidate();
            }

            //在边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                // mDragHelper.captureChildView(mEdgeTrackerView, pointerId);
//                mDragHelper.captureChildView(mRightMenuView, pointerId);

            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                int childWidth = changedView.getWidth();
//                float offset = (float) (childWidth + left) / childWidth;
                float offset = (float) (getWidth() - left) / childWidth;
                mRightMenuOnScreen = offset;
                Log.i(TAG, "onViewPositionChanged: mRightMenuOnScreen:" + mRightMenuOnScreen);
                changedView.setVisibility(offset == 0 ? View.INVISIBLE : View.VISIBLE);
                invalidate();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mRightMenuView == child ? child.getWidth() : 0;
            }

        });

        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_RIGHT);
        mDragHelper.setMinVelocity(minVel);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure: 11111 ");
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);

        View rightMenuView = getChildAt(1);
        MarginLayoutParams lp = (MarginLayoutParams) rightMenuView.getLayoutParams();

        final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                mMinDrawerMargin + lp.leftMargin + lp.rightMargin,
                lp.width);
        final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                lp.topMargin + lp.bottomMargin,
                lp.height);
        rightMenuView.measure(drawerWidthSpec, drawerHeightSpec);


        View contentView = getChildAt(0);
        lp = (MarginLayoutParams) contentView.getLayoutParams();
        final int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        contentView.measure(contentWidthSpec, contentHeightSpec);

        mRightMenuView = rightMenuView;
        mContentView = contentView;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View menuView = mRightMenuView;
        View contentView = mContentView;

        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();
        contentView.layout(lp.leftMargin, lp.topMargin,
                lp.leftMargin + contentView.getMeasuredWidth(),
                lp.topMargin + contentView.getMeasuredHeight());

        MarginLayoutParams lpRightView = (MarginLayoutParams) menuView.getLayoutParams();

        final int menuWidth = menuView.getMeasuredWidth();
        int childLeft = getWidth() - (int) (menuWidth * mRightMenuOnScreen);

        Log.i(TAG, "onLayout: childLeft:" + childLeft + "|menuWidth:" + menuWidth + "|mRightMenuOnScreen:" + mRightMenuOnScreen + "|getWidth():" + getWidth() + "changed:" + changed);
        menuView.layout(childLeft, lpRightView.topMargin, childLeft + menuWidth, lpRightView.topMargin + menuView.getMeasuredHeight());
    }

    public void closeDrawer() {
        View menuView = mRightMenuView;
        mRightMenuOnScreen = 0.f;
        mShown = false;
        if (mOnCloseListener != null) {
            mOnCloseListener.onClose();
        }
        mDragHelper.smoothSlideViewTo(menuView, getWidth(), menuView.getTop());
        invalidate();
    }

    public void openDrawer() {
        View menuView = mRightMenuView;
        mRightMenuOnScreen = 1.0f;
        mShown = true;
        mDragHelper.smoothSlideViewTo(menuView, getWidth() - menuView.getWidth(), 0);
        invalidate();
    }


    public void DrawerInvalidate() {
        invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mRightMenuView = getChildAt(1);

    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    public boolean isShown() {
        return mShown;
    }

    public void setShown(boolean shown) {
        mShown = shown;
    }


    public interface OnCloseListener {
        void onClose();
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }
}
