package tw.moskastudio.fabtest.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import tw.moskastudio.fabtest.R;


/**
 * Created by alanx on 2015/4/18.
 */
public class FABContainerView extends FrameLayout {

    private static final long LONG_PRESSED_INTERVAL = 500;
    private static final int MOVE_THRESHOLD = 100;

    private static final int POSITION_UPPER_RIGHT = 1;
    private static final int POSITION_UPPER_LEFT = 2;
    private static final int POSITION_LOWER_LEFT = 3;
    private static final int POSITION_LOWER_RIGHT = 4;

    private Context mContext;
    private FrameLayout mFrameContainer;
    private SocialFABView mSocialFABView;
    private int mStartX = 0;
    private int mStartY = 0;
    private long mStartClickTime = 0;
    private long mPressInterval = 0;
    private int mCurrentPosition = POSITION_LOWER_RIGHT;
    int[][] mMarginArrays = new int[4][4];

    public FABContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        LayoutInflater.from(mContext).inflate(R.layout.view_fab_container, this);

        mFrameContainer = (FrameLayout) findViewById(R.id.container_frame);
        mSocialFABView = (SocialFABView) findViewById(R.id.social_fab_view);

        mSocialFABView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                if (view.getId() != R.id.social_fab_view) return false;

                Boolean mIsMoving = false;
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                int w = view.getMeasuredWidth();
                int h = view.getMeasuredHeight();
                int parentW = mFrameContainer.getMeasuredWidth();
                int parentH = mFrameContainer.getMeasuredHeight();

                int topMargin = y - (w * 2); //- (mFrameContainer.getMeasuredHeight() / 2)
                int leftMargin = x - (h);// - (mFrameContainer.getMeasuredWidth() / 2);

                if (topMargin < 0)
                    topMargin = 0;

                if (leftMargin < 0)
                    leftMargin = 0;

                if (topMargin > parentH - h)
                    topMargin = parentH - h;

                if (leftMargin > parentW - w)
                    leftMargin = parentW - w;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        Log.d("Touch", "ACTION_MOVE x:" + x + " y:" + y + " w:" + w + " h:" + h + "topMargin:" + topMargin + " leftMargin:" + leftMargin);
                        if (!mIsMoving) {
                            mPressInterval = System.currentTimeMillis() - mStartClickTime;
                            if (mPressInterval > LONG_PRESSED_INTERVAL || Math.abs(mStartX - x) > MOVE_THRESHOLD || Math.abs(mStartY - y) > MOVE_THRESHOLD) {
//                            if (mPressInterval > LONG_PRESSED_INTERVAL ) {
                                mIsMoving = true;
                                mSocialFABView.collapseButtons();
                            }
                        }

                        if (mIsMoving) {
                            // Move view
                            params.gravity = Gravity.TOP | Gravity.LEFT;
                            params.setMargins(leftMargin, topMargin, 0, 0);
                            view.setLayoutParams(params);
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d("Touch", "ACTION_UP x:" + x + " y:" + y + " w:" + w + " h:" + h + " topMargin:" + topMargin + " leftMargin:" + leftMargin);
                        if (!mIsMoving) {
                            mPressInterval = System.currentTimeMillis() - mStartClickTime;

                            if (mPressInterval > LONG_PRESSED_INTERVAL || Math.abs(mStartX - x) > MOVE_THRESHOLD || Math.abs(mStartY - y) > MOVE_THRESHOLD) {
//                            if (mPressInterval > LONG_PRESSED_INTERVAL ) {
                                mIsMoving = true;
                                mSocialFABView.collapseButtons();
                            }
                        }

                        if (!mIsMoving) {
                            mSocialFABView.toggleButtons();
                        } else {
                            params.gravity = Gravity.TOP | Gravity.LEFT;
                            params.setMargins(leftMargin, topMargin, 0, 0);
                            view.setLayoutParams(params);
                            setFABPosition(leftMargin, topMargin);
                        }

                        break;

                    case MotionEvent.ACTION_DOWN:
                        Log.d("Touch", "ACTION_DOWN x:" + x + " y:" + y + " w:" + w + " h:" + h + " topMargin:" + topMargin + " leftMargin:" + leftMargin);

                        mStartClickTime = System.currentTimeMillis();
                        mStartX = x;
                        mStartY = y;
                        break;
                }

