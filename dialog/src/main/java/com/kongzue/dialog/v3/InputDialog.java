package com.kongzue.dialog.v3;

import com.kongzue.dialog.ResourceTable;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.interfaces.OnBackClickListener;
import com.kongzue.dialog.util.DialogBase;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.ResTUtil;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.util.InputInfo;
import ohos.accessibility.ability.AccessibleAbility;
import ohos.accessibility.ability.SoftKeyBoardController;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.InputAttribute;
import ohos.agp.components.TextField;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

import java.lang.ref.WeakReference;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/12 16:42
 */
public class InputDialog extends MessageDialog {

    private String inputText = "";
    private CharSequence hintText;

    private OnInputDialogButtonClickListener onOkButtonClickListener;
    private OnInputDialogButtonClickListener onCancelButtonClickListener;
    private OnInputDialogButtonClickListener onOtherButtonClickListener;
    private OnInputDialogButtonClickListener OnInputDialogButtonClickListener;

    private InputDialog() {
    }

    public static InputDialog build(Context context) {
        synchronized (InputDialog.class) {
            InputDialog inputDialog = new InputDialog();
            inputDialog.context = new WeakReference<>(context);

            inputDialog.style = DialogSettings.style;
            inputDialog.okButtonDrawable = DialogSettings.okButtonDrawable;
            inputDialog.cancelButtonDrawable = DialogSettings.cancelButtonDrawable;
            inputDialog.otherButtonDrawable = DialogSettings.otherButtonDrawable;

            switch (inputDialog.style) {
                case STYLE_IOS:
                    inputDialog.build(inputDialog, ResourceTable.Layout_dialog_select_ios);
                    break;
                case STYLE_KONGZUE:
                    inputDialog.build(inputDialog, ResourceTable.Layout_dialog_select);
                    break;
                case STYLE_MATERIAL:
                    inputDialog.build(inputDialog, ResourceTable.Layout_dialog_select_material);
                    break;
                case STYLE_MIUI:
                    inputDialog.build(inputDialog, ResourceTable.Layout_dialog_select_ios);
                    break;
            }
            return inputDialog;
        }
    }

    public static InputDialog show(Context context, CharSequence title, CharSequence message) {
        synchronized (InputDialog.class) {
            InputDialog inputDialog = show(context, title, message, null, null, null);
            return inputDialog;
        }
    }

    public static InputDialog show(Context context, int titleResId, int messageResId) {
        synchronized (InputDialog.class) {
            InputDialog inputDialog = show(context,
                    context.getString(titleResId),
                    context.getString(messageResId),
                    null, null, null
            );
            return inputDialog;
        }
    }

    public static InputDialog show(Context context, CharSequence title, CharSequence message, CharSequence okButton) {
        synchronized (InputDialog.class) {
            InputDialog inputDialog = show(context, title, message, okButton, null, null);
            return inputDialog;
        }
    }

    public static InputDialog show(Context context, int titleResId, int messageResId, int okButtonResId) {
        synchronized (InputDialog.class) {
            InputDialog inputDialog = show(context,
                    context.getString(titleResId),
                    context.getString(messageResId),
                    context.getString(okButtonResId),
                    null, null
            );
            return inputDialog;
        }
    }

    public static InputDialog show(Context context, CharSequence title, CharSequence message, CharSequence okButton, CharSequence cancelButton) {
        synchronized (InputDialog.class) {
            InputDialog inputDialog = show(context, title, message, okButton, cancelButton, null);
            return inputDialog;
        }
    }

    public static InputDialog show(Context context, int titleResId, int messageResId, int okButtonResId, int cancelButtonResId) {
        synchronized (InputDialog.class) {
            InputDialog inputDialog = show(
                    context,
                    context.getString(titleResId),
                    context.getString(messageResId),
                    context.getString(okButtonResId),
                    context.getString(cancelButtonResId),
                    null
            );
            return inputDialog;
        }
    }

    public static InputDialog show(Context context, CharSequence title, CharSequence message, CharSequence okButton, CharSequence cancelButton, CharSequence otherButton) {
        synchronized (InputDialog.class) {
            InputDialog inputDialog = build(context);

            inputDialog.title = title;
            if (okButton != null) inputDialog.okButton = okButton;
            inputDialog.message = message;
            inputDialog.cancelButton = cancelButton;
            inputDialog.otherButton = otherButton;

            inputDialog.showDialog(context);
            return inputDialog;
        }
    }

