package com.chan.today.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chan.today.R;

/**
 * Created by Chan on 2016/5/25.
 * <p/>
 * 自定义底部按钮，随下滑隐藏，上拉显示
 */
public class BottomMenu extends LinearLayout {

    private ImageView iv_date;
    private ImageView iv_share;
    private ImageView iv_quit;

    private ObjectAnimator slipInAnimation;
    private ObjectAnimator slipOutAnimation;

    private boolean isHide = false; //标志是否布局已经隐藏

    private OnBottomMenuItemClickListener listener;

    public void setOnBottomMenuItemClickListener (OnBottomMenuItemClickListener listener) {
        this.listener = listener;
    }

    public BottomMenu (Context context) {
        super (context);
        initIvs (context);
    }

    public BottomMenu (Context context, AttributeSet attrs) {
        super (context, attrs);
        initIvs (context);
    }

    /**
     * 初始化ImageViews以及添加ImageViews的点击事件，回调接口的onItemClick
     *
     * @param context
     */
    private void initIvs (Context context) {

        requestFocus ();

        this.setOrientation (HORIZONTAL);

        LayoutParams params =
                new LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        iv_date = new ImageView (context);
        iv_date.setImageResource (R.drawable.calendar_slector);
        iv_date.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick (View v) {
                listener.onItemClicked (v, 0);
            }
        });

        iv_share = new ImageView (context);
        iv_share.setImageResource (R.drawable.share_slector);
        iv_share.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick (View v) {
                listener.onItemClicked (v, 1);
            }
        });

        iv_quit = new ImageView (context);
        iv_quit.setImageResource (R.drawable.quit_slector);
        iv_quit.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick (View v) {
                listener.onItemClicked (v, 2);
            }
        });

        addView (iv_date, params);
        addView (iv_share, params);
        addView (iv_quit, params);

        // 初始化动画
        slipInAnimation = ObjectAnimator.ofFloat (this, "translationY", 0, DataFactory.dip2px (context, 100));
        slipOutAnimation = ObjectAnimator.ofFloat (this, "translationY", DataFactory.dip2px (context, 100), 0);
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged (w, h, oldw, oldh);
        Log.d (DataFactory.TAG, "this getY" + this.getY () + "");
    }

    /**
     * 每次滑动调用
     * <p/>
     * 动画(隐藏)
     */
    public void startSlipInAnimation (int delayMillis) {

        if (isHide) {
            return;
        }

        if (!slipInAnimation.isRunning ()) {
            slipInAnimation.setDuration (500);
            slipInAnimation.setStartDelay (delayMillis);
            slipInAnimation.start ();
            slipInAnimation.addListener (new Animator.AnimatorListener () {
                @Override
                public void onAnimationStart (Animator animation) {

                }

                @Override
                public void onAnimationEnd (Animator animation) {
                    isHide = true;
                }

                @Override
                public void onAnimationCancel (Animator animation) {

                }

                @Override
                public void onAnimationRepeat (Animator animation) {

                }
            });
        }
    }

    /**
     * 每次停止滑动滑动显示布局
     * <p/>
     * 从底部滑出效果,
     * <p/>
     * 在未滑动的时候调用SlipInAnimation
     */
    public void startSlipOutAnimation () {

        if (!slipOutAnimation.isRunning ()) {
            slipOutAnimation.setDuration (500);
            slipOutAnimation.start ();
            slipOutAnimation.addListener (new Animator.AnimatorListener () {
                @Override
                public void onAnimationStart (Animator animation) {

                }

                @Override
                public void onAnimationEnd (Animator animation) {
                    isHide = false;
                    //4秒未进行操作则隐藏
                    startSlipInAnimation (4000);
                }

                @Override
                public void onAnimationCancel (Animator animation) {

                }

                @Override
                public void onAnimationRepeat (Animator animation) {

                }
            });
        }
    }

    public interface OnBottomMenuItemClickListener {
        public void onItemClicked (View view, int position);
    }
}