                return true;
            }
        });

        mFrameContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                init();
                setFABPosition(POSITION_UPPER_RIGHT);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    mFrameContainer.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                } else {
                    mFrameContainer.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void init() {
        int w = mSocialFABView.getMeasuredWidth();
        int h = mSocialFABView.getMeasuredHeight();
        int parentW = mFrameContainer.getMeasuredWidth();
        int parentH = mFrameContainer.getMeasuredHeight();

        Log.d("init", w + " " + h + " " + parentW + " " + parentH);

        mMarginArrays[POSITION_UPPER_RIGHT - 1][0] = parentW - (int) (w * 1.2f);
        mMarginArrays[POSITION_UPPER_RIGHT - 1][1] = 0 + (int) (h * 0.8f);
        mMarginArrays[POSITION_UPPER_RIGHT - 1][2] = 0;
        mMarginArrays[POSITION_UPPER_RIGHT - 1][3] = 0;

        mMarginArrays[POSITION_UPPER_LEFT - 1][0] = 0 + (int) (w * 0.2f);
        mMarginArrays[POSITION_UPPER_LEFT - 1][1] = 0 + (int)(h * 0.8f);
        mMarginArrays[POSITION_UPPER_LEFT - 1][2] = 0;
        mMarginArrays[POSITION_UPPER_LEFT - 1][3] = 0;

        mMarginArrays[POSITION_LOWER_LEFT - 1][0] = 0 + (int) (w * 0.2f);
        mMarginArrays[POSITION_LOWER_LEFT - 1][1] = 0;
        mMarginArrays[POSITION_LOWER_LEFT - 1][2] = 0;
        mMarginArrays[POSITION_LOWER_LEFT - 1][3] = 0 + (int) (h * 0.8f);

        mMarginArrays[POSITION_LOWER_RIGHT - 1][0] = parentW - (int) (w * 1.2f);
        mMarginArrays[POSITION_LOWER_RIGHT - 1][1] = 0;
        mMarginArrays[POSITION_LOWER_RIGHT - 1][2] = 0;
        mMarginArrays[POSITION_LOWER_RIGHT - 1][3] = 0 + (int) (h * 0.8f);

        for (int x = 0; x < 4; x++) {
            Log.d("init", mMarginArrays[x][0] + " " + mMarginArrays[x][1] + " " + mMarginArrays[x][2] + " " + mMarginArrays[x][3]);
        }
    }

    private void setFABPosition(int position) {
        mCurrentPosition = position;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mSocialFABView.getLayoutParams();
        Log.d("setFABPosition", mCurrentPosition + ":" + mMarginArrays[mCurrentPosition - 1][0] +","+mMarginArrays[mCurrentPosition - 1][1] + "," + mMarginArrays[mCurrentPosition - 1][2] +","+mMarginArrays[mCurrentPosition - 1][3]);
        switch (position) {
            case POSITION_UPPER_RIGHT:
            case POSITION_UPPER_LEFT:
                mSocialFABView.displayAsUp();
                params.gravity = Gravity.TOP | Gravity.LEFT;
                params.setMargins(mMarginArrays[mCurrentPosition - 1][0], mMarginArrays[mCurrentPosition - 1][1], mMarginArrays[mCurrentPosition - 1][2], mMarginArrays[mCurrentPosition - 1][3]);
                break;
            case POSITION_LOWER_LEFT:
            case POSITION_LOWER_RIGHT:
                mSocialFABView.displayAsDown();
                params.gravity = Gravity.BOTTOM | Gravity.LEFT;
                params.setMargins(mMarginArrays[mCurrentPosition - 1][0], mMarginArrays[mCurrentPosition - 1][1], mMarginArrays[mCurrentPosition - 1][2], mMarginArrays[mCurrentPosition - 1][3]);
                break;
            default:
                break;
        }
        mSocialFABView.setLayoutParams(params);
    }

    private void setFABPosition(int x, int y) {
        int w = mSocialFABView.getMeasuredWidth();
        int h = mSocialFABView.getMeasuredHeight();
        int parentW = mFrameContainer.getMeasuredWidth();
        int parentH = mFrameContainer.getMeasuredHeight();
        int position;

        if (x < (parentW / 2) - w) {
            if (y < (parentH / 2) - h) {
                position = POSITION_UPPER_LEFT;
            } else {
                position = POSITION_LOWER_LEFT;
            }
        } else {
            if (y < (parentH / 2) - h) {
                position = POSITION_UPPER_RIGHT;
            } else {
                position = POSITION_LOWER_RIGHT;
            }
        }
        setFABPosition(position);
    }

    public SocialFABView getSocialFABView() {
        return mSocialFABView;
    }
}
