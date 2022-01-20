package com.kongzue.dialog.v3;

import com.kongzue.dialog.ResourceTable;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnNotificationClickListener;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.Log;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.util.UiUtil;
import com.kongzue.dialog.util.kToast;
import com.kongzue.dialog.util.view.NotifyToastShadowView;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.text.Font;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

import java.lang.ref.WeakReference;
import java.util.Optional;

import static com.kongzue.dialog.util.UiUtil.getPixelElementByResId;
import static ohos.agp.animation.Animator.CurveType.DECELERATE;
import static ohos.agp.components.Component.HIDE;
import static ohos.agp.components.element.ShapeElement.RECTANGLE;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/10 18:32
 */
public class Notification {
    private static final String TAG = Notification.class.getSimpleName();

    public enum DURATION_TIME {
        SHORT, LONG
    }

    public enum Mode {
        FLOATING_WINDOW,
        TOAST
    }

    public static Mode mode = Mode.TOAST ;

    protected kToast ktoast;
    private OnNotificationClickListener onNotificationClickListener;
    private OnDismissListener onDismissListener;

    private DialogSettings.STYLE style;
    private int backgroundColor;
    private DURATION_TIME durationTime = DURATION_TIME.LONG;
    private WeakReference<Context> context;
    private CharSequence title;
    private CharSequence message;
    private int iconResId;

    private Component customView;
    private NotifyToastShadowView rootView;

    private DependentLayout boxBody;
    private DirectionalLayout boxTitle;
    private DirectionalLayout boxNotic;
    private DirectionalLayout btnNotic;
    private Text txtTitle;
    private Text txtMessage;
    private Image imgIcon;
    private DependentLayout boxCustom;

    private TextInfo titleTextInfo;
    private TextInfo messageTextInfo;

    private Notification() {
    }

    public static Notification build(Context context, CharSequence message) {
        synchronized (Notification.class) {
            Notification notification = new Notification();
            notification.log("装载消息通知: " + notification.toString());
            notification.context = new WeakReference<>(context);
            notification.message = message;
            return notification;
        }
    }

    public static Notification build(Context context, int messageResId) {
        synchronized (Notification.class) {
            Notification notification = new Notification();
            notification.context = new WeakReference<>(context);
            notification.message = context.getString(messageResId);
            return notification;
        }
    }

    public static Notification show(Context context, CharSequence message) {
        Notification notification = build(context, message);
        notification.showNotification();
        return notification;
    }

    public static Notification show(Context context, int messageResId) {
        return show(context, context.getString(messageResId));
    }

    public static Notification show(Context context, CharSequence message, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.durationTime = durationTime;
        notification.showNotification();
        return notification;
    }

    public static Notification show(Context context, int messageResId, DURATION_TIME durationTime) {
        return show(context, context.getString(messageResId), durationTime);
    }

    public static Notification show(Context context, CharSequence message, DialogSettings.STYLE style) {
        Notification notification = build(context, message);
        notification.style = style;
        notification.showNotification();
        return notification;
    }

    public static Notification show(Context context, int messageResId, DialogSettings.STYLE style) {
        return show(context, context.getString(messageResId), style);
    }

    public static Notification show(Context context, CharSequence message, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.durationTime = durationTime;
        notification.style = style;
        notification.showNotification();
        return notification;
    }

    public static Notification show(Context context, int messageResId, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        return show(context, context.getString(messageResId), style, durationTime);
    }

