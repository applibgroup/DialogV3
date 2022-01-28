package com.kongzue.dialog.util.view;

import com.kongzue.dialog.ResourceTable;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnNotificationClickListener;
import ohos.agp.animation.Animator;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

import java.util.Optional;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/10 23:02
 */
public class NotifyToastShadowView extends DirectionalLayout implements Component.TouchEventListener {

    private OnDismissListener onDismissListener;
    private OnNotificationClickListener onNotificationClickListener;
    private boolean dispatchTouchEvent = true;

    public NotifyToastShadowView(Context context) {
        super(context);
    }

    public NotifyToastShadowView(Context context, AttrSet attrs) {
        super(context, attrs);
    }

    public NotifyToastShadowView(Context context, AttrSet attrs, String styleName) {
        super(context, attrs, styleName);
    }

    private boolean isTouched = false;
    private boolean isTouchDown = false;
    private float touchDownY = 0;

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (dispatchTouchEvent) {
            switch (touchEvent.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN:
                    isTouched = true;
                    isTouchDown = true;
                    touchDownY = touchEvent.getPointerScreenPosition(0).getY();
                    break;
                case TouchEvent.POINT_MOVE:
                    if (isTouchDown) {
                        float delta = touchEvent.getPointerScreenPosition(0).getY() - touchDownY;
                        setTranslationY((delta > 0 ? 0 : delta) - getTop());
                    }
                    break;
                case TouchEvent.PRIMARY_POINT_UP:
                case TouchEvent.CANCEL:
                    createAnimator(touchEvent);
                    break;
            }
            return true;
        }
        return false;
    }
    protected void createAnimator(TouchEvent touchEvent){
        isTouchDown = false;
        if (getBottom() > -dip2px(5)) {
            DirectionalLayout BoxNotic = (DirectionalLayout) findComponentById(ResourceTable.Id_btn_notic);
            if (touchEvent.getPointerScreenPosition(0).getY() > 0 && touchEvent.getPointerScreenPosition(0).getY() < BoxNotic.getHeight()) {
                if (onNotificationClickListener != null)
                    onNotificationClickListener.onClick();
            }
        }
        if (getBottom() > -dip2px(30)) {
            createAnimatorProperty().moveToY(0).setDuration(100).setStateChangedListener(null);
        } else {
            createAnimatorProperty().moveToY(-getHeight()).alpha(0).setDuration(200).setStateChangedListener(new Animator.StateChangedListener() {
                @Override
                public void onStart(Animator animator) {
                    //TODO
                }

                @Override
                public void onStop(Animator animator) {
                    //TODO
                }

                @Override
                public void onCancel(Animator animator) {
                    //TODO
                }

                @Override
                public void onEnd(Animator animator) {
                    setVisibility(HIDE);
                    if (onDismissListener != null) onDismissListener.onDismiss();
                }

                @Override
                public void onPause(Animator animator) {
                    //TODO
                }

                @Override
                public void onResume(Animator animator) {
                    //TODO
                }
            });
        }
    }

    public int dip2px(float dpValue) {
        float scale = 0.0f;
        Optional<Display> displayMetrics = DisplayManager.getInstance().getDefaultDisplay(getContext());
        if (displayMetrics.isPresent()) {
            scale = displayMetrics.get().getAttributes().densityPixels;
        }
        return (int) (dpValue * scale + 0.5f);
    }

    public boolean isTouched() {
        return isTouched;
    }

    public OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }

    public NotifyToastShadowView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public void setOnNotificationClickListener(OnNotificationClickListener onNotificationClickListener) {
        setFocusable(FOCUS_ENABLE);
        setEnabled(true);
        setClickable(true);
        this.onNotificationClickListener = onNotificationClickListener;
    }

    public void setDispatchTouchEvent(boolean dispatchTouchEvent) {
        this.dispatchTouchEvent = dispatchTouchEvent;
    }
}
