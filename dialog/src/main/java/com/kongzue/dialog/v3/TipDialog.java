package com.kongzue.dialog.v3;

import com.kongzue.dialog.ResourceTable;
import com.kongzue.dialog.interfaces.OnBackClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.util.*;
import com.kongzue.dialog.util.view.BlurView;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementScatter;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.kongzue.dialog.util.DialogSettings.blurAlpha;
import static java.util.Objects.isNull;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/22 16:16
 */
public class TipDialog extends DialogBase {
    private DialogSettings.THEME tipTheme;

    private static EventHandler mHandler = new EventHandler(EventRunner.getMainEventRunner());

    public enum TYPE {
        WARNING, SUCCESS, ERROR, OTHER
    }

    private OnDismissListener dismissListener;

    public static TipDialog waitDialogTemp;
    protected CharSequence message;
    private TYPE type;
    private Element tipImage;

    private BlurView blurView;

    protected boolean isAlreadyShown;
    protected int customDialogStyleId;

    private DependentLayout boxBody;
    private DependentLayout boxBlur;
    private DependentLayout boxProgress;
    private ProgressView progress;
    private DependentLayout boxTip, tipbox;
    private Text txtInfo;

    private int tipTime = 1500;

    protected OnDismissListener onDismissListener;
    protected OnDismissListener dismissEvent;
    protected OnShowListener onShowListener;
    public OnBackClickListener onBackClickListener;

    protected TextInfo titleTextInfo;
    protected TextInfo messageTextInfo;
    protected TextInfo tipTextInfo;
    protected TextInfo buttonTextInfo;
    protected TextInfo buttonPositiveTextInfo;
    protected InputInfo inputInfo;
    protected int backgroundColor = 0;
    protected Component customView;
    protected int backgroundResId = -1;

    private Timer cancelTimer;

    protected Boolean cancelable;

    protected TipDialog() {
    }

    public static TipDialog build(Context context) {
        synchronized (TipDialog.class) {
            TipDialog waitDialog = new TipDialog();

            if (waitDialogTemp == null) {
                waitDialogTemp = waitDialog;
            } else {
                if (waitDialogTemp.context.get() != context) {
                    dismiss();
                    waitDialogTemp = waitDialog;
                } else {
                    waitDialog = waitDialogTemp;
                }
            }
            waitDialog.context = new WeakReference<>(context);
            waitDialog.build(waitDialog, ResourceTable.Layout_dialog_wait);
            return waitDialog;
        }
    }

    public static TipDialog showWait(Context context, CharSequence message) {
        synchronized (TipDialog.class) {
            TipDialog waitDialog = build(context);

            waitDialogTemp.onDismissListener = () -> {
                if (waitDialogTemp != null && waitDialogTemp.dismissListener != null)
                    waitDialogTemp.dismissListener.onDismiss();
                waitDialogTemp = null;
            };

//            if (waitDialog == null) {
//                waitDialogTemp.setTip(null);
//                waitDialogTemp.setMessage(message);
//                if (waitDialogTemp.cancelTimer != null) waitDialogTemp.cancelTimer.cancel();
//                return waitDialogTemp;
//            } else {
                waitDialog.message = message;
                waitDialog.type = null;
                waitDialog.tipImage = null;
                if (waitDialog.cancelTimer != null) waitDialog.cancelTimer.cancel();
                waitDialog.showDialog(context);
                return waitDialog;
//            }
        }
    }

    public static TipDialog showWait(Context context, int messageResId) {
        synchronized (TipDialog.class) {
            TipDialog waitDialog = build(context);

            waitDialogTemp.onDismissListener = () -> {
                if (waitDialogTemp != null && waitDialogTemp.dismissListener != null)
                    waitDialogTemp.dismissListener.onDismiss();
                waitDialogTemp = null;
            };

//            if (waitDialog == null) {
//                waitDialogTemp.setTip(null);
//                waitDialogTemp.setMessage(context.getString(messageResId));
//                if (waitDialogTemp.cancelTimer != null) waitDialogTemp.cancelTimer.cancel();
//                return waitDialogTemp;
//            } else {
                waitDialog.message = context.getString(messageResId);
                waitDialog.type = null;
                waitDialog.tipImage = null;
                if (waitDialog.cancelTimer != null) waitDialog.cancelTimer.cancel();
                waitDialog.showDialog(context);
                return waitDialog;
            }
//        }
    }

