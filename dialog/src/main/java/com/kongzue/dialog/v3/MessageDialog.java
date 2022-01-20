package com.kongzue.dialog.v3;

import com.kongzue.dialog.ResourceTable;
import com.kongzue.dialog.interfaces.OnBackClickListener;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.util.*;
import com.kongzue.dialog.util.view.MaxHeightLayout;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementScatter;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.text.Font;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;

import static java.util.Objects.isNull;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/29 16:43
 */
public class MessageDialog extends DialogBase {
    protected int buttonOrientation;
    protected Boolean cancelable;
    protected CharSequence title = "Tips";
    protected CharSequence message = "Prompt Message";
    protected CharSequence okButton = "Ok";
    protected CharSequence cancelButton;
    protected CharSequence otherButton;

    protected static boolean contentViewEnabled;

    protected int light_theme = 0;
    protected int dark_theme = 1;

    protected Element okButtonDrawable;
    protected Element cancelButtonDrawable;
    protected Element otherButtonDrawable;

    protected DependentLayout boxRoot;
    protected DependentLayout bkg;
    protected Text txtDialogTitle;
    protected Text txtDialogTip;
    protected DependentLayout boxCustom;
    protected TextField txtInput;
    protected MaxHeightLayout boxInput;
    protected Image splitHorizontal;
    protected DirectionalLayout boxButton;
    private ScrollView scroller;
    protected Text btnSelectNegative;
    protected Image splitVertical1;
    protected Text btnSelectOther;
    protected Image splitVertical2;
    protected Text btnSelectPositive;

    ComponentContainer rootLayout;

    protected OnDialogButtonClickListener onOkButtonClickListener;
    protected OnDialogButtonClickListener onCancelButtonClickListener;
    protected OnDialogButtonClickListener onOtherButtonClickListener;
    protected OnBackClickListener onBackClickListener;
    protected OnDismissListener onDismissListener;
    protected OnDismissListener dismissEvent;
    protected OnShowListener onShowListener;
    protected boolean isAlreadyShown;

    protected TextInfo titleTextInfo;
    protected TextInfo messageTextInfo;
    protected TextInfo tipTextInfo;
    protected TextInfo buttonTextInfo;
    protected TextInfo buttonPositiveTextInfo;
    protected InputInfo inputInfo;
    protected int backgroundColor = 0;
    protected Component customView;
    protected int backgroundResId = -1;
    protected int customDialogStyleId;

    public static MessageDialog build(Context context) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = new MessageDialog();
            messageDialog.style = DialogSettings.style;

            messageDialog.okButtonDrawable = DialogSettings.okButtonDrawable;
            messageDialog.cancelButtonDrawable = DialogSettings.cancelButtonDrawable;
            messageDialog.otherButtonDrawable = DialogSettings.otherButtonDrawable;

