package tw.moskastudio.fabtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tw.moskastudio.fabtest.R;

public class SocialFABView extends FrameLayout {

    public static final int TYPE_HOME_PAGE = 1;
    public static final int TYPE_DETAIL_PAGE = 2;

    private static final int HOME_PADDING_IN_DP = 6;
    private static final int DETAIL_PADDING_IN_DP = 9;
    private static final int HOME_TEXTSIZE_IN_SP = 14;
    private static final int DETAIL_TEXTSIZE_IN_SP = 17;

    private static final int POSITION_UP = 1;
    private static final int POSITION_DOWN = 2;

    private Context mContext;

    private LinearLayout fabLl;
    private RelativeLayout socialBarRl;

    private FrameLayout socialFl;
    private RelativeLayout.LayoutParams socialFlLayoutParams;

    private RelativeLayout buttonCollectionRl;
    private RelativeLayout.LayoutParams buttonCollectionRlParams;

    private FrameLayout likeFl;
    private FrameLayout hateFl;
    private FrameLayout commentFl;

    private ImageView imgSocialLike;
    private ImageView imgSocialHate;
    private TextView textSocialLike;
    private TextView textSocialHate;
    private TextView textSocialComment;

    private int viewPosition = POSITION_UP;

    private boolean isExpanded = false;
    private boolean isLiked;
    private boolean isHated;

    private int numberOfLikes;
    private int numberOfHates;
    private int numberOfComments;

    public SocialFABView(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0, 0);
    }

    public SocialFABView(Context context, AttributeSet attrs,
                         int numberOfLikes, int numberOfHates, int numberOfComments) {
        super(context, attrs);
        mContext = context;
        this.numberOfLikes = numberOfLikes;
        this.numberOfHates = numberOfHates;
        this.numberOfComments = numberOfComments;

        LayoutInflater.from(mContext).inflate(R.layout.view_social_fab, this);

        fabLl = (LinearLayout) findViewById(R.id.fabLl);
        socialBarRl = (RelativeLayout) findViewById(R.id.socialBarRl);

        socialFl = (FrameLayout) findViewById(R.id.socialFl);
        socialFlLayoutParams = (RelativeLayout.LayoutParams)(socialFl.getLayoutParams());

        buttonCollectionRl = (RelativeLayout) findViewById(R.id.buttonCollectionRl);
        buttonCollectionRlParams = (RelativeLayout.LayoutParams)(buttonCollectionRl.getLayoutParams());

        likeFl = (FrameLayout) findViewById(R.id.likeFl);
        hateFl = (FrameLayout) findViewById(R.id.hateFl);
        commentFl = (FrameLayout) findViewById(R.id.commentFl);

        imgSocialLike = (ImageView) findViewById(R.id.imgSocialLike);
        imgSocialHate = (ImageView) findViewById(R.id.imgSocialHate);

        textSocialLike = (TextView) findViewById(R.id.textSocialLike);
        textSocialHate = (TextView) findViewById(R.id.textSocialHate);
        textSocialComment = (TextView) findViewById(R.id.textSocialComment);

        setSocialBarUIStatus();
        setSocialBarType(TYPE_HOME_PAGE);
    }

    public void setSocialBarType(int type) {
        float paddingInPx = 0;
        float textSizeInSp = 0;
        int drawbleId;

        switch (type) {
            case TYPE_DETAIL_PAGE:
                paddingInPx = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, DETAIL_PADDING_IN_DP,
                        getResources().getDisplayMetrics());
                textSizeInSp = DETAIL_TEXTSIZE_IN_SP;
//			drawbleId = R.drawable.selector_social_bar;
                break;

            default:
                paddingInPx = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, HOME_PADDING_IN_DP,
                        getResources().getDisplayMetrics());
                textSizeInSp = HOME_TEXTSIZE_IN_SP;
//			drawbleId = R.color.social_bar_background;
                break;
        }