    public static TipDialog show(Context context, CharSequence message, TYPE type) {
        synchronized (TipDialog.class) {
            TipDialog waitDialog = build(context);

            waitDialogTemp.onDismissListener = () -> {
                if (waitDialogTemp != null && waitDialogTemp.dismissListener != null)
                    waitDialogTemp.dismissListener.onDismiss();
                waitDialogTemp = null;
            };

//            if (waitDialog == null) {
//                waitDialogTemp.setTip(type);
//                waitDialogTemp.setMessage(message);
//                waitDialogTemp.autoDismiss();
//                return waitDialogTemp;
//            } else {
                waitDialog.message = message;
                waitDialog.setTip(type);
                waitDialog.showDialog(context);
                waitDialog.autoDismiss();
                return waitDialog;
            }
//        }
    }

    private void autoDismiss() {
        if (cancelTimer != null) cancelTimer.cancel();
        cancelTimer = new Timer();
        cancelTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                doDismiss();
                dismiss();
                cancelTimer.cancel();
            }
        }, tipTime);
    }

    public static TipDialog show(Context context, int messageResId, TYPE type) {
        return show(context, context.getString(messageResId), type);
    }

    public static TipDialog show(Context context, CharSequence message, int icoResId) {
        synchronized (TipDialog.class) {
            TipDialog waitDialog = build(context);

            waitDialogTemp.onDismissListener = () -> {
                if (waitDialogTemp != null && waitDialogTemp.dismissListener != null)
                    waitDialogTemp.dismissListener.onDismiss();
                waitDialogTemp = null;
            };

//            if (waitDialog == null) {
//                waitDialogTemp.setTip(icoResId);
//                waitDialogTemp.setMessage(message);
//                waitDialogTemp.autoDismiss();
//                return waitDialogTemp;
//            } else {
                waitDialog.message = message;
                waitDialog.setTip(icoResId);
                waitDialog.showDialog(context);
                waitDialog.autoDismiss();
                return waitDialog;
            }
//        }
    }

    public static TipDialog show(Context context, int messageResId, int icoResId) {
        return show(context, context.getString(messageResId), icoResId);
    }

    protected void showDialog(Context context) {
        super.showDialog(1, context);
        setDismissEvent();
    }

    protected void setDismissEvent() {
        onDismissListener = () -> {
            if (dismissListener != null)
                dismissListener.onDismiss();
            waitDialogTemp = null;
        };
    }

    private Component rootView;

    @Override
    public void bindView(ComponentContainer rootView) {
        if (boxTip != null) {
            boxTip.removeAllComponents();
        }
        if (boxBlur != null) {
            boxBlur.removeAllComponents();
        }

        this.rootView = rootView;
        if (rootView != null) {
            tipbox = UiUtil.getComponent(rootView, ResourceTable.Id_tip_box);
            boxBody = UiUtil.getComponent(rootView, ResourceTable.Id_box_body);
            boxBlur = UiUtil.getComponent(rootView, ResourceTable.Id_box_blur);
            boxProgress = UiUtil.getComponent(rootView, ResourceTable.Id_box_progress);
            progress = UiUtil.getComponent(rootView, ResourceTable.Id_circularprogressbar);
            boxTip = UiUtil.getComponent(rootView, ResourceTable.Id_box_tip);
            txtInfo = UiUtil.getComponent(rootView, ResourceTable.Id_txt_info);

            refreshView();
            if (onShowListener != null) onShowListener.onShow(this);
        }
    }

    @Override
    public void refreshView() {
        if (rootView != null) {
            final int bkgResId, blurFrontColor;
            if (message.length() > 14) {
                tipbox.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_CONTENT, 400));
            } else {
                tipbox.setLayoutConfig(new ComponentContainer.LayoutConfig(400, 400));
            }
            if (tipTheme == null) tipTheme = DialogSettings.tipTheme;
            if (DialogSettings.tipBackgroundResId != 0 && backgroundResId == -1) {
                backgroundResId = DialogSettings.tipBackgroundResId;
            }
            switch (tipTheme) {
                case LIGHT:
                    tipbox.setBackground(ElementScatter.getInstance(tipbox.getContext()).parse(ResourceTable.Graphic_dialog_light_shape));
                    int darkColor = Color.rgb(0, 0, 0);
                    blurFrontColor = Color.argb(blurAlpha, 0, 0, 0);
                    if (progress != null) {
                        progress.setColor(Color.BLACK);
                    }
                    txtInfo.setTextColor(new Color(darkColor));
                    if (type != null) {
                        boxProgress.setVisibility(Component.HIDE);
                        boxTip.setVisibility(Component.VISIBLE);
                        switch (type) {
                            case OTHER:
                                boxTip.setBackground(tipImage);
                                break;
                            case ERROR:
                                PixelMapElement pixelMap = ResTUtil.getPixelMapDrawable(boxBody.getContext(), ResourceTable.Media_img_error_dark);
                                boxTip.setBackground(pixelMap);
                                break;
                            case WARNING:
                                PixelMapElement pixelMap1 = ResTUtil.getPixelMapDrawable(boxBody.getContext(), ResourceTable.Media_img_warning_dark);
                                boxTip.setBackground(pixelMap1);
                                break;
                            case SUCCESS:
                                PixelMapElement pixelMap2 = ResTUtil.getPixelMapDrawable(boxBody.getContext(), ResourceTable.Media_img_finish_dark);
                                boxTip.setBackground(pixelMap2);
                                break;
                        }
                    } else {
                        boxProgress.setVisibility(Component.VISIBLE);
                        boxTip.setVisibility(Component.HIDE);
                    }
                    break;
                case DARK:
                    tipbox.setBackground(ElementScatter.getInstance(boxTip.getContext()).parse(ResourceTable.Graphic_dialog_dark_shape));
                    int lightColor = Color.rgb(255, 255, 255);
                    blurFrontColor = Color.argb(blurAlpha, 0, 0, 0);
                    if (progress != null) {
                        progress.setColor(Color.WHITE);
                    }
                    txtInfo.setTextColor(new Color(lightColor));
                    if (type != null) {
                        boxProgress.setVisibility(Component.HIDE);
                        boxTip.setVisibility(Component.VISIBLE);
                        switch (type) {
                            case OTHER:
                                boxTip.setBackground(tipImage);
                                break;
                            case ERROR:
                                PixelMapElement pixelMap = ResTUtil.getPixelMapDrawable(boxBody.getContext(), ResourceTable.Media_img_error);
                                boxTip.setBackground(pixelMap);
                                break;
                            case WARNING:
                                PixelMapElement pixelMap1 = ResTUtil.getPixelMapDrawable(boxBody.getContext(), ResourceTable.Media_img_warning);
                                boxTip.setBackground(pixelMap1);
                                break;
                            case SUCCESS:
                                PixelMapElement pixelMap2 = ResTUtil.getPixelMapDrawable(boxBody.getContext(), ResourceTable.Media_img_finish);
                                boxTip.setBackground(pixelMap2);
                                break;
                        }
                    } else {
                        boxProgress.setVisibility(Component.VISIBLE);
                        boxTip.setVisibility(Component.HIDE);
                    }
                    break;
                default:
                    blurFrontColor = Color.argb(blurAlpha, 0, 0, 0);
                    break;
            }
            if (message == null) {
                txtInfo.setVisibility(Component.HIDE);
            } else {
                txtInfo.setVisibility(Component.VISIBLE);
                txtInfo.setText(message.toString());
                useTextInfo(txtInfo, tipTextInfo);
            }
        }

    }

    @Override
    public void show(Context context) {
    }

    public void showNoAutoDismiss(Context context) {
        showDialog(context);
    }

    public OnDismissListener getOnDismissListener() {
        return dismissListener == null ? () -> {

        } : dismissListener;
    }

    public TipDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.dismissListener = onDismissListener;
        setDismissEvent();
        return this;
    }

    public OnShowListener getOnShowListener() {
        return onShowListener == null ? dialog -> {
        } : onShowListener;
    }

    public TipDialog setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }

    public static void dismiss() {
        if (waitDialogTemp != null) waitDialogTemp.doDismiss();
        waitDialogTemp = null;
        List<DialogBase> temp = new ArrayList<>();
        temp.addAll(dialogList);
        for (DialogBase dialog : temp) {
            if (dialog instanceof TipDialog) {
                dialog.doDismiss();
            }
        }
    }

    public static void dismiss(int millisecond) {
        mHandler.postTask(() -> dismiss(), millisecond);
    }

    public CharSequence getMessage() {
        return message;
    }

    public TipDialog setMessage(CharSequence message) {
        this.message = message;
        if (txtInfo != null) txtInfo.setText(message.toString());
        refreshView();
        return this;
    }

    public TipDialog setMessage(int messageResId) {
        this.message = context.get().getString(messageResId);
        if (txtInfo != null) txtInfo.setText(message.toString());
        refreshView();
        return this;
    }

    public TipDialog setTip(TYPE type) {
        this.type = type;
        if (type != TYPE.OTHER) tipImage = null;
        refreshView();
        return this;
    }

    public TipDialog setTip(int resId) {
        this.type = TYPE.OTHER;
        tipImage = ResTUtil.getPixelMapDrawable(context.get(), resId);
        refreshView();
        return this;
    }

    public TYPE getType() {
        return type;
    }

    public Element getTipImage() {
        return tipImage;
    }

    public Text getTxtInfo() {
        return txtInfo;
    }

    public TipDialog setTipTime(int tipTime) {
        this.tipTime = tipTime;
        if (type != null) autoDismiss();
        return this;
    }

    public TipDialog setTheme(DialogSettings.THEME theme) {
        tipTheme = theme;
        refreshView();
        return this;
    }

    public DialogSettings.THEME getTheme() {
        return tipTheme;
    }

    public TipDialog setCustomView(Component customView) {
        this.customView = customView;
        refreshView();
        return this;
    }

    public interface OnBindComponent {
        void onBind(TipDialog dialog, Component v);
    }

    private OnBindComponent onBindView;

    public TipDialog setCustomView(int customViewLayoutId, OnBindComponent onBindView) {
        return this;
    }

    public boolean getCancelable() {
        return cancelable == Boolean.TRUE;
    }

    public TipDialog setCancelable(boolean enable) {
        this.cancelable = enable ? Boolean.TRUE : Boolean.FALSE;
        if (dialog != null) dialog.get().setCancelable(cancelable == Boolean.TRUE);
        return this;
    }

    @Deprecated
    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    @Deprecated
    public TipDialog setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshView();
        return this;
    }

    public TextInfo getTipTextInfo() {
        return tipTextInfo;
    }

    public TipDialog setTipTextInfo(TextInfo tipTextInfo) {
        this.tipTextInfo = tipTextInfo;
        refreshView();
        return this;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public TipDialog setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
        refreshView();
        return this;
    }

    public TipDialog setCustomDialogStyleId(int customDialogStyleId) {
        if (isAlreadyShown) {
            return this;
        }
        this.customDialogStyleId = customDialogStyleId;
        return this;
    }

    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }

    public OnBackClickListener getOnBackClickListener() {
        return onBackClickListener;
    }

    public TipDialog setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
        onBackPressed(onBackClickListener);
        return this;
    }
}