    public static InputDialog show(Context context, int titleResId, int messageResId, int okButtonResId, int cancelButtonResId, int otherButtonResId) {
        synchronized (InputDialog.class) {
            InputDialog inputDialog = show(
                    context,
                    context.getString(titleResId),
                    context.getString(messageResId),
                    context.getString(okButtonResId),
                    context.getString(cancelButtonResId),
                    context.getString(otherButtonResId)
            );
            return inputDialog;
        }
    }

    @Override
    public void bindView(ComponentContainer rootView) {
        super.bindView(rootView);

    }

    @Override
    public void refreshView() {
        super.refreshView();

        if (boxInput != null) {
            boxInput.setMaxHeight(dip2px(100));
        }
        if (btnSelectPositive != null) {
            btnSelectPositive.setClickedListener(component -> {
                hideInputKeyboard();
                if (onOkButtonClickListener != null) {
                    if (!onOkButtonClickListener.onClick(getInputText())) {
                        doDismiss();
                    }
                } else {
                    doDismiss();
                }
            });
        }
        if (btnSelectNegative != null) {
            btnSelectNegative.setClickedListener(component -> {
                hideInputKeyboard();
                hideInputKeyboard();
                if (onCancelButtonClickListener != null) {
                    if (!onCancelButtonClickListener.onClick(getInputText().toString())) {
                        doDismiss();
                    }
                } else {
                    doDismiss();
                }
            });
        }
        if (btnSelectOther != null) {
            btnSelectOther.setClickedListener(component -> {
                hideInputKeyboard();
                if (onOtherButtonClickListener != null) {
                    if (!onOtherButtonClickListener.onClick(getInputText().toString())) {
                        doDismiss();
                    }
                } else {
                    doDismiss();
                }
            });
        }
        if (txtInput != null) {
            if (txtInput != null) {
                EventHandler handler = new EventHandler(EventRunner.getMainEventRunner());
                handler.postTask(new Runnable() {
                    @Override
                    public void run() {
                        txtInput.setFocusable(Component.FOCUS_ENABLE);
                        txtInput.setTouchFocusable(true);
                        txtInput.requestFocus();
                        SoftKeyBoardController ime = new SoftKeyBoardController(AccessibleAbility.SHOW_MODE_AUTO, null);
                        ime.setShowMode(AccessibleAbility.SHOW_MODE_AUTO);
                    }
                }, 100);
            }
        }
        refreshComponents();
    }