    public static Notification show(Context context, CharSequence title, CharSequence message) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.showNotification();
        return notification;
    }

    public static Notification show(Context context, int titleResId, int messageResId) {
        return show(context, context.getString(titleResId), context.getString(messageResId));
    }

    public static Notification show(Context context, CharSequence title, CharSequence message, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.durationTime = durationTime;
        notification.showNotification();
        return notification;
    }

    public static Notification show(Context context, int titleResId, int messageResId, DURATION_TIME durationTime) {
        return show(context, context.getString(titleResId), context.getString(messageResId), durationTime);
    }

    public static Notification show(Context context, CharSequence title, CharSequence message, DialogSettings.STYLE style) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.style = style;
        notification.showNotification();
        return notification;
    }

    public static Notification show(Context context, int titleResId, int messageResId, DialogSettings.STYLE style) {
        return show(context, context.getString(titleResId), context.getString(messageResId), style);
    }

    public static Notification show(Context context, CharSequence title, CharSequence message, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.durationTime = durationTime;
        notification.style = style;
        notification.showNotification();
        return notification;
    }

    public static Notification show(Context context, int titleResId, int messageResId, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        return show(context, context.getString(titleResId), context.getString(messageResId), style, durationTime);
    }

    public static Notification show(Context context, int titleResId, int messageResId, int iconResId) {
        return show(context, context.getString(titleResId), context.getString(messageResId), iconResId);
    }

    public static Notification show(Context context, CharSequence title, CharSequence message, int iconResId, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.iconResId = iconResId;
        notification.durationTime = durationTime;
        notification.showNotification();
        return notification;
    }

    public static Notification show(Context context, int titleResId, int messageResId, int iconResId, DURATION_TIME durationTime) {
        return show(context, context.getString(titleResId), context.getString(messageResId), iconResId, durationTime);
    }

    public static Notification show(Context context, CharSequence title, CharSequence message, int iconResId, DialogSettings.STYLE style) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.iconResId = iconResId;
        notification.style = style;
        notification.showNotification();
        return notification;
    }

    public static Notification show(Context context, int titleResId, int messageResId, int iconResId, DialogSettings.STYLE style) {
        return show(context, context.getString(titleResId), context.getString(messageResId), iconResId, style);
    }

    public static Notification show(Context context, CharSequence title, CharSequence message, int iconResId, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.iconResId = iconResId;
        notification.durationTime = durationTime;
        notification.style = style;
        notification.showNotification();
        return notification;
    }

    public static Notification show(Context context, int titleResId, int messageResId, int iconResId, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        return show(context, context.getString(titleResId), context.getString(messageResId), iconResId, style, durationTime);
    }

    public static Notification show(Context context, CharSequence title, CharSequence message, int iconResId) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.iconResId = iconResId;
        notification.showNotification();
        return notification;
    }

    private boolean isShow;

    public void showNotification() {
//        log("启动消息通知 -> " + toString());
        isShow = true;
        if (style == null) style = DialogSettings.style;
        switch (style) {
            case STYLE_MIUI:
            case STYLE_IOS:
                showIOSNotification();
                break;
            case STYLE_MATERIAL:
                showMaterialNotification();
                break;
            default:
                showKongzueNotification();
                break;
        }
    }

    private void showMaterialNotification() {
        rootView = (NotifyToastShadowView) LayoutScatter.getInstance(context.get()).parse(ResourceTable.Layout_notification_material, null, false);
        boxBody = (DependentLayout) rootView.findComponentById(ResourceTable.Id_box_body);
        boxNotic = (DirectionalLayout) rootView.findComponentById(ResourceTable.Id_box_notic);
        btnNotic = (DirectionalLayout) rootView.findComponentById(ResourceTable.Id_btn_notic);
        txtTitle = (Text) rootView.findComponentById(ResourceTable.Id_txt_title);
        txtMessage = (Text) rootView.findComponentById(ResourceTable.Id_txt_message);
        imgIcon = (Image) rootView.findComponentById(ResourceTable.Id_img_icon);
        boxCustom = (DependentLayout) rootView.findComponentById(ResourceTable.Id_box_custom);

        rootView.setOnNotificationClickListener(() -> {
            if (customView == null) {
                ktoast.cancel();
                if (onNotificationClickListener != null) onNotificationClickListener.onClick();
            }
        });


        if (messageTextInfo == null) {
            messageTextInfo = DialogSettings.contentTextInfo;
        }
        if (titleTextInfo == null) {
            titleTextInfo = DialogSettings.titleTextInfo;
        }

        useTextInfo(txtTitle, titleTextInfo);
        useTextInfo(txtMessage, messageTextInfo);

        btnNotic.setPadding(dip2px(15), getStatusBarHeight() + dip2px(15), dip2px(15), dip2px(15));

        if (isNull(title)) {
            txtTitle.setVisibility(Component.HIDE);
        } else {
            txtTitle.setVisibility(Component.VISIBLE);
            txtTitle.setText(title.toString());
        }

        if (iconResId == 0) {
            imgIcon.setVisibility(Component.HIDE);
        } else {
            imgIcon.setVisibility(Component.VISIBLE);
            if (iconResId != 0) {
                imgIcon.setForeground(getPixelElementByResId(context.get(), iconResId));
            }
        }

        txtMessage.setText(message.toString());

        boxBody.createAnimatorProperty().setCurveType(DECELERATE).moveFromY(-boxBody.getHeight()).moveToY(0).setDuration(800).start();
        ktoast = new kToast(durationTime, onDismissListener, dip2px(100)).show(context.get(), rootView);
    }

    private void showIOSNotification() {
        Log.d(TAG, " showIOSNotification");
        rootView = (NotifyToastShadowView) LayoutScatter.getInstance(context.get()).parse(ResourceTable.Layout_notification_ios, null, false);

        boxBody = (DependentLayout) rootView.findComponentById(ResourceTable.Id_box_body);
        btnNotic = (DirectionalLayout) rootView.findComponentById(ResourceTable.Id_btn_notic);
        boxTitle = (DirectionalLayout) rootView.findComponentById(ResourceTable.Id_box_title);
        imgIcon = (Image) rootView.findComponentById(ResourceTable.Id_img_icon);
        txtTitle = (Text) rootView.findComponentById(ResourceTable.Id_txt_title);
        txtMessage = (Text) rootView.findComponentById(ResourceTable.Id_txt_message);
        boxCustom = (DependentLayout) rootView.findComponentById(ResourceTable.Id_box_custom);

        rootView.setOnNotificationClickListener(() -> {
            if (customView == null) {
                ktoast.cancel();
                if (onNotificationClickListener != null) onNotificationClickListener.onClick();
            }
        });

        if (messageTextInfo == null) {
            messageTextInfo = DialogSettings.contentTextInfo;
        }
        if (titleTextInfo == null) {
            titleTextInfo = DialogSettings.titleTextInfo;
        }

        useTextInfo(txtTitle, titleTextInfo);
        useTextInfo(txtMessage, messageTextInfo);

        if (isNull(title)) {
            txtTitle.setVisibility(Component.HIDE);
        } else {
            txtTitle.setVisibility(Component.VISIBLE);
            txtTitle.setText(title.toString());
        }

        if (iconResId == 0) {
            imgIcon.setVisibility(Component.HIDE);
        } else {
            imgIcon.setVisibility(Component.VISIBLE);
            if (iconResId != 0) {
                imgIcon.setForeground(getPixelElementByResId(context.get(), iconResId));
            }
        }

        txtMessage.setText(message.toString());
        if (isNull(title)) {
            boxTitle.setVisibility(Component.HIDE);
        } else {
            boxTitle.setVisibility(Component.VISIBLE);
        }

        ktoast = new kToast(durationTime, onDismissListener, dip2px(115)).show(context.get(), rootView);
        btnNotic.createAnimatorProperty()
                .setCurveType(DECELERATE)
                .moveFromY(-boxBody.getHeight())
                .moveToY(0)
                .setDuration(800)
                .start();
    }

    private void showKongzueNotification() {
        rootView = (NotifyToastShadowView) LayoutScatter.getInstance(context.get()).parse(ResourceTable.Layout_notification_kongzue, null, false);

        boxBody = (DependentLayout) rootView.findComponentById(ResourceTable.Id_box_body);
        boxNotic = (DirectionalLayout) rootView.findComponentById(ResourceTable.Id_box_notic);
        btnNotic = (DirectionalLayout) rootView.findComponentById(ResourceTable.Id_btn_notic);
        txtTitle = (Text) rootView.findComponentById(ResourceTable.Id_txt_title);
        txtMessage = (Text) rootView.findComponentById(ResourceTable.Id_txt_message);
        imgIcon = (Image) rootView.findComponentById(ResourceTable.Id_img_icon);
        boxCustom = (DependentLayout) rootView.findComponentById(ResourceTable.Id_box_custom);

        rootView.setOnNotificationClickListener(() -> {
                    if (customView == null) {
                        ktoast.cancel();
                        if (onNotificationClickListener != null) onNotificationClickListener.onClick();
                    }
                }
        );


        context.get().getMainTaskDispatcher().asyncDispatch(() -> {
            boxNotic.createAnimatorProperty().setCurveType(DECELERATE).moveFromY(-boxBody.getHeight()).moveToY(0).setDuration(800).start();
        });

        if (messageTextInfo == null) {
            messageTextInfo = DialogSettings.contentTextInfo;
        }
        if (titleTextInfo == null) {
            titleTextInfo = DialogSettings.titleTextInfo;
        }

        btnNotic.setPadding(dip2px(10), getStatusBarHeight(), dip2px(10), 0);

        refreshView();

        ktoast = new kToast(durationTime, onDismissListener, dip2px(80)).show(context.get(), rootView);
    }

    private void refreshView() {
        if (style != DialogSettings.STYLE.STYLE_IOS && style != DialogSettings.STYLE.STYLE_MIUI) {
            if (btnNotic != null) {
                if (backgroundColor == 0)
                    if (style == DialogSettings.STYLE.STYLE_KONGZUE) {
                        backgroundColor = UiUtil.getColor(context.get(), ResourceTable.Color_notificationNormal);
                    } else {
                        backgroundColor = UiUtil.getColor(context.get(), ResourceTable.Color_white);
                    }
                btnNotic.setBackground(UiUtil.getShapeElement(RECTANGLE, backgroundColor, 0.0f));
            }
        }
        if (txtTitle != null) {
            if (isNull(title)) {
                txtTitle.setVisibility(HIDE);
            } else {
                txtTitle.setVisibility(Component.VISIBLE);
                txtTitle.setText(title.toString());
            }
        }
        if (txtMessage != null) {
            txtMessage.setText(message.toString());
            if (isNull(title)) {
                txtMessage.setTextAlignment(TextAlignment.CENTER);
            } else {
                txtMessage.setTextAlignment(TextAlignment.LEFT | TextAlignment.VERTICAL_CENTER);
            }
        }
        if (imgIcon != null) {
            if (iconResId == 0) {
                imgIcon.setVisibility(Component.HIDE);
            } else {
                imgIcon.setVisibility(Component.VISIBLE);
                if (iconResId != 0) {
                    imgIcon.setForeground(getPixelElementByResId(context.get(), iconResId));
                }
            }
        }
        if (boxCustom != null) {
            if (customView != null) {
                boxCustom.removeAllComponents();
                boxCustom.setVisibility(Component.VISIBLE);
                if (customView.getComponentParent() != null && customView.getComponentParent() instanceof ComponentContainer) {
                    customView.getComponentParent().removeComponent(customView);
                }
                boxCustom.addComponent(customView);
                rootView.setDispatchTouchEvent(false);
                if (onBindView != null) onBindView.onBind(this, customView);
            } else {
                boxCustom.setVisibility(Component.HIDE);
                rootView.setDispatchTouchEvent(true);
            }
        }

        useTextInfo(txtTitle, titleTextInfo);
        useTextInfo(txtMessage, messageTextInfo);
    }

    public static void log(Object o) {
        if (DialogSettings.DEBUGMODE) Log.i(">>>", o.toString());
    }

    public static void error(Object o) {
        if (DialogSettings.DEBUGMODE) Log.e(">>>", o.toString());
    }

    private int getStatusBarHeight() {
        return dip2px(20);
    }

    protected boolean isNull(String s) {
        if (s == null || s.length() == 0 || s.trim().isEmpty() || s.equals("null") || s.equals("(null)")) {
            return true;
        }
        return false;
    }

    protected boolean isNull(CharSequence s) {
        if (s == null || s.length() == 0 || s.toString().trim().isEmpty() || s.toString().equals("null") || s.toString().equals("(null)")) {
            return true;
        }
        return false;
    }

    protected void useTextInfo(Text textView, TextInfo textInfo) {
        if (textInfo == null) return;
        if (textView == null) return;
        if (textInfo.getFontSize() > 0) {
            textView.setTextSize(textInfo.getFontSize(), Text.TextSizeType.VP);
        }
        if (textInfo.getFontColor() != 1) {
            textView.setTextColor(new Color(textInfo.getFontColor()));
        }
        if (textInfo.getGravity() != -1) {
            textView.setTextAlignment(textInfo.getGravity());

        }
        Font font = textInfo.isBold() ? Font.DEFAULT_BOLD : Font.SANS_SERIF;
        textView.setFont(font);
    }

    public int dip2px(float dpValue) {
        float scale = 0.0f;
        Optional<Display> displayMetrics = DisplayManager.getInstance().getDefaultDisplay(context.get());
        if (displayMetrics.isPresent()) {
            scale = displayMetrics.get().getAttributes().densityPixels;
        }
        return (int) (dpValue * scale + 0.5f);
    }

    public Notification setOnNotificationClickListener(OnNotificationClickListener onNotificationClickListener) {
        this.onNotificationClickListener = onNotificationClickListener;
        return this;
    }

    public DialogSettings.STYLE getStyle() {
        return style;
    }

    public Notification setStyle(DialogSettings.STYLE style) {
        this.style = style;
        if (isShow) {
            error("必须使用 build(...) 方法创建时，才可以使用 setStyle(...) 来修改通知主题或风格。");
        }
        return this;
    }

    public DURATION_TIME getDurationTime() {
        return durationTime;
    }

    public Notification setDurationTime(DURATION_TIME durationTime) {
        this.durationTime = durationTime;
        if (isShow) {
            error("必须使用 build(...) 方法创建时，才可以使用 setDurationTime(...) 来修改通知持续时间。");
        }
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public Notification setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshView();
        return this;
    }

    public CharSequence getTitle() {
        return title;
    }

    public Notification setTitle(CharSequence title) {
        this.title = title;
        refreshView();
        return this;
    }

    public Notification setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        refreshView();
        return this;
    }

    public CharSequence getMessage() {
        return message;
    }

    public Notification setMessage(CharSequence message) {
        this.message = message;
        refreshView();
        return this;
    }

    public Notification setMessage(int messageResId) {
        this.message = context.get().getString(messageResId);
        refreshView();
        return this;
    }

    public int getIconResId() {
        return iconResId;
    }

    public Notification setIconResId(int iconResId) {
        this.iconResId = iconResId;
        refreshView();
        return this;
    }

    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }

    public Notification setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        refreshView();
        return this;
    }

    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    public Notification setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshView();
        return this;
    }

    public OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }

    public Notification setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public Component getCustomView() {
        return customView;
    }

    public Notification setCustomView(Component customView) {
        this.customView = customView;
        refreshView();
        return this;
    }

    private OnBindView onBindView;

    public Notification setCustomView(int customViewLayoutId, OnBindView onBindView) {
        customView = LayoutScatter.getInstance(context.get()).parse(customViewLayoutId, null, false);
        this.onBindView = onBindView;
        refreshView();
        return this;
    }

    public void dismiss() {
        if (ktoast != null) ktoast.cancel();
    }

    public interface OnBindView {
        void onBind(Notification notification, Component v);
    }

    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
}
