/*
 * Copyright 2016 Carbs.Wang (AvatarImageView)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kongzue.dialog.util.view;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.*;
import ohos.agp.utils.Matrix;
import ohos.agp.utils.RectFloat;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;


public class IOSItemImageView extends Image {

    private static final int DEFAULT_CORNER_RADIUS_DP = 6;
    private static final int COLOR_DRAWABLE_DIMENSION = 1;

    private Paint mPaintDraw= new Paint();
    private PixelMap mBitmap;
    private PixelMapShader mBitmapShader;
    private Matrix mMatrix = new Matrix();
    private int mCornerRadius = -1;
    private RectFloat mRectFloat;
    private float mImageViewH;
    private float mImageViewW;
    private float mDrawableW;
    private float mDrawableH;

    private static final String ATTRIBUTE_CornerRadius = "aiv_CornerRadius";

    public IOSItemImageView(Context context) {
        super(context);
        init();

    }

    public IOSItemImageView(Context context, AttrSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();

    }

    public IOSItemImageView(Context context, AttrSet attrs, String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();

    }

    private void init(){
        this.addDrawTask((compoent, canvas) -> {
            if (mBitmap != null) {
                toDrawBitmap(canvas);
            }
        });
        mPaintDraw.setAntiAlias(true);
        mPaintDraw.setStyle(Paint.Style.FILL_STYLE);
        if (mCornerRadius < 0){
            mCornerRadius = dp2px(getContext(), DEFAULT_CORNER_RADIUS_DP);
        }
        postLayout();
    }

    private void initAttr(Context context, AttrSet attrs) {
        if (attrs == null) {
            return;
        }
        boolean isPresentCornerRadius = attrs.getAttr(ATTRIBUTE_CornerRadius).isPresent();
        if (isPresentCornerRadius) {
            mCornerRadius = dp2px(getContext(), attrs.getAttr(ATTRIBUTE_CornerRadius).get().getIntegerValue());
        } else {
            mCornerRadius = dp2px(getContext(), DEFAULT_CORNER_RADIUS_DP);
        }
    }


    public void setImageDrawable(Element drawable) {
        setDrawable(drawable);
    }

    public void setDrawable(Element drawable) {
        PixelMap bitmap = getBitmapFromDrawable(drawable);
        setBitmap(bitmap);
    }

    public void setImageBitmap(PixelMap bm) {
        Element drawable =new PixelMapElement(bm);
        setDrawable(drawable);
    }

    private void toDrawBitmap(Canvas canvas) {
        if(mBitmap == null) return;
        drawBitmap(canvas, mBitmap, true);
    }

    private void drawBitmap(Canvas canvas, PixelMap bitmap, boolean adjustScale) {
        refreshBitmapShaderConfig(bitmap, adjustScale);
        mPaintDraw.setShader(mBitmapShader,Paint.ShaderType.PIXELMAP_SHADER);
        canvas.drawRoundRect(mRectFloat, mCornerRadius,mCornerRadius, mPaintDraw);
    }

    private void refreshBitmapShaderConfig(PixelMap bitmap, boolean adjustScale) {
        PixelMapHolder pixelmapholder = new PixelMapHolder(bitmap);
        mBitmapShader = new PixelMapShader(pixelmapholder, Shader.TileMode.REPEAT_TILEMODE, Shader.TileMode.REPEAT_TILEMODE);
        mMatrix.reset();
        if(adjustScale) {
            if ((mImageViewH * mDrawableW > mDrawableH * mImageViewW)) {
                float scale1 = (mImageViewH)/(mDrawableH);
                float offset1 = (mDrawableW * scale1 - mImageViewW)/2;
                mMatrix.setScale(scale1, scale1);
                mMatrix.postTranslate(-offset1 + getPaddingLeft(), 0 + getPaddingTop());
            } else {
                float scale2 = (mImageViewW)/(mDrawableW);
                float offset2 = (mDrawableH * scale2 - mImageViewH)/2;
                mMatrix.setScale(scale2, scale2);
                mMatrix.postTranslate(0 + getPaddingLeft(), -offset2 + getPaddingTop());
            }
        }
        mBitmapShader.setShaderMatrix(mMatrix);
    }

    private void setBitmap(PixelMap bitmap) {
        if (bitmap == null) {
            return;
        }
        if (bitmap != this.mBitmap) {
            this.mBitmap = bitmap;
            invalidate();
        }
    }

    private PixelMap getBitmapFromDrawable(Element drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof PixelMapElement) {
            mDrawableW = drawable.getWidth();
            mDrawableH = drawable.getHeight();
            return ((PixelMapElement) drawable).getPixelMap();
        }
        try {
            PixelMap bitmap;
            PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
            initializationOptions.size = new Size();
            if (drawable instanceof ShapeElement) {
                initializationOptions.size.width = COLOR_DRAWABLE_DIMENSION;
                initializationOptions.size.height = COLOR_DRAWABLE_DIMENSION;
                initializationOptions.pixelFormat = PixelFormat.ARGB_8888;
                bitmap = PixelMap.create(initializationOptions);
            } else {
                initializationOptions.size.width = drawable.getWidth();
                initializationOptions.size.height = drawable.getHeight();
                initializationOptions.pixelFormat = PixelFormat.ARGB_8888;
                bitmap = PixelMap.create(initializationOptions);
            }
            Texture texture = new Texture(bitmap);
            Canvas canvas = new Canvas(texture);
            drawable.setBounds(0, 0, texture.getWidth(), texture.getHeight());
            drawable.drawToCanvas(canvas);
            mDrawableW = texture.getWidth();
            mDrawableH = texture.getHeight();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void postLayout() {
        super.postLayout();
        mImageViewH = getHeight() - getPaddingTop() - getPaddingBottom();
        mImageViewW = getWidth() - getPaddingLeft() - getPaddingRight();
        mRectFloat = new RectFloat(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes().densityPixels;
        return (int) (dipValue * scale + 0.5f);
    }
}