    private void refreshComponents() {

        if (txtInput != null) {
            txtInput.setText(inputText);
            txtInput.setVisibility(Component.VISIBLE);

            if (theme == DialogSettings.THEME.DARK) {
                txtInput.setTextColor(Color.WHITE);
                txtInput.setHintColor(new Color(ResourceTable.Color_whiteAlpha30));
            }
            if (hintText != null) {
                txtInput.setHint(hintText.toString());
            }
            if (inputInfo != null) {
                int inputType = InputAttribute.PATTERN_PASSWORD | inputInfo.getInputType();
                txtInput.setTextInputType(inputType);
                if (inputInfo.getTextInfo() != null)
                    useTextInfo(txtInput, inputInfo.getTextInfo());
            }
        }
        if (rootLayout != null) {
            switch (style) {
                case STYLE_IOS:
                    ShapeElement shapeElement = new ShapeElement();
                    shapeElement.setRgbColor(new RgbColor(224, 224, 224));
                    splitVertical1.setBackground(shapeElement);
                    splitVertical2.setBackground(shapeElement);
                    splitVertical1.setWidth(1);
                    splitVertical2.setWidth(1);
                    if (DialogSettings.theme == DialogSettings.THEME.DARK) {
                        shapeElement.setRgbColor(new RgbColor(136, 136, 136));
                        splitVertical1.setBackground(shapeElement);
                        splitVertical2.setBackground(shapeElement);
                        txtInput.setTextColor(Color.WHITE);
                    }
                    if (customView != null) {
                        DialogSettings.customInputDialog = false;
                    }
                    break;
                case STYLE_KONGZUE:
                    if (DialogSettings.customInputDialog){
                        boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 920));
                    } else {
                        boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 550));
                    }
                    ShapeElement shapeElementdivider = new ShapeElement();
                    shapeElementdivider.setRgbColor(new RgbColor(224, 224, 224));
                    splitVertical1.setBackground(shapeElementdivider);
                    splitVertical2.setBackground(shapeElementdivider);
                    splitVertical1.setWidth(1);
                    splitVertical1.setVisibility(Component.VISIBLE);
                    splitVertical2.setVisibility(Component.VISIBLE);
                    splitVertical2.setWidth(1);
                    if (DialogSettings.theme == DialogSettings.THEME.DARK) {
                        shapeElementdivider.setRgbColor(new RgbColor(40, 40, 40));
                        splitVertical1.setBackground(shapeElementdivider);
                        splitVertical2.setBackground(shapeElementdivider);
                        txtInput.setTextColor(Color.WHITE);
                    }
                    if (customView != null) {
                        DialogSettings.customInputDialog = false;
                    }
                    break;
                case STYLE_MATERIAL:
                    if (DialogSettings.customInputDialog){
                        boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 1050));
                    } else {
                        boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 640));
                    }
                    ShapeElement shapeElementInput = new ShapeElement();
                    shapeElementInput.setShape(ShapeElement.LINE);
                    shapeElementInput.setStroke(5, new RgbColor(77, 77, 255));
                    txtInput.setBasement(shapeElementInput);
                    boxInput.setPaddingTop(40);
                    boxButton.setAlignment(LayoutAlignment.RIGHT);
                    boxButton.setPaddingRight(100);
                    if (DialogSettings.theme == DialogSettings.THEME.DARK) {
                        txtInput.setTextColor(Color.WHITE);
                    }
                    if (customView != null) {
                        DialogSettings.customInputDialog = false;
                    }
                    break;
                case STYLE_MIUI:
                    if (DialogSettings.customInputDialog){
                        boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 1450));
                    } else {
                        boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 1100));
                    }
                    txtInput.setPaddingTop(40);
                    txtInput.setTextAlignment(TextAlignment.VERTICAL_CENTER);
                    txtInput.setTextSize(50);
                    btnSelectPositive.setMarginTop(80);
                    btnSelectPositive.setTextColor(Color.WHITE);
                    btnSelectNegative.setTextColor(Color.BLACK);
                    btnSelectNegative.setMarginTop(80);
                    btnSelectOther.setMarginTop(80);
                    btnSelectNegative.setMarginLeft(40);
                    btnSelectPositive.setMarginLeft(40);
                    splitHorizontal.setVisibility(Component.HIDE);
                    splitVertical1.setVisibility(Component.HIDE);
                    splitVertical2.setVisibility(Component.HIDE);
                    if (DialogSettings.theme == DialogSettings.THEME.DARK) {
                        txtInput.setTextColor(Color.WHITE);
                    }
                    if (customView != null) {
                        DialogSettings.customInputDialog = false;
                    }
                    break;
                default:
                    break;
            }
        }
        DialogSettings.modalDialog = false;
        DialogSettings.customDialog = false;
        DialogSettings.selectionDialog = false;
        DialogSettings.bottomMenuTitle = false;
    }

    public void hideInputKeyboard() {
        SoftKeyBoardController ime = new SoftKeyBoardController(AccessibleAbility.SHOW_MODE_AUTO, null);
        ime.setShowMode(AccessibleAbility.SHOW_MODE_HIDE);
    }

    public String getInputText() {
        if (txtInput == null) {
            return inputText.toString();
        } else {
            return txtInput.getText().toString();
        }
    }

    //其他设置
    public CharSequence getTitle() {
        return title;
    }

    public InputDialog setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    public InputDialog setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        return this;
    }

    public CharSequence getMessage() {
        return message;
    }

    public InputDialog setMessage(CharSequence content) {
        this.message = content;
        return this;
    }

    public InputDialog setMessage(int contentResId) {
        this.message = context.get().getString(contentResId);
        return this;
    }

    public CharSequence getOkButton() {
        return okButton;
    }

    public InputDialog setOkButton(CharSequence okButton) {
        this.okButton = okButton;
        refreshView();
        return this;
    }

    public InputDialog setOkButton(int okButtonResId) {
        setOkButton(context.get().getString(okButtonResId));
        return this;
    }

    public InputDialog setOkButton(CharSequence okButton, OnInputDialogButtonClickListener
            onOkButtonClickListener) {
        this.okButton = okButton;
        this.onOkButtonClickListener = onOkButtonClickListener;
        this.OnInputDialogButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }

    public InputDialog setOkButton(int okButtonResId, OnInputDialogButtonClickListener
            onOkButtonClickListener) {
        setOkButton(context.get().getString(okButtonResId), onOkButtonClickListener);
        return this;
    }

    public InputDialog setOkButton(OnInputDialogButtonClickListener onOkButtonClickListener) {
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }

    public CharSequence getCancelButton() {
        return cancelButton;
    }

    public InputDialog setCancelButton(CharSequence cancelButton) {
        this.cancelButton = cancelButton;
        refreshView();
        return this;
    }

    public InputDialog setCancelButton(int cancelButtonResId) {
        setCancelButton(context.get().getString(cancelButtonResId));
        return this;
    }

    public InputDialog setCancelButton(CharSequence cancelButton, OnInputDialogButtonClickListener
            onCancelButtonClickListener) {
        this.cancelButton = cancelButton;
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }

    public InputDialog setCancelButton(int cancelButtonResId, OnInputDialogButtonClickListener
            onCancelButtonClickListener) {
        setCancelButton(context.get().getString(cancelButtonResId), onCancelButtonClickListener);
        return this;
    }

    public InputDialog setCancelButton(OnInputDialogButtonClickListener
                                               onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }

    public CharSequence getOtherButton() {
        return otherButton;
    }

    public InputDialog setOtherButton(CharSequence otherButton) {
        this.otherButton = otherButton;
        refreshView();
        return this;
    }

    public InputDialog setOtherButton(int otherButtonResId) {
        setCancelButton(context.get().getString(otherButtonResId));
        refreshView();
        return this;
    }

    public InputDialog setOtherButton(CharSequence otherButton, OnInputDialogButtonClickListener
            onOtherButtonClickListener) {
        this.otherButton = otherButton;
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }

    public InputDialog setOtherButton(int otherButtonResId, OnInputDialogButtonClickListener
            onOtherButtonClickListener) {
        setOtherButton(context.get().getString(otherButtonResId), onOtherButtonClickListener);
        return this;
    }

    public InputDialog setOtherButton(OnInputDialogButtonClickListener
                                              onOtherButtonClickListener) {
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }

    public OnInputDialogButtonClickListener getOnInputOkButtonClickListener() {
        return onOkButtonClickListener;
    }

    @Deprecated
    public OnDialogButtonClickListener getOnOkButtonClickListener() {
        return null;
    }

    public InputDialog setOnOkButtonClickListener(OnInputDialogButtonClickListener
                                                          onOkButtonClickListener) {
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }

    public OnInputDialogButtonClickListener getOnInputCancelButtonClickListener() {
        return onCancelButtonClickListener;
    }

    @Deprecated
    public OnDialogButtonClickListener getOnCancelButtonClickListener() {
        return null;
    }

    public InputDialog setOnCancelButtonClickListener(OnInputDialogButtonClickListener
                                                              onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }

    public OnInputDialogButtonClickListener getOnInputOtherButtonClickListener() {
        return onOtherButtonClickListener;
    }

    @Deprecated
    public OnDialogButtonClickListener getOnOtherButtonClickListener() {
        return null;
    }

    public InputDialog setOnOtherButtonClickListener(OnInputDialogButtonClickListener
                                                             onOtherButtonClickListener) {
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }

    public InputDialog setOkButtonDrawable(int okButtonDrawableResId) {
        this.okButtonDrawable = ResTUtil.getPixelMapDrawable(boxRoot.getContext(), okButtonDrawableResId);
        refreshView();
        return this;
    }

    public InputDialog setOkButtonDrawable(Element okButtonDrawable) {
        this.okButtonDrawable = okButtonDrawable;
        refreshView();
        return this;
    }

    public InputDialog setCancelButtonDrawable(int okButtonDrawableResId) {
        this.cancelButtonDrawable = ResTUtil.getPixelMapDrawable(boxRoot.getContext(), okButtonDrawableResId);
        refreshView();
        return this;
    }

    public InputDialog setCancelButtonDrawable(Element cancelButtonDrawable) {
        this.cancelButtonDrawable = cancelButtonDrawable;
        refreshView();
        return this;
    }

    public InputDialog setOtherButtonDrawable(int okButtonDrawableResId) {
        this.otherButtonDrawable = ResTUtil.getPixelMapDrawable(boxRoot.getContext(), okButtonDrawableResId);
        refreshView();
        return this;
    }

    public InputDialog setOtherButtonDrawable(Element otherButtonDrawable) {
        this.otherButtonDrawable = otherButtonDrawable;
        refreshView();
        return this;
    }

    public int getButtonOrientation() {
        return buttonOrientation;
    }

    public InputDialog setButtonOrientation(int buttonOrientation) {
        this.buttonOrientation = buttonOrientation;
        refreshView();
        return this;
    }

    //其他
    public OnDismissListener getOnDismissListener() {
        return onDismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        } : onDismissListener;
    }

    public InputDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public OnShowListener getOnShowListener() {
        return onShowListener == null ? new OnShowListener() {
            @Override
            public void onShow(DialogBase dialog) {

            }
        } : onShowListener;
    }

    public InputDialog setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }

    public DialogSettings.STYLE getStyle() {
        return style;
    }

    public InputDialog setStyle(DialogSettings.STYLE style) {
        if (isAlreadyShown) {
            return this;
        }

        this.style = style;
        switch (this.style) {
            case STYLE_IOS:
                build(this, ResourceTable.Layout_dialog_select_ios);
                break;
            case STYLE_KONGZUE:
                build(this,  ResourceTable.Layout_dialog_select);
                break;
            case STYLE_MATERIAL:
                build(this, ResourceTable.Layout_dialog_select_material);
                break;
            case STYLE_MIUI:
                build(this, ResourceTable.Layout_dialog_select_miui);
                break;
        }

        return this;
    }

    public DialogSettings.THEME getTheme() {
        return theme;
    }

    public InputDialog setTheme(DialogSettings.THEME theme) {

        if (isAlreadyShown) {
            return this;
        }

        this.theme = theme;
        refreshView();
        return this;
    }

    public boolean getCancelable() {
        return cancelable == Boolean.TRUE;
    }

    public InputDialog setCancelable(boolean enable) {
        this.cancelable = enable ? Boolean.TRUE : Boolean.FALSE;
        if (dialog != null) dialog.get().setCancelable(cancelable == Boolean.TRUE);
        return this;
    }


    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }

    public InputDialog setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        refreshView();
        return this;
    }

    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    public InputDialog setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshView();
        return this;
    }

    public TextInfo getButtonTextInfo() {
        return buttonTextInfo;
    }

    public InputDialog setButtonTextInfo(TextInfo buttonTextInfo) {
        this.buttonTextInfo = buttonTextInfo;
        refreshView();
        return this;
    }

    public TextInfo getButtonPositiveTextInfo() {
        return buttonPositiveTextInfo;
    }

    public InputDialog setButtonPositiveTextInfo(TextInfo buttonPositiveTextInfo) {
        this.buttonPositiveTextInfo = buttonPositiveTextInfo;
        refreshView();
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public InputDialog setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshView();
        return this;
    }

    public InputDialog setInputText(String inputText) {
        this.inputText = inputText;
        refreshView();
        return this;
    }

    public InputDialog setInputText(int inputTextResId) {
        this.inputText = context.get().getString(inputTextResId);
        refreshView();
        return this;
    }

    public CharSequence getHintText() {
        return hintText;
    }

    public InputDialog setHintText(CharSequence hintText) {
        this.hintText = hintText;
        refreshView();
        return this;
    }

    public InputDialog setHintText(int hintTextResId) {
        this.hintText = context.get().getString(hintTextResId);
        refreshView();
        return this;
    }

    public InputInfo getInputInfo() {
        return inputInfo;
    }

    public InputDialog setInputInfo(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
        refreshView();
        return this;
    }

    public Component getCustomView() {
        this.customView = customView;
        refreshView();
        return customView;
    }

    public InputDialog setCustomView(Component customView) {
        this.customView = customView;
        refreshView();
        return this;
    }

    private OnBindView onBindView;

    public InputDialog setCustomView(int customViewLayoutId, OnBindView onBindView) {
        customView = LayoutScatter.getInstance(context.get()).parse(customViewLayoutId, null, false);
        this.onBindView = onBindView;
        refreshView();
        return this;
    }

    public interface OnBindView {
        void onBind(InputDialog dialog, Component v);
    }

    public TextField getEditTextView() {
        return txtInput;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public InputDialog setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
        refreshView();
        return this;
    }

    public InputDialog setCustomDialogStyleId(int customDialogStyleId) {
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

    public InputDialog setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
        return this;
    }

    public ALIGN getAlign() {
        return align;
    }

    public InputDialog setAlign(ALIGN align) {
        this.align = align;
        return this;
    }
}
