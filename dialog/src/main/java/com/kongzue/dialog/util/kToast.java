package com.kongzue.dialog.util;

import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.view.NotifyToastShadowView;
import com.kongzue.dialog.v3.Notification;
import ohos.aafwk.ability.AbilityPackage;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.*;
import ohos.app.Context;

import java.util.Optional;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/7/25 14:56
 */
public class kToast {
    private final String TAG = kToast.class.getSimpleName();
    private NotifyToastShadowView rootView;
    private Window toastWindow;
    private int width, height;

    private ToastDialog toast;
    private Notification.DURATION_TIME durationTime = Notification.DURATION_TIME.LONG;
    private OnDismissListener onDismissListener;

    public kToast(Notification.DURATION_TIME durationTime, OnDismissListener onDismissListener, int ht) {
        this.durationTime = durationTime;
        this.onDismissListener = onDismissListener;
        height = ht;
    }

    public kToast show(final Context context, final NotifyToastShadowView view) {
        cancel();
        rootView = view;
        switch (Notification.mode) {
            case TOAST:
                showToast(context);
                break;
            case FLOATING_WINDOW:
                showFloatingWindow(context);
                break;
        }
        view.setOnDismissListener(() -> cancel());
        return this;
    }

    private WindowManager windowManager;
    private WindowManager.LayoutConfig windowParams;

    private void showFloatingWindow(Context context) {
        windowManager = WindowManager.getInstance();
        windowParams = new WindowManager.LayoutConfig();
        DisplayAttributes da = getDisplayAttributes(context);
        windowParams.width = ComponentContainer.LayoutConfig.MATCH_PARENT;
        windowParams.height = ComponentContainer.LayoutConfig.MATCH_CONTENT;
        windowParams.alignment = LayoutAlignment.TOP;
		windowParams.type = WindowManager.LayoutConfig.MOD_TOAST;
        if (context instanceof AbilityPackage) {
            windowParams.type = WindowManager.LayoutConfig.MOD_APPLICATION_OVERLAY;
        }
        try {
            toastWindow = windowManager.addComponent(rootView, context, windowParams.type);
            toastWindow.setTransparent(true);
            toastWindow.setLayoutConfig(windowParams);
            rootView.getContext().getUITaskDispatcher().delayDispatch(new Runnable() {
                @Override
                public void run() {
                    if (!rootView.isTouched()) cancel();
                }
            }, durationTime == Notification.DURATION_TIME.LONG ? 3000 : 1500);
        } catch (Exception e) {
            error("启动通知错误，在使用 context 为 Application 时必须声明：ohos.permission.SYSTEM_FLOAT_WINDOW 开启悬浮窗权限!");
        }
    }

    private void showToast(Context context) {
        toast = new ToastDialog(context.getApplicationContext());
        toast.setAlignment(LayoutAlignment.HORIZONTAL_CENTER | LayoutAlignment.TOP);
        toast.setDuration(durationTime.ordinal());
        DisplayAttributes da = getDisplayAttributes(context);
        toast.setComponent((Component) rootView);
        toast.setSize(ComponentContainer.LayoutConfig.MATCH_PARENT, 600);
        toast.getWindow().setStatusBarVisibility(Component.INVISIBLE);
        toast.setTransparent(true);
        toast.show();
    }

    public DisplayAttributes getDisplayAttributes(Context context) {
        Optional<Display> optionalDisplay = DisplayManager.getInstance().getDefaultDisplay(context);
        Display display = null;
        if (optionalDisplay.isPresent()) {
            display = optionalDisplay.get();
            if (display != null && display.getAttributes() != null) {
                return display.getAttributes();
            }
        }
        return null;
    }

    public void cancel() {
        try {
            if (onDismissListener != null) onDismissListener.onDismiss();
            if (toast != null) {
                toast.cancel();
                toast = null;
            }
            if (windowManager != null && toastWindow != null) {
                windowManager.destroyWindow(toastWindow);
                toastWindow = null;
            }
        } catch (Exception e) {
        }
    }

    public void log(Object o) {
        if (DialogSettings.DEBUGMODE) Log.i(">>>", o.toString());
    }

    public void error(Object o) {
        if (DialogSettings.DEBUGMODE) Log.e(">>>", o.toString());
    }
}