            switch (messageDialog.style) {
                case STYLE_IOS:
                    messageDialog.build(messageDialog, ResourceTable.Layout_dialog_select_ios);
                    break;
                case STYLE_KONGZUE:
                    messageDialog.build(messageDialog, ResourceTable.Layout_dialog_select);
                    break;
                case STYLE_MATERIAL:
                    messageDialog.build(messageDialog, ResourceTable.Layout_dialog_select_material);
                    break;
                case STYLE_MIUI:
                    messageDialog.build(messageDialog, ResourceTable.Layout_dialog_select_miui);
                    break;
            }
            return messageDialog;
        }
    }

    public MessageDialog setStyle(DialogSettings.STYLE style) {
        this.style = style;
        switch (this.style) {
            case STYLE_IOS:
                build(this, ResourceTable.Layout_dialog_select_ios);
                break;
            case STYLE_KONGZUE:
                build(this, ResourceTable.Layout_dialog_select);
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

    public static MessageDialog show(Context context, CharSequence title, CharSequence message, CharSequence okButton) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = show(context, title, message, okButton, null, null);
            return messageDialog;
        }
    }

    public static MessageDialog show(Context context, CharSequence title, CharSequence message, CharSequence okButton, CharSequence cancelButton) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = show(context, title, message, okButton, cancelButton, null);
            return messageDialog;
        }
    }

    public static MessageDialog show(Context context, CharSequence title, CharSequence message, CharSequence okButton, CharSequence cancelButton, CharSequence otherButton) {
        synchronized (MessageDialog.class) {
            if (cancelButton != null || otherButton != null) {
                DialogSettings.selectionDialog = true;
            }
            MessageDialog messageDialog = build(context);
            messageDialog.title = title;
            if (okButton != null) messageDialog.okButton = okButton;
            messageDialog.message = message;
            messageDialog.cancelButton = cancelButton;
            messageDialog.otherButton = otherButton;

            messageDialog.showDialog(context);
            return messageDialog;
        }
    }

    protected void showDialog(Context context) {
        if (style == DialogSettings.STYLE.STYLE_IOS) {
            super.showDialog(light_theme, context);
        } else if (style == DialogSettings.STYLE.STYLE_MATERIAL) {
            if (theme == DialogSettings.THEME.LIGHT) {
                super.showDialog(light_theme, context);
            } else {
                super.showDialog(dark_theme, context);
            }
        } else {
            super.showDialog(light_theme, context);
        }
    }

    @Override
    public void bindView(ComponentContainer rootView) {
        prepareDialogView(rootView);
    }

    public void prepareDialogView(ComponentContainer rootview) {
        if (boxCustom != null) boxCustom.removeAllComponents();
        if (rootview != null) {
            rootLayout = rootview;
            boxRoot = UiUtil.getComponent(rootLayout, ResourceTable.Id_box_root);
            bkg = UiUtil.getComponent(rootLayout, ResourceTable.Id_bkg);
            txtDialogTitle = UiUtil.getComponent(rootLayout, ResourceTable.Id_txt_dialog_title);
            txtDialogTip = UiUtil.getComponent(rootLayout, ResourceTable.Id_txt_dialog_tip);
            boxCustom = UiUtil.getComponent(rootLayout, ResourceTable.Id_box_custom);
            txtInput = UiUtil.getComponent(rootLayout, ResourceTable.Id_txt_input);
            splitHorizontal = UiUtil.getComponent(rootLayout, ResourceTable.Id_split_horizontal);
            boxButton = UiUtil.getComponent(rootLayout, ResourceTable.Id_box_button);
            scroller = UiUtil.getComponent(rootLayout, ResourceTable.Id_scroller);
            btnSelectNegative = UiUtil.getComponent(rootLayout, ResourceTable.Id_btn_selectNegative);
            splitVertical1 = UiUtil.getComponent(rootLayout, ResourceTable.Id_split_vertical1);
            btnSelectOther = UiUtil.getComponent(rootLayout, ResourceTable.Id_btn_selectOther);
            splitVertical2 = UiUtil.getComponent(rootLayout, ResourceTable.Id_split_vertical2);
            btnSelectPositive = UiUtil.getComponent(rootLayout, ResourceTable.Id_btn_selectPositive);
            boxInput = UiUtil.getComponent(rootLayout, ResourceTable.Id_box_input);
            refreshView();
        }
    }

    public void refreshView() {
        if (txtDialogTitle != null) {
            if (title == null) {
                txtDialogTitle.setVisibility(Component.INVISIBLE);
            } else {
                txtDialogTitle.setVisibility(Component.VISIBLE);
                txtDialogTitle.setText(title.toString());
                txtDialogTitle.setFont(Font.DEFAULT_BOLD);
            }
        }
        if (txtDialogTip != null) {
            if (message == null) {
                txtDialogTip.setVisibility(Component.INVISIBLE);
            } else {
                txtDialogTip.setVisibility(Component.VISIBLE);
                txtDialogTip.setText(message.toString());
            }
        }

        if (rootLayout != null) {
            switch (style) {
                case STYLE_IOS:
                    if (DialogSettings.theme == DialogSettings.THEME.DARK) {
                        boxRoot.setBackground(ElementScatter.getInstance(boxRoot.getContext()).parse(ResourceTable.Graphic_dialog_dark_shape));
                        txtDialogTitle.setTextColor(Color.WHITE);
                        txtDialogTip.setTextColor(Color.WHITE);
                        ShapeElement shapeElement = new ShapeElement();
                        shapeElement.setRgbColor(new RgbColor(136, 136, 136)); //#888888
                        splitVertical1.setBackground(shapeElement);
                        splitVertical2.setBackground(shapeElement);
                        splitHorizontal.setBackground(shapeElement);
                        txtInput.setBackground(ElementScatter.getInstance(boxRoot.getContext()).parse(ResourceTable.Graphic_editbox_dialog_bkg_ios_dark));
                    }
                    if (!DialogSettings.customDialog) {
                        if (message.length() < 20) {
                            if (btnSelectPositive != null && !isNull(cancelButton) && !isNull(otherButton)) {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(850, 800));
                            } else if ((btnSelectPositive != null && !isNull(cancelButton) && isNull(otherButton))
                                    || (btnSelectPositive != null && isNull(cancelButton) && !isNull(otherButton))) {
                                //  boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(850, 500));
                            } else if (btnSelectPositive != null && isNull(cancelButton) && isNull(otherButton)) {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(850, 400));
                            } else {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(850, 400));
                            }
                        } else {
                            if (btnSelectPositive != null && !isNull(cancelButton) && !isNull(otherButton)) {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(850, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                            } else if ((btnSelectPositive != null && !isNull(cancelButton) && isNull(otherButton))
                                    || (btnSelectPositive != null && isNull(cancelButton) && !isNull(otherButton))) {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(850, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                            } else if (btnSelectPositive != null && isNull(cancelButton) && isNull(otherButton)) {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(850, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                            } else {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(850, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                            }
                        }
                    } else {
                        boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(850, 700));
                    }
                    if ((DialogSettings.selectionDialog == true || DialogSettings.customDialog == true) && (DialogSettings.modalDialog == false)) {
                        scroller.setHeight(220);
                        txtDialogTip.setHeight(200);
                        boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(850, 850));
                        DialogSettings.selectionDialog = false;
                        DialogSettings.customDialog = false;
                    }
                    if (customView != null) {
                        DialogSettings.customDialog = false;
                        DirectionalLayout.LayoutConfig lp = new DirectionalLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
                        boxCustom.removeAllComponents();
                        if (customView.getComponentParent() != null && customView.getComponentParent() instanceof ComponentContainer) {
                            ((ComponentContainer) customView.getComponentParent()).removeComponent(customView);
                        }
                        boxCustom.addComponent(customView, lp);
                        if (onBindView != null) onBindView.onBind(this, customView);
                        boxCustom.setVisibility(Component.VISIBLE);
                    } else {
                        boxCustom.setVisibility(Component.HIDE);
                    }
                    break;
                case STYLE_KONGZUE:
                    boxRoot.setBackground(ElementScatter.getInstance(rootLayout.getContext()).parse(ResourceTable.Graphic_dialog_dark_shape_nocorner_white));
                    if (message.length() < 20) {
                        if (btnSelectPositive != null && !isNull(cancelButton) && !isNull(otherButton)) {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 800));
                            splitHorizontal.setVisibility(Component.VISIBLE);
                            splitVertical1.setVisibility(Component.VISIBLE);
                            splitVertical2.setVisibility(Component.VISIBLE);
                            btnSelectPositive.setBackground(ElementScatter.getInstance(rootLayout.getContext()).parse(ResourceTable.Graphic_button_selectdialog_kongzhue_white));
                        } else if ((btnSelectPositive != null && !isNull(cancelButton) && isNull(otherButton))
                                || (btnSelectPositive != null && isNull(cancelButton) && !isNull(otherButton))) {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 500));
                        } else if (btnSelectPositive != null && isNull(cancelButton) && isNull(otherButton)) {
                            splitHorizontal.setVisibility(Component.HIDE);
                            if (DialogSettings.customDialog) {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 750));
                            } else {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 400));
                            }
                        } else {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 400));
                        }
                    } else {
                        if (btnSelectPositive != null && !isNull(cancelButton) && !isNull(otherButton)) {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                            splitHorizontal.setVisibility(Component.VISIBLE);
                            splitVertical1.setVisibility(Component.VISIBLE);
                            splitVertical2.setVisibility(Component.VISIBLE);
                            btnSelectPositive.setBackground(ElementScatter.getInstance(rootLayout.getContext()).parse(ResourceTable.Graphic_button_selectdialog_kongzhue_white));
                        } else if ((btnSelectPositive != null && !isNull(cancelButton) && isNull(otherButton))
                                || (btnSelectPositive != null && isNull(cancelButton) && !isNull(otherButton))) {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                        } else if (btnSelectPositive != null && isNull(cancelButton) && isNull(otherButton)) {
                            splitHorizontal.setVisibility(Component.HIDE);
                            if (DialogSettings.customDialog) {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 750));
                            } else {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                            }
                        } else {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                        }
                    }
                    if (DialogSettings.theme == DialogSettings.THEME.DARK) {
                        boxRoot.setBackground(ElementScatter.getInstance(rootLayout.getContext()).parse(ResourceTable.Graphic_dialog_dark_shape_nocorner_black));
                        btnSelectPositive.setBackground(ElementScatter.getInstance(rootLayout.getContext()).parse(ResourceTable.Graphic_button_selectdialog_kongzhue_black));
                        btnSelectNegative.setBackground(ElementScatter.getInstance(rootLayout.getContext()).parse(ResourceTable.Graphic_button_selectdialog_kongzhue_black));
                        btnSelectOther.setBackground(ElementScatter.getInstance(rootLayout.getContext()).parse(ResourceTable.Graphic_button_selectdialog_kongzhue_black));
                        txtDialogTitle.setTextColor(Color.WHITE);
                        txtDialogTip.setTextColor(Color.WHITE);
                        btnSelectPositive.setTextColor(Color.WHITE);
                        btnSelectNegative.setTextColor(Color.WHITE);
                        btnSelectOther.setTextColor(Color.WHITE);
                        ShapeElement shapeElement = new ShapeElement();
                        shapeElement.setRgbColor(new RgbColor(0, 0, 0)); //#888888
                        splitVertical1.setBackground(shapeElement);
                        splitVertical2.setBackground(shapeElement);
                        splitHorizontal.setBackground(shapeElement);
                    }
                    if ((DialogSettings.selectionDialog == true || DialogSettings.customDialog == true) && (DialogSettings.modalDialog == false)) {
                        scroller.setHeight(220);
                        txtDialogTip.setHeight(200);
                        boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 870));
                        DialogSettings.selectionDialog = false;
                        DialogSettings.customDialog = false;
                    }
                    if (customView != null) {
                        DialogSettings.customDialog = false;
                        DirectionalLayout.LayoutConfig lp = new DirectionalLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
                        boxCustom.removeAllComponents();
                        if (customView.getComponentParent() != null && customView.getComponentParent() instanceof ComponentContainer) {
                            ((ComponentContainer) customView.getComponentParent()).removeComponent(customView);
                        }
                        boxCustom.addComponent(customView, lp);
                        if (onBindView != null) onBindView.onBind(this, customView);
                        boxCustom.setVisibility(Component.VISIBLE);
                    } else {
                        boxCustom.setVisibility(Component.HIDE);
                    }
                    break;
                case STYLE_MATERIAL:
                    txtDialogTitle.setTextAlignment(TextAlignment.LEFT);
                    boxRoot.setBackground(ElementScatter.getInstance(rootLayout.getContext()).parse(ResourceTable.Graphic_dialog_dark_shape_nocorner_white));
                    if (DialogSettings.theme == DialogSettings.THEME.DARK) {
                        boxRoot.setBackground(ElementScatter.getInstance(rootLayout.getContext()).parse(ResourceTable.Graphic_dialog_dark_shape_nocorner_black));
                        boxRoot.setBackground(ElementScatter.getInstance(boxRoot.getContext()).parse(ResourceTable.Graphic_dialog_dark_shape_nocorner_black));
                        txtDialogTitle.setTextColor(Color.WHITE);
                        txtDialogTip.setTextColor(Color.WHITE);
                    }
                    if (message.length() < 20) {
                        if (btnSelectPositive != null && !isNull(cancelButton) && !isNull(otherButton)) {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 500));
                        } else if ((btnSelectPositive != null && !isNull(cancelButton) && isNull(otherButton))
                                || (btnSelectPositive != null && isNull(cancelButton) && !isNull(otherButton))) {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 500));
                        } else if (btnSelectPositive != null && isNull(cancelButton) && isNull(otherButton)) {
                            boxButton.setAlignment(LayoutAlignment.RIGHT);
                            btnSelectPositive.setMarginRight(80);
                            if (DialogSettings.customDialog) {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 880));
                            } else {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 500));
                            }
                        } else {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 500));
                        }
                    } else {
                        if (btnSelectPositive != null && !isNull(cancelButton) && !isNull(otherButton)) {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                        } else if ((btnSelectPositive != null && !isNull(cancelButton) && isNull(otherButton))
                                || (btnSelectPositive != null && isNull(cancelButton) && !isNull(otherButton))) {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                        } else if (btnSelectPositive != null && isNull(cancelButton) && isNull(otherButton)) {
                            boxButton.setAlignment(LayoutAlignment.RIGHT);
                            btnSelectPositive.setMarginRight(80);
                            if (DialogSettings.customDialog) {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, 880));
                            } else {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                            }
                        } else {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(1050, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                        }
                    }
                    if (customView != null) {
                        DialogSettings.customDialog = false;
                        DirectionalLayout.LayoutConfig lp = new DirectionalLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
                        boxCustom.removeAllComponents();
                        if (customView.getComponentParent() != null && customView.getComponentParent() instanceof ComponentContainer) {
                            ((ComponentContainer) customView.getComponentParent()).removeComponent(customView);
                        }
                        boxCustom.addComponent(customView, lp);
                        if (onBindView != null) onBindView.onBind(this, customView);
                        boxCustom.setVisibility(Component.VISIBLE);
                    } else {
                        boxCustom.setVisibility(Component.HIDE);
                    }
                    break;
                case STYLE_MIUI:
                    if (message.length() <20) {
                        txtDialogTip.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
                        if (btnSelectPositive != null && !isNull(cancelButton) && !isNull(otherButton)) {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 1100));
                        } else if ((btnSelectPositive != null && !isNull(cancelButton) && isNull(otherButton))
                                || (btnSelectPositive != null && isNull(cancelButton) && !isNull(otherButton))) {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 800));
                        } else if (btnSelectPositive != null && isNull(cancelButton) && isNull(otherButton)) {
                            if (DialogSettings.customDialog) {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 1100));
                            } else {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 700));
                            }
                        } else {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 700));
                        }
                    } else {
                        if (scroller != null) {
                            scroller.setHeight(450);
                            txtDialogTip.setHeight(400);
                        }
                        if (btnSelectPositive != null && !isNull(cancelButton) && !isNull(otherButton)) {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 1300));
                        } else if ((btnSelectPositive != null && !isNull(cancelButton) && isNull(otherButton))
                                || (btnSelectPositive != null && isNull(cancelButton) && !isNull(otherButton))) {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 1000));
                        } else if (btnSelectPositive != null && isNull(cancelButton) && isNull(otherButton)) {
                            if (DialogSettings.customDialog) {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 1300));
                            } else {
                                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 1000));
                            }
                        } else {
                            boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 1000));
                        }
                    }
                    boxRoot.setBackground(ElementScatter.getInstance(rootLayout.getContext()).parse(ResourceTable.Graphic_dialog_light_shape));
                    txtInput.setBackground(ElementScatter.getInstance(boxRoot.getContext()).parse(ResourceTable.Graphic_editbox_dialog_bkg_miui_light));
                    btnSelectPositive.setBackground(ElementScatter.getInstance(boxRoot.getContext()).parse(ResourceTable.Graphic_button_selectdialog_miui_blue));
                    btnSelectOther.setBackground(ElementScatter.getInstance(boxRoot.getContext()).parse(ResourceTable.Graphic_button_selectdialog_miui_gray));
                    btnSelectNegative.setBackground(ElementScatter.getInstance(boxRoot.getContext()).parse(ResourceTable.Graphic_button_selectdialog_miui_gray));
                    if (DialogSettings.theme == DialogSettings.THEME.DARK) {
                        boxRoot.setBackground(ElementScatter.getInstance(boxRoot.getContext()).parse(ResourceTable.Graphic_dialog_dark_shape));
                        txtDialogTitle.setTextColor(Color.WHITE);
                        txtDialogTip.setTextColor(Color.WHITE);
                        txtInput.setBackground(ElementScatter.getInstance(boxRoot.getContext()).parse(ResourceTable.Graphic_editbox_dialog_bkg_miui_dark));
                    }
                    if (customView != null) {
                        DialogSettings.customDialog = false;
                        DirectionalLayout.LayoutConfig lp = new DirectionalLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
                        boxCustom.removeAllComponents();
                        if (customView.getComponentParent() != null && customView.getComponentParent() instanceof ComponentContainer) {
                            ((ComponentContainer) customView.getComponentParent()).removeComponent(customView);
                        }
                        boxCustom.addComponent(customView, lp);
                        if (onBindView != null) onBindView.onBind(this, customView);
                        boxCustom.setVisibility(Component.VISIBLE);
                    } else {
                        boxCustom.setVisibility(Component.HIDE);
                    }
                    break;
                default:
                    break;
            }
        }

        if (boxButton != null && (DialogSettings.style != DialogSettings.STYLE.STYLE_MATERIAL)) {
            boxButton.setOrientation(buttonOrientation);
            if (buttonOrientation == LayoutAlignment.VERTICAL_CENTER) {
                //竖排排列的情况下
                boxButton.removeAllComponents();

                if (style == DialogSettings.STYLE.STYLE_IOS) {
                    boxButton.addComponent(btnSelectPositive);
                    boxButton.addComponent(splitVertical2);
                    boxButton.addComponent(btnSelectNegative);
                    boxButton.addComponent(splitVertical1);
                    boxButton.addComponent(btnSelectOther);

                    DirectionalLayout.LayoutConfig lp1 = new DirectionalLayout.LayoutConfig(DirectionalLayout.LayoutConfig.MATCH_PARENT, 1);
                    splitVertical1.setLayoutConfig(lp1);
                    splitVertical2.setLayoutConfig(lp1);
                } else if (style == DialogSettings.STYLE.STYLE_KONGZUE) {
                    boxButton.addComponent(splitHorizontal);
                    boxButton.addComponent(btnSelectPositive);
                    boxButton.addComponent(splitVertical2);
                    boxButton.addComponent(btnSelectNegative);
                    boxButton.addComponent(splitVertical1);
                    boxButton.addComponent(btnSelectOther);

                    splitHorizontal.setMarginTop(10);
                    DirectionalLayout.LayoutConfig lp1 = new DirectionalLayout.LayoutConfig(DirectionalLayout.LayoutConfig.MATCH_PARENT, 1);
                    splitVertical1.setLayoutConfig(lp1);
                    splitVertical2.setLayoutConfig(lp1);
                    splitHorizontal.setLayoutConfig(lp1);
                } else {
                    boxButton.addComponent(btnSelectPositive);
                    boxButton.addComponent(btnSelectNegative);
                    boxButton.addComponent(btnSelectOther);
                }

            }
        }

        if (btnSelectPositive != null) {
            if (DialogSettings.style == DialogSettings.STYLE.STYLE_MATERIAL) {
                btnSelectPositive.setText(okButton.toString().toUpperCase());
            } else {
                btnSelectPositive.setText(okButton.toString());
            }
            if (okButtonDrawable != null) {
                btnSelectPositive.setBackground(okButtonDrawable);
            }

            btnSelectPositive.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    if (onOkButtonClickListener != null) {
                        doDismiss();
                        onOkButtonClickListener.onClick();
                    } else {
                        doDismiss();
                    }
                }
            });
        }
        if (btnSelectNegative != null) {
            if (isNull(cancelButton)) {
                btnSelectNegative.setVisibility(Component.HIDE);
                if (DialogSettings.style == DialogSettings.STYLE.STYLE_IOS) {
                    splitVertical2.setVisibility(Component.HIDE);
                }
            } else {
                if (DialogSettings.style == DialogSettings.STYLE.STYLE_MATERIAL) {
                    btnSelectNegative.setText(cancelButton.toString().toUpperCase());
                } else {
                    btnSelectNegative.setText(cancelButton.toString());
                }
                if (cancelButtonDrawable != null) {
                    btnSelectNegative.setBackground(cancelButtonDrawable);
                }

                btnSelectNegative.setClickedListener(component -> {
                    if (onCancelButtonClickListener != null) {
                        doDismiss();
                    } else {
                        doDismiss();
                    }
                });
            }
        }
        if (btnSelectOther != null) {
            if (isNull(otherButton)) {
                if (DialogSettings.style == DialogSettings.STYLE.STYLE_IOS) {
                    splitVertical1.setVisibility(Component.HIDE);
                }

            } else {
                if (DialogSettings.style == DialogSettings.STYLE.STYLE_IOS) {
                    splitVertical1.setVisibility(Component.VISIBLE);
                }
                btnSelectOther.setVisibility(Component.VISIBLE);
                if (DialogSettings.style == DialogSettings.STYLE.STYLE_MATERIAL) {
                    btnSelectOther.setText(otherButton.toString().toUpperCase());
                } else {
                    btnSelectOther.setText(otherButton.toString());
                }
                if (otherButtonDrawable != null) {
                    btnSelectOther.setBackground(otherButtonDrawable);
                }
            }

            btnSelectOther.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    if (onOtherButtonClickListener != null) {
                        doDismiss();
                    } else {
                        doDismiss();
                    }
                }
            });
        }
    }

    public MessageDialog setButtonOrientation(int buttonOrientation) {
        this.buttonOrientation = buttonOrientation;
        refreshView();
        return this;
    }

    @Override
    public void show(Context context) {
        showDialog(context);
    }

    public CharSequence getTitle() {
        return title;
    }

    public MessageDialog setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    public MessageDialog setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        return this;
    }

    public CharSequence getMessage() {
        return message;
    }

    public MessageDialog setMessage(CharSequence content) {
        this.message = content;
        return this;
    }

    public MessageDialog setMessage(int contentResId) {
        this.message = context.get().getString(contentResId);
        return this;
    }

    public CharSequence getOkButton() {
        return okButton;
    }

    public MessageDialog setOkButton(String okButton) {
        this.okButton = okButton;
        refreshView();
        return this;
    }

    public MessageDialog setOkButton(int okButtonResId) {
        setOkButton(context.get().getString(okButtonResId));
        return this;
    }

    public MessageDialog setOkButton(CharSequence okButton, OnDialogButtonClickListener onOkButtonClickListener) {
        this.okButton = okButton;
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }

    public MessageDialog setOkButton(int okButtonResId, OnDialogButtonClickListener onOkButtonClickListener) {
        setOkButton(context.get().getString(okButtonResId), onOkButtonClickListener);
        return this;
    }

    public MessageDialog setOkButton(OnDialogButtonClickListener onOkButtonClickListener) {
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }

    public CharSequence getCancelButton() {
        return cancelButton;
    }

    public MessageDialog setCancelButton(CharSequence cancelButton) {
        this.cancelButton = cancelButton;
        refreshView();
        return this;
    }

    public MessageDialog setCancelButton(int cancelButtonResId) {
        setCancelButton(context.get().getString(cancelButtonResId));
        return this;
    }

    public MessageDialog setCancelButton(CharSequence cancelButton, OnDialogButtonClickListener onCancelButtonClickListener) {
        this.cancelButton = cancelButton;
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }

    public MessageDialog setCancelButton(int cancelButtonResId, OnDialogButtonClickListener onCancelButtonClickListener) {
        setCancelButton(context.get().getString(cancelButtonResId), onCancelButtonClickListener);
        return this;
    }

    public MessageDialog setCancelButton(OnDialogButtonClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }

    public CharSequence getOtherButton() {
        return otherButton;
    }

    public MessageDialog setOtherButton(CharSequence otherButton) {
        this.otherButton = otherButton;
        refreshView();
        return this;
    }

    public MessageDialog setOtherButton(int otherButtonResId) {
        setOtherButton(context.get().getString(otherButtonResId));
        return this;
    }

    public MessageDialog setOtherButton(CharSequence otherButton, OnDialogButtonClickListener onOtherButtonClickListener) {
        this.otherButton = otherButton;
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }

    public MessageDialog setOtherButton(int otherButtonResId, OnDialogButtonClickListener onOtherButtonClickListener) {
        setOtherButton(context.get().getString(otherButtonResId), onOtherButtonClickListener);
        return this;
    }

    public MessageDialog setOtherButton(OnDialogButtonClickListener onOtherButtonClickListener) {
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }

    public OnDialogButtonClickListener getOnOkButtonClickListener() {
        return onOkButtonClickListener;
    }

    public MessageDialog setOnOkButtonClickListener(OnDialogButtonClickListener onOkButtonClickListener) {
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }

    public OnDialogButtonClickListener getOnCancelButtonClickListener() {
        return onCancelButtonClickListener;
    }

    public MessageDialog setOnCancelButtonClickListener(OnDialogButtonClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }

    public OnDialogButtonClickListener getOnOtherButtonClickListener() {
        return onOtherButtonClickListener;
    }

    public MessageDialog setOnOtherButtonClickListener(OnDialogButtonClickListener onOtherButtonClickListener) {
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }

    public MessageDialog setOkButtonDrawable(Context context, int okButtonDrawableResId) {
        this.okButtonDrawable = ResTUtil.getPixelMapDrawable(context, okButtonDrawableResId);
        refreshView();
        return this;
    }

    public MessageDialog setOkButtonDrawable(Element okButtonDrawable) {
        this.okButtonDrawable = okButtonDrawable;
        refreshView();
        return this;
    }

    public MessageDialog setCancelButtonDrawable(Context context, int okButtonDrawableResId) {
        this.cancelButtonDrawable = ResTUtil.getPixelMapDrawable(context, okButtonDrawableResId);
        refreshView();
        return this;
    }

    public MessageDialog setCancelButtonDrawable(Element cancelButtonDrawable) {
        this.cancelButtonDrawable = cancelButtonDrawable;
        refreshView();
        return this;
    }

    public MessageDialog setOtherButtonDrawable(Context context, int okButtonDrawableResId) {
        this.otherButtonDrawable = ResTUtil.getPixelMapDrawable(context, okButtonDrawableResId);
        refreshView();
        return this;
    }

    public MessageDialog setOtherButtonDrawable(Element otherButtonDrawable) {
        this.otherButtonDrawable = otherButtonDrawable;
        refreshView();
        return this;
    }

    public int getButtonOrientation() {
        return buttonOrientation;
    }

    //其他
    public OnDismissListener getOnDismissListener() {
        return onDismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        } : onDismissListener;
    }

    public MessageDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        onDismissListener(onDismissListener);
        return this;
    }

    public OnShowListener getOnShowListener() {
        return onShowListener == null ? new OnShowListener() {
            @Override
            public void onShow(DialogBase dialog) {

            }
        } : onShowListener;
    }

    public MessageDialog setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }

    public DialogSettings.STYLE getStyle() {
        return style;
    }

    public DialogSettings.THEME getTheme() {
        return theme;
    }

    public MessageDialog setTheme(DialogSettings.THEME theme) {
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

    public MessageDialog setCancelable(boolean enable) {
        this.cancelable = enable ? Boolean.TRUE : Boolean.FALSE;
        if (dialog != null) dialog.get().setCancelable(cancelable == Boolean.TRUE);
        return this;
    }


    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }

    public MessageDialog setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        refreshView();
        return this;
    }

    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }

    public MessageDialog setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshView();
        return this;
    }

    public TextInfo getButtonTextInfo() {
        return buttonTextInfo;
    }

    public MessageDialog setButtonTextInfo(TextInfo buttonTextInfo) {
        this.buttonTextInfo = buttonTextInfo;
        refreshView();
        return this;
    }

    public TextInfo getButtonPositiveTextInfo() {
        return buttonPositiveTextInfo;
    }

    public MessageDialog setButtonPositiveTextInfo(TextInfo buttonPositiveTextInfo) {
        this.buttonPositiveTextInfo = buttonPositiveTextInfo;
        refreshView();
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public MessageDialog setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshView();
        return this;
    }

    public Component getCustomView() {
        return customView;
    }

    public MessageDialog setCustomView(Component customView) {
        this.customView = customView;
        refreshView();
        return this;
    }

    private OnBindView onBindView;

    public MessageDialog setCustomView(int customViewLayoutId, OnBindView onBindView) {
        DialogSettings.customDialog = true;
        customView = LayoutScatter.getInstance(context.get()).parse(customViewLayoutId, null, false);
        this.onBindView = onBindView;
        refreshView();
        return this;
    }

    public interface OnBindView {
        void onBind(MessageDialog dialog, Component v);
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public MessageDialog setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
        refreshView();
        return this;
    }

    public MessageDialog setCustomDialogStyleId(int customDialogStyleId) {
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

    public MessageDialog setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
        return this;
    }

    public ALIGN getAlign() {
        return align;
    }

    public MessageDialog setAlign(ALIGN align) {
        this.align = align;
        return this;
    }
}
