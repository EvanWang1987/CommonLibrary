package com.github.evan.common_library.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.evan.common_library.R;
import com.github.evan.common_library.utils.DensityUtil;
import com.github.evan.common_library.utils.ResourceUtil;

/**
 * 状态切换器
 */
public class StateSwitcher extends ConstraintLayout {
    public static final int IDEA = -1;
    public static final int LOADING = 0;
    public static final int NO_NETWORK = 1;
    public static final int EMPTY = 2;
    public static final int NO_SEARCH_CONTENT = 3;
    public static final int NETWORK_TIME_OUT = 4;
    public static final int UNKNOWN = 5;

    private Drawable mLoadingDrawable, mUnknownDrawable, mLoadEmptyDrawable, mNetUnAvailableDrawable, mNetTimeoutDrawable, mNoSearchContentDrawable;
    private CharSequence mLoadingText, mUnknownText, mLoadEmptyText, mNetUnAvailableText, mNetTimeoutText, mNoSearchContentText;

    private int mState = IDEA;
    private boolean mIsLoadingWithRotateAnim = true;
    private @ColorInt int mStatusTextColor;
    private @Dimension int mStatusTextSize;
    private ImageView mImgStateDrawable;
    private TextView mTxtState;
    private ObjectAnimator mRotateAnim;

    public StateSwitcher(Context context) {
        super(context);
        setup(context, null, -1);
    }

