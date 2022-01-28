package com.kongzue.dialog.util.view;

import com.kongzue.dialog.util.UiUtil;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout;
import ohos.agp.render.Canvas;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

import java.util.Optional;

public class MaxHeightLayout extends DependentLayout implements Component.DrawTask {
    private int maxHeight;

    public MaxHeightLayout(Context context) {
        super(context);
        init(context);
    }

    public MaxHeightLayout(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init(context);
    }

    public MaxHeightLayout(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init(context);
    }

    private void init(Context context) {
        maxHeight = (int) (getScreenHeight(context) * 0.8);
        addDrawTask(this);
    }

    public MaxHeightLayout setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        heightSize = heightSize <= maxHeight ? heightSize : maxHeight;
        UiUtil.measure(widthMeasureSpec, heightMeasureSpec);
        //super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
    }

    private int getScreenHeight(Context context) {
        Optional<Display> optionalDisplay = DisplayManager.getInstance().getDefaultDisplay(context);
        if (optionalDisplay.isPresent()) {
            return optionalDisplay.get().getRealAttributes().height;
        }
        return 0;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        onMeasure(getWidth(), getHeight());
    }
}
