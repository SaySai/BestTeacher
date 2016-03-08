/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.shanghai.haojiajiao.weight.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView.ScaleType;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.weight.library.PullToRefreshBase;


public class RotateLoadingLayout extends LoadingLayout {

	static final int ROTATION_ANIMATION_DURATION = 1200;

	private final Animation mRotateAnimation;
	private final Matrix mHeaderImageMatrix;

	private float mRotationPivotX, mRotationPivotY;

	private final boolean mRotateDrawableWhilePulling;

	public RotateLoadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);

		mRotateDrawableWhilePulling = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);

		mHeaderImage.setScaleType(ScaleType.MATRIX);
		mHeaderImageMatrix = new Matrix();
		mHeaderImage.setImageMatrix(mHeaderImageMatrix);

		mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
	}

	public void onLoadingDrawableSet(Drawable imageDrawable) {
		if (null != imageDrawable) {
			mRotationPivotX = Math.round(imageDrawable.getIntrinsicWidth() / 2f);
			mRotationPivotY = Math.round(imageDrawable.getIntrinsicHeight() / 2f);
		}
	}
	float temp=0.01f;
	float temp1=0;
	protected void onPullImpl(float scaleOfLayout) {
		float angle;
		if (mRotateDrawableWhilePulling) {
//			angle = scaleOfLayout * 90f;
//			Log.i("sd",angle+"");
//			temp=temp+0.01f;
			if(temp1>scaleOfLayout)
			{
				//逐渐变小
				temp=temp-0.01f;
				if(temp>0.5&&temp<1) {
					mHeaderImageMatrix.setScale(temp, temp);
//		mHeaderImageMatrix.setRotate(angle, mRotationPivotX, mRotationPivotY);
					mHeaderImage.setImageMatrix(mHeaderImageMatrix);
				}
				temp1=scaleOfLayout;
				Log.i("sd","变小");
			}else if(temp1<scaleOfLayout)
			{
				//逐渐变大
				temp=temp+0.01f;
				if(temp<1&&temp>0.5) {
					mHeaderImageMatrix.setScale(temp, temp);
//		mHeaderImageMatrix.setRotate(angle, mRotationPivotX, mRotationPivotY);
					mHeaderImage.setImageMatrix(mHeaderImageMatrix);
				}
				temp1=scaleOfLayout;
				Log.i("sd",""+temp);
			}
			Log.i("sd","变小");
		} else {
//			angle = Math.max(0f, Math.min(180f, scaleOfLayout * 360f - 180f));
//			resetImageRotation();
			mHeaderImageMatrix.setScale(1, 1);
			mHeaderImage.setImageMatrix(mHeaderImageMatrix);
		}

	}

	@Override
	protected void refreshingImpl() {
		mHeaderImage.startAnimation(mRotateAnimation);
	}

	@Override
	protected void resetImpl() {
		mHeaderImage.clearAnimation();
		resetImageRotation();
	}

	private void resetImageRotation() {
		if (null != mHeaderImageMatrix) {
			mHeaderImageMatrix.reset();
			mHeaderImage.setImageMatrix(mHeaderImageMatrix);
		}
	}

	@Override
	protected void pullToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected void releaseToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.mipmap.default_ptr_rotate;
	}

}
