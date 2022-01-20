package com.kongzue.dialog.util;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.render.Canvas;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.app.Context;

public class ProgressView extends Text implements Component.LayoutRefreshedListener, Component.DrawTask {
    private ProgressViewDrawable mAnimatedDrawable;

    private AttrSet mAttrSet;
    private Context mContext;
    private Color color = Color.BLACK;
    private float mStrokeWidth = 4.0f;
    private static final String cpb_default_color = "cpb_default_color";
    private static final String cpb_default_stroke_width = "cpb_default_stroke_width";

    public ProgressView(Context context, AttrSet attrs) {
        super(context, attrs);
        mContext = context;

        if (attrs != null) {
            mAttrSet = attrs;
            color = Color.BLACK;
            mStrokeWidth = mAttrSet.getAttr(cpb_default_stroke_width).isPresent() ?
                    mAttrSet.getAttr(cpb_default_stroke_width).get().getFloatValue() : 4.0f;
        } else {
            color = Color.BLACK;
            mStrokeWidth = 4.0f;
        }

        setLayoutRefreshedListener(this::onRefreshed);
        addDrawTask(this::onDraw);
    }

    public void setColor(Color definedcolor) {
        color = definedcolor;
        setLayoutRefreshedListener(this::onRefreshed);
        addDrawTask(this::onDraw);
    }

    boolean mIndeterminateProgressMode = true;

    @Override
    public void onRefreshed(Component component) {
        if (mIndeterminateProgressMode) {
            invalidate();
        }
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        drawIndeterminateProgress(canvas);
    }

    private void drawIndeterminateProgress(Canvas canvas) {
        if (mAnimatedDrawable == null) {
            mAnimatedDrawable = new ProgressViewDrawable(color, mStrokeWidth);
            int left = 5;
            int right = getWidth();
            int bottom = getWidth();
            int top = 5;
            mAnimatedDrawable.onBoundsChange(new Rect(left, top, right, bottom));
            mAnimatedDrawable.setComponent(this);
            mAnimatedDrawable.start();
            mAnimatedDrawable.drawToCanvas(canvas);
        } else {
            mAnimatedDrawable.drawToCanvas(canvas);
        }
    }
}