//		// set drawable
//		likeFl.setBackgroundResource(drawbleId);
//		hateFl.setBackgroundResource(drawbleId);
//		commentFl.setBackgroundResource(drawbleId);

        // set paddings
        likeFl.setPadding((int) paddingInPx, (int) paddingInPx, 0,
                (int) paddingInPx);
        hateFl.setPadding((int) paddingInPx, (int) paddingInPx, 0,
                (int) paddingInPx);
        commentFl.setPadding((int) paddingInPx, (int) paddingInPx, 0,
                (int) paddingInPx);

        // set text size
        textSocialLike.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp);
        textSocialHate.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp);
        textSocialComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp);

        invalidate();
    }

    private void updateSocialButtonUI() {

    }

    private void setSocialBarUIStatus() {
        imgSocialLike.setImageResource(isLiked ? R.drawable.btn_like_h
                : R.drawable.btn_like);
        imgSocialHate.setImageResource(isHated ? R.drawable.btn_unlike_h
                : R.drawable.btn_unlike);
        textSocialLike.setText(String.valueOf(numberOfLikes));
        textSocialHate.setText(String.valueOf(numberOfHates));
        textSocialComment.setText(String.valueOf(numberOfComments));

        if (isExpanded) {
            buttonCollectionRl.setVisibility(View.VISIBLE);

            if (viewPosition == POSITION_UP) {
                socialBarRl.removeView(socialFl);
                socialFlLayoutParams.addRule(RelativeLayout.BELOW, 0);
                socialBarRl.addView(socialFl, socialFlLayoutParams);

                socialBarRl.removeView(buttonCollectionRl);
                buttonCollectionRlParams.addRule(RelativeLayout.BELOW, socialFl.getId());
                socialBarRl.addView(buttonCollectionRl, buttonCollectionRlParams);
            } else {
                socialBarRl.removeView(buttonCollectionRl);
                buttonCollectionRlParams.addRule(RelativeLayout.BELOW, 0);
                socialBarRl.addView(buttonCollectionRl, buttonCollectionRlParams);

                socialBarRl.removeView(socialFl);
                socialFlLayoutParams.addRule(RelativeLayout.BELOW, buttonCollectionRl.getId());
                socialBarRl.addView(socialFl, socialFlLayoutParams);
            }

        } else {
            buttonCollectionRl.setVisibility(View.GONE);

        }

        invalidate();
    }

    public void displayAsUp() {
        viewPosition = POSITION_UP;
        setSocialBarUIStatus();
    }

    public void displayAsDown() {
        viewPosition = POSITION_DOWN;
        setSocialBarUIStatus();
    }

    public void   toggleButtons() {
        isExpanded = !isExpanded;
        setSocialBarUIStatus();
    }

    public void expandButtons() {
        if (!isExpanded) {
            isExpanded = true;
            setSocialBarUIStatus();
        }
    }

    public void collapseButtons() {
        if (isExpanded) {
            isExpanded = false;
            setSocialBarUIStatus();
        }
    }

    public void updateSocialBarUIStatus(boolean isLiked, boolean isHated,
                                        int numberOfLikes, int numberOfHates, int numberOfComments) {
        this.isLiked = isLiked;
        this.isHated = isHated;
        this.numberOfLikes = numberOfLikes;
        this.numberOfHates = numberOfHates;
        this.numberOfComments = numberOfComments;

        setSocialBarUIStatus();
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
        setSocialBarUIStatus();
    }

    public void setHated(boolean isHated) {
        this.isHated = isHated;
        setSocialBarUIStatus();
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
        setSocialBarUIStatus();
    }

    public void setNumberOfHates(int numberOfHates) {
        this.numberOfHates = numberOfHates;
        setSocialBarUIStatus();
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
        setSocialBarUIStatus();
    }

    public void setOnSocialButtonTouchListener(OnTouchListener listener) {
        socialFl.setOnTouchListener(listener);
    }

    public void setOnLikeClickListener(OnClickListener listener) {
        likeFl.setOnClickListener(listener);
    }

    public void setOnDisLikeClickListener(OnClickListener listener) {
        hateFl.setOnClickListener(listener);
    }

    public void setOnCommentClickListener(OnClickListener listener) {
        commentFl.setOnClickListener(listener);
    }

    public boolean isExpanded() {
        return isExpanded;
    }
    public boolean isHated() {
        return isHated;
    }
    public boolean isLiked() {
        return isLiked;
    }
}
