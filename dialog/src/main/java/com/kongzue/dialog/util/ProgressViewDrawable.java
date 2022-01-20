/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
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
 */
package com.kongzue.dialog.util;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Component;
import ohos.agp.components.element.FrameAnimationElement;
import ohos.agp.render.Arc;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.utils.ObjectAttribute;

public class ProgressViewDrawable extends FrameAnimationElement {
    private static final int ANGLE_ANIMATOR_DURATION = 2000;
    private static final int SWEEP_ANIMATOR_DURATION = 600;
    public static final int MIN_SWEEP_ANGLE = 30;
    private final RectFloat fBounds = new RectFloat();

    private AnimatorValue mObjectAnimatorSweep;
    private AnimatorValue mObjectAnimatorAngle;
    private boolean mModeAppearing;
    private Paint mPaint;
    private float mCurrentGlobalAngleOffset;
    private float mCurrentGlobalAngle;
    private float mCurrentSweepAngle;
    private float mBorderWidth;
    private boolean mRunning;
    private Component component;
    private static Color color;

    public ProgressViewDrawable(Color colorType, float borderWidth) {
        mBorderWidth = borderWidth;
        color = colorType;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE_STYLE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(color);

        setupAnimations();
    }

    public void setComponent(Component component) {
        if (component != null) {
            this.component = component;
        }
    }

    @Override
    public void drawToCanvas(Canvas canvas) {
        float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
        float sweepAngle = mCurrentSweepAngle;
        if (!mModeAppearing) {
            startAngle = startAngle + sweepAngle;
            sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
        } else {
            sweepAngle += MIN_SWEEP_ANGLE;
        }
        canvas.drawArc(fBounds, new Arc(startAngle, sweepAngle, false), mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }


    private void toggleAppearingMode() {
        mModeAppearing = !mModeAppearing;
        if (mModeAppearing) {
            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
        }
    }

    public void onBoundsChange(Rect bounds) {
        fBounds.left = bounds.left + mBorderWidth / 2f + .5f;
        fBounds.right = bounds.right - mBorderWidth / 2f - .5f;
        fBounds.top = bounds.top + mBorderWidth / 2f + .5f;
        fBounds.bottom = bounds.bottom - mBorderWidth / 2f - .5f;
    }

    private ObjectAttribute<ProgressViewDrawable, Float> mAngleProperty =
            new ObjectAttribute<ProgressViewDrawable, Float>(Float.class, "angle") {
                @Override
                public Float get(ProgressViewDrawable object) {
                    return object.getCurrentGlobalAngle();
                }

                @Override
                public void set(ProgressViewDrawable object, Float value) {
                    object.setCurrentGlobalAngle(value);
                }
            };

    private ObjectAttribute<ProgressViewDrawable, Float> mSweepProperty
            = new ObjectAttribute<ProgressViewDrawable, Float>(Float.class, "arc") {
        @Override
        public Float get(ProgressViewDrawable object) {
            return object.getCurrentSweepAngle();
        }

        @Override
        public void set(ProgressViewDrawable object, Float value) {
            object.setCurrentSweepAngle(value);
        }
    };

    private void setupAnimations() {
        mObjectAnimatorAngle = new AnimatorValue();
        mObjectAnimatorAngle.setCurveType(Animator.CurveType.LINEAR);
        mObjectAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
        mObjectAnimatorAngle.setLoopedCount(-1);
        mObjectAnimatorAngle.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float v) {
                mCurrentGlobalAngle = v * 360f;
                invalidateSelf();
            }
        });
        mObjectAnimatorSweep = new AnimatorValue();
        mObjectAnimatorSweep.setCurveType(Animator.CurveType.DECELERATE);
        mObjectAnimatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);
        mObjectAnimatorSweep.setLoopedCount(-1);
        mObjectAnimatorSweep.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float v) {
                mCurrentSweepAngle = (360f - MIN_SWEEP_ANGLE * 2) * v;
            }
        });
        mObjectAnimatorSweep.setLoopedListener(new Animator.LoopedListener() {
            @Override
            public void onRepeat(Animator animator) {
                toggleAppearingMode();
            }
        });
    }

    public void invalidateSelf() {
        if (component != null) component.invalidate();
    }

    @Override
    public void start() {

        mObjectAnimatorAngle.start();
        mObjectAnimatorSweep.start();
        invalidateSelf();
        if (isRunning()) {
            return;
        }
        mRunning = true;
    }

    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }
        mRunning = false;
        mObjectAnimatorAngle.cancel();
        mObjectAnimatorSweep.cancel();
        invalidateSelf();
    }

    public boolean isRunning() {
        return mRunning;
    }

    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        mCurrentGlobalAngle = currentGlobalAngle;
        invalidateSelf();
    }

    public float getCurrentGlobalAngle() {
        return mCurrentGlobalAngle;
    }

    public void setCurrentSweepAngle(float currentSweepAngle) {
        mCurrentSweepAngle = currentSweepAngle;
        invalidateSelf();
    }

    public float getCurrentSweepAngle() {
        return mCurrentSweepAngle;
    }
}
