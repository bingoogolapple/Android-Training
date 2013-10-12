package com.bingoogol.smartbulb.util;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;

/**
 * pathMenu动画
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class MyAnimations {

	private static int xOffset = 15;
	private static int yOffset = -13;

	private MyAnimations() {
	}

	/**
	 * 初始化偏移量
	 * 
	 * @param context
	 *            应用程序上下文
	 */
	public static void initOffset(Context context) {
		xOffset = (int) (10.667 * context.getResources().getDisplayMetrics().density);
		yOffset = -(int) (8.667 * context.getResources().getDisplayMetrics().density);
	}

	/**
	 * 获取滚轮动画
	 * 
	 * @param fromDegrees
	 *            开始时的度数
	 * @param toDegrees
	 *            结束时的度数
	 * @param durationMillis
	 *            动画的持续时间
	 * @return Animation
	 */
	public static Animation getRotateAnimation(float fromDegrees, float toDegrees, int durationMillis) {
		RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(durationMillis);
		rotate.setFillAfter(true);
		return rotate;
	}

	/**
	 * 收起pathMenu动画
	 * 
	 * @param viewgroup
	 *            包含pathMenu的布局组件
	 * @param durationMillis
	 *            动画的持续时间
	 */
	public static void startAnimationsIn(ViewGroup viewgroup, int durationMillis) {
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			ImageButton inoutimagebutton = (ImageButton) viewgroup.getChildAt(i);
			inoutimagebutton.setVisibility(0);
			MarginLayoutParams mlp = (MarginLayoutParams) inoutimagebutton.getLayoutParams();
			Animation animation = new TranslateAnimation(xOffset - mlp.leftMargin, 0F, yOffset + mlp.bottomMargin, 0F);

			animation.setFillAfter(true);
			animation.setDuration(durationMillis);
			animation.setStartOffset((i * 100) / (-1 + viewgroup.getChildCount()));
			animation.setInterpolator(new OvershootInterpolator(2F));
			inoutimagebutton.startAnimation(animation);

		}
	}

	/**
	 * 打开pathMenu动画
	 * 
	 * @param viewgroup
	 *            包含pathMenu的布局组件
	 * @param durationMillis
	 *            动画的持续时间
	 */
	public static void startAnimationsOut(ViewGroup viewgroup, int durationMillis) {
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			final ImageButton inoutimagebutton = (ImageButton) viewgroup.getChildAt(i);
			MarginLayoutParams mlp = (MarginLayoutParams) inoutimagebutton.getLayoutParams();
			Animation animation = new TranslateAnimation(0F, xOffset - mlp.leftMargin, 0F, yOffset + mlp.bottomMargin);

			animation.setFillAfter(true);
			animation.setDuration(durationMillis);
			animation.setStartOffset(((viewgroup.getChildCount() - i) * 100) / (-1 + viewgroup.getChildCount()));// 顺序倒一下比较舒服
			animation.setInterpolator(new AnticipateInterpolator(2F));
			animation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation arg0) {
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					inoutimagebutton.setVisibility(8);
				}
			});
			inoutimagebutton.startAnimation(animation);
		}

	}

}