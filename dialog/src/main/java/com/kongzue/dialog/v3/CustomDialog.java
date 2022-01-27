package com.kongzue.dialog.v3;

import com.kongzue.dialog.ResourceTable;
import com.kongzue.dialog.interfaces.OnBackClickListener;
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.DialogBase;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;

import java.lang.ref.WeakReference;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/13 00:17
 */
public class CustomDialog extends DialogBase {

    private boolean fullScreen = false;
    private OnBindView onBindView;

    private CustomDialog() {
        log("Load Custom Dialog Box: " + toString());
    }

    public static CustomDialog show(Context context, int layoutResId, OnBindView onBindView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = new WeakReference<>(context);
            customDialog.onBindView = onBindView;
            customDialog.customView = LayoutScatter.getInstance(context).parse(layoutResId, null, false);
            customDialog.build(customDialog, layoutResId);
            customDialog.show(context);
            return customDialog;
        }
    }

    public static CustomDialog show(Context context, Component customView, OnBindView onBindView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = new WeakReference<>(context);
            customDialog.onBindView = onBindView;
            customDialog.customView = customView;
            customDialog.build(customDialog, ResourceTable.Layout_dialog_custom);
            customDialog.show(context);
            return customDialog;
        }
    }

    public static CustomDialog show(Context context, Component customView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = new WeakReference<>(context);
            customDialog.customView = customView;
            customDialog.build(customDialog, ResourceTable.Layout_dialog_custom);
            customDialog.show(context);
            return customDialog;
        }
    }

    public static CustomDialog build(Context context, int layoutResId, OnBindView onBindView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = new WeakReference<>(context);
            customDialog.onBindView = onBindView;
            customDialog.customView = LayoutScatter.getInstance(context).parse(layoutResId, null, false);
            customDialog.build(customDialog, layoutResId);
            return customDialog;
        }
    }

    public static CustomDialog build(Context context, Component customView, OnBindView onBindView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = new WeakReference<>(context);
            customDialog.onBindView = onBindView;
            customDialog.customView = customView;
            customDialog.build(customDialog, ResourceTable.Layout_dialog_custom);
            return customDialog;
        }
    }

    public static CustomDialog build(Context context, Component customView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.context = new WeakReference<>(context);
            customDialog.customView = customView;
            customDialog.build(customDialog, ResourceTable.Layout_dialog_custom);
            return customDialog;
        }
    }

    private DependentLayout boxCustom;

    @Override
    public void bindView(ComponentContainer rootView) {
        log("启动自定义对话框 -> " + toString());
        if (boxCustom != null) boxCustom.removeAllComponents();
        boxCustom = (DependentLayout) rootView.findComponentById(ResourceTable.Id_box_custom);
        if (boxCustom == null) {
            if (onBindView != null) onBindView.onBind(this, rootView);
        } else {
            boxCustom.removeAllComponents();
            if (customView.getComponentParent() != null && customView.getComponentParent() instanceof ComponentContainer) {
                customView.getComponentParent().removeComponent(customView);
            }
            DependentLayout.LayoutConfig lp = customLayoutParams != null ? customLayoutParams : new DependentLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
            boxCustom.addComponent(customView, lp);
            boxCustom.setLayoutConfig(lp);
            if (onBindView != null) onBindView.onBind(this, customView);
        }

        if (onShowListener != null) onShowListener.onShow(this);
    }

    private DependentLayout.LayoutConfig customLayoutParams;

    @Override
    public void refreshView() {
        //TODO
    }

    @Override
    public void show(Context context) {
        showDialog();
    }

    public void show(int styleResId) {
        showDialog(styleResId, context.get());
    }

    public interface OnBindView {
        void onBind(CustomDialog dialog, Component v);
    }

    public OnDismissListener getOnDismissListener() {
        return onDismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO
            }
        } : onDismissListener;
    }

    public CustomDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public OnShowListener getOnShowListener() {
        return onShowListener == null ? new OnShowListener() {
            @Override
            public void onShow(DialogBase dialog) {
                //TODO
            }
        } : onShowListener;
    }

    public CustomDialog setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public CustomDialog setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
        return this;
    }

    public boolean getCancelable() {
        return cancelable == BOOLEAN.TRUE;
    }

    public CustomDialog setCancelable(boolean enable) {
        this.cancelable = enable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        if (dialog != null) dialog.get().setCancelable(cancelable == BOOLEAN.TRUE);
        return this;
    }

    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }

    public OnBackClickListener getOnBackClickListener() {
        return onBackClickListener;
    }

    public CustomDialog setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
        return this;
    }

    public DependentLayout.LayoutConfig getCustomLayoutParams() {
        return customLayoutParams;
    }

    public CustomDialog setCustomLayoutParams(DependentLayout.LayoutConfig customLayoutParams) {
        this.customLayoutParams = customLayoutParams;
        return this;
    }
}