    public StateSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs, -1);
    }

    public StateSwitcher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs, defStyleAttr);
    }

    public void setState(int loadingStatus) {
        mState = loadingStatus;
        switch (mState){
            case IDEA:
                mImgStateDrawable.setVisibility(INVISIBLE);
                mTxtState.setVisibility(INVISIBLE);
                stopRotateAnim();
                break;

            case LOADING:
                mImgStateDrawable.setImageDrawable(mLoadingDrawable);
                mTxtState.setText(mLoadingText);
                startRotateAnim();
                break;

            case UNKNOWN:
                mImgStateDrawable.setImageDrawable(mUnknownDrawable);
                mTxtState.setText(mUnknownText);
                stopRotateAnim();
                break;

            case EMPTY:
                mImgStateDrawable.setImageDrawable(mLoadEmptyDrawable);
                mTxtState.setText(mLoadEmptyText);
                stopRotateAnim();
                break;

            case NO_NETWORK:
                mImgStateDrawable.setImageDrawable(mNetUnAvailableDrawable);
                mTxtState.setText(mNetUnAvailableText);
                stopRotateAnim();
                break;

            case NETWORK_TIME_OUT:
                mImgStateDrawable.setImageDrawable(mNetTimeoutDrawable);
                mTxtState.setText(mNetTimeoutText);
                stopRotateAnim();
                break;

            case NO_SEARCH_CONTENT:
                mImgStateDrawable.setImageDrawable(mNoSearchContentDrawable);
                mTxtState.setText(mNoSearchContentText);
                stopRotateAnim();
                break;
        }

        if(mState != IDEA){
            mImgStateDrawable.setVisibility(VISIBLE);
            mTxtState.setVisibility(VISIBLE);
        }
    }

    public int getState() {
        return mState;
    }

    public Drawable getLoadingDrawable() {
        return mLoadingDrawable;
    }

    public void setLoadingDrawable(Drawable loadingDrawable) {
        this.mLoadingDrawable = loadingDrawable;
    }

    public Drawable getUnknownDrawable() {
        return mUnknownDrawable;
    }

    public void setUnknownDrawable(Drawable unknownDrawable) {
        this.mUnknownDrawable = unknownDrawable;
    }

    public Drawable getLoadEmptyDrawable() {
        return mLoadEmptyDrawable;
    }

    public void setLoadEmptyDrawable(Drawable loadEmptyDrawable) {
        this.mLoadEmptyDrawable = loadEmptyDrawable;
    }

    public Drawable getNetUnAvailableDrawable() {
        return mNetUnAvailableDrawable;
    }

    public void setNetUnAvailableDrawable(Drawable netUnAvailableDrawable) {
        this.mNetUnAvailableDrawable = netUnAvailableDrawable;
    }

    public Drawable getNetTimeoutDrawable() {
        return mNetTimeoutDrawable;
    }

    public void setNetTimeoutDrawable(Drawable netTimeoutDrawable) {
        this.mNetTimeoutDrawable = netTimeoutDrawable;
    }

    public CharSequence getLoadingText() {
        return mLoadingText;
    }

    public void setLoadingText(CharSequence loadingText) {
        this.mLoadingText = loadingText;
    }

    public CharSequence getUnknownText() {
        return mUnknownText;
    }

    public void setUnknownText(CharSequence unknownText) {
        this.mUnknownText = unknownText;
    }

    public CharSequence getLoadEmptyText() {
        return mLoadEmptyText;
    }

    public void setmLoadEmptyText(CharSequence loadEmptyText) {
        this.mLoadEmptyText = loadEmptyText;
    }

    public CharSequence getNetUnAvailableText() {
        return mNetUnAvailableText;
    }

    public void setNetUnAvailableText(CharSequence netUnAvailableText) {
        this.mNetUnAvailableText = netUnAvailableText;
    }

    public CharSequence getNetTimeoutText() {
        return mNetTimeoutText;
    }

    public void setNetTimeoutText(CharSequence netTimeoutText) {
        this.mNetTimeoutText = netTimeoutText;
    }

    private void startRotateAnim(){
        if(mIsLoadingWithRotateAnim){
            mRotateAnim.start();
        }
    }

    private void stopRotateAnim(){
        if(mIsLoadingWithRotateAnim){
            if(mRotateAnim.isRunning()){
                mRotateAnim.cancel();
                mImgStateDrawable.setRotation(0f);
            }
        }
    }

    private void setup(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_state_switcher, this, true);
        mTxtState = findViewById(R.id.txt_state_switcher);
        mImgStateDrawable = findViewById(R.id.img_state_switcher);

        mRotateAnim = ObjectAnimator.ofFloat(mImgStateDrawable, "rotation", 0f, 360f);
        mRotateAnim.setDuration(750);
        mRotateAnim.setRepeatCount(ObjectAnimator.INFINITE);
        mRotateAnim.setRepeatMode(ObjectAnimator.RESTART);
        mRotateAnim.setInterpolator(new LinearInterpolator());
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StateSwitcher);
            mLoadingDrawable = typedArray.getDrawable(R.styleable.StateSwitcher_loadingDrawable);
            mLoadingText = typedArray.getText(R.styleable.StateSwitcher_loadingText);
            mUnknownDrawable = typedArray.getDrawable(R.styleable.StateSwitcher_unknownDrawable);
            mUnknownText = typedArray.getText(R.styleable.StateSwitcher_unknownText);
            mLoadEmptyDrawable = typedArray.getDrawable(R.styleable.StateSwitcher_loadEmptyDrawable);
            mLoadEmptyText = typedArray.getText(R.styleable.StateSwitcher_loadEmptyText);
            mNetUnAvailableDrawable = typedArray.getDrawable(R.styleable.StateSwitcher_netUnAvailableDrawable);
            mNetUnAvailableText = typedArray.getText(R.styleable.StateSwitcher_netUnAvailableText);
            mNetTimeoutDrawable = typedArray.getDrawable(R.styleable.StateSwitcher_netTimeoutDrawable);
            mNetTimeoutText = typedArray.getText(R.styleable.StateSwitcher_netTimeoutText);
            mNoSearchContentDrawable = typedArray.getDrawable(R.styleable.StateSwitcher_noSearchContentDrawable);
            mNoSearchContentText = typedArray.getString(R.styleable.StateSwitcher_noSearchContentText);
            mStatusTextColor = typedArray.getColor(R.styleable.StateSwitcher_textColor, ResourceUtil.getColor(R.color.DarkGray));
            mStatusTextSize = (int) typedArray.getDimension(R.styleable.StateSwitcher_textSize, DensityUtil.sp2px(16));
            mIsLoadingWithRotateAnim = typedArray.getBoolean(R.styleable.StateSwitcher_isLoadingWithRotateAnimation, true);

            mLoadingDrawable = null == mLoadingDrawable ? ResourceUtil.getDrawable(R.mipmap.status_loading) : mLoadingDrawable;
            mUnknownDrawable = null == mUnknownDrawable ? ResourceUtil.getDrawable(R.mipmap.status_unknown_error) : mUnknownDrawable;
            mLoadEmptyDrawable = null == mLoadEmptyDrawable ? ResourceUtil.getDrawable(R.mipmap.status_empty_data) : mLoadEmptyDrawable;
            mNetUnAvailableDrawable = null == mNetUnAvailableDrawable ? ResourceUtil.getDrawable(R.mipmap.status_net_unavailable) : mNetUnAvailableDrawable;
            mNetTimeoutDrawable = null == mNetTimeoutDrawable ? ResourceUtil.getDrawable(R.mipmap.status_net_timeout) : mNetTimeoutDrawable;
            mNoSearchContentDrawable = null == mNoSearchContentDrawable ? ResourceUtil.getDrawable(R.mipmap.status_no_search_content) : mNoSearchContentDrawable;
            mLoadingText = null == mLoadingText ? ResourceUtil.getString(R.string.loading_pager_loading) : mLoadingText;
            mUnknownText = null == mUnknownText ? ResourceUtil.getString(R.string.loading_pager_unknown_error) : mUnknownText;
            mLoadEmptyText = null == mLoadEmptyText ? ResourceUtil.getString(R.string.loading_pager_empty_data) : mLoadEmptyText;
            mNetUnAvailableText = null == mNetUnAvailableText ? ResourceUtil.getString(R.string.loading_pager_net_unavailable) : mNetUnAvailableText;
            mNetTimeoutText = null == mNetTimeoutText ? ResourceUtil.getString(R.string.loading_pager_net_timeout) : mNetTimeoutText;
            mNoSearchContentText = null == mNoSearchContentText ? ResourceUtil.getString(R.string.loading_pager_no_search_content) : mNoSearchContentText;
            typedArray.recycle();
        } else {
            mLoadingDrawable = ResourceUtil.getDrawable(R.mipmap.status_loading);
            mUnknownDrawable = ResourceUtil.getDrawable(R.mipmap.status_unknown_error);
            mLoadEmptyDrawable = ResourceUtil.getDrawable(R.mipmap.status_empty_data);
            mNetUnAvailableDrawable = ResourceUtil.getDrawable(R.mipmap.status_net_unavailable);
            mNetTimeoutDrawable = ResourceUtil.getDrawable(R.mipmap.status_net_timeout);
            mNoSearchContentDrawable = ResourceUtil.getDrawable(R.mipmap.status_no_search_content);
            mLoadingText = ResourceUtil.getString(R.string.loading_pager_loading);
            mUnknownText = ResourceUtil.getString(R.string.loading_pager_unknown_error);
            mLoadEmptyText = ResourceUtil.getString(R.string.loading_pager_empty_data);
            mNetUnAvailableText = ResourceUtil.getString(R.string.loading_pager_net_unavailable);
            mNetTimeoutText = ResourceUtil.getString(R.string.loading_pager_net_timeout);
            mNoSearchContentText = ResourceUtil.getString(R.string.loading_pager_no_search_content);
            mStatusTextColor = ResourceUtil.getColor(R.color.DarkGray);
            mStatusTextSize = DensityUtil.sp2px(16);
            mIsLoadingWithRotateAnim = true;
        }
    }






}
