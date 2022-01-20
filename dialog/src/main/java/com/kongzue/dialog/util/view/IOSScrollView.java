package com.kongzue.dialog.util.view;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ScrollView;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/5/11 02:30
 */
public class IOSScrollView extends ScrollView implements Component.TouchEventListener{
    
    private static final int size = 2;
    private Component inner;
    private float x;
    private Rect normal = new Rect();;
    
    
    public IOSScrollView(Context context) {
        super(context);
    }
    
    public IOSScrollView(Context context, AttrSet attrs) {
        super(context, attrs);
    }
    
    public IOSScrollView(Context context, AttrSet attrs, String defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void postLayout() {
        if (getChildCount() > 0) {
            inner = getComponentAt(0);
        }
        super.postLayout();
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (getChildCount() > 0 & getComponentAt(0) == null) {
            return false;
        } else {
            commOnTouchEvent(touchEvent);
        }
        return false;
    }

    public void commOnTouchEvent(TouchEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                x = ev.getPointerPosition(0).getX();
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                if (isNeedAnimation()) {
                    animation();
                }
                break;
            case TouchEvent.POINT_MOVE:
                final float preX = x;
                float nowX = ev.getPointerPosition(0).getX();
                int deltaX = (int) (preX - nowX) / size;
                
                x = nowX;
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                        return;
                    }
                    int xx = inner.getLeft() - deltaX;
                    inner.setComponentPosition(xx, inner.getTop(), inner.getRight()- deltaX, inner.getBottom());
                }
                break;
            default:
                break;
        }
    }
    
    public void animation() {
        inner.createAnimatorProperty().moveToX(inner.getLeft()).setDuration(300).start();
        inner.setComponentPosition(normal.left, normal.top, normal.right, normal.bottom);
        normal.set(0, 0, 0, 0);
    }
    
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }
    
    public boolean isNeedMove() {
        int offset = inner.getEstimatedWidth() - getWidth();
        int scrollX = getScrollValue(AXIS_X);
        if (scrollX == 0 || scrollX == offset) {
            return true;
        }
        return false;
    }

}
