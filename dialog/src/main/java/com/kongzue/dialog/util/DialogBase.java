/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kongzue.dialog.util;

import com.kongzue.dialog.interfaces.OnBackClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.CustomDialog;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.ShareDialog;
import com.kongzue.dialog.v3.TipDialog;
import ohos.accessibility.ability.AccessibleAbility;
import ohos.accessibility.ability.SoftKeyBoardController;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Text;
import ohos.agp.text.Font;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class DialogBase {
    private DialogBase dialogBase;
    private int layoutId;
    private int styleId;
    protected Component customView;
    protected TextInfo buttonTextInfo;
    protected TextInfo titleTextInfo;
    protected TextInfo messageTextInfo;
    protected TextInfo tipTextInfo;
    protected InputInfo inputInfo;
    protected TextInfo buttonPositiveTextInfo;
    public WeakReference<Context> context;
    public WeakReference<DialogHelper> dialog;

    protected static List<DialogBase> dialogList = new ArrayList<>();

    protected static WeakReference<Context> newContext;

    protected DialogSettings.STYLE style;
    protected DialogSettings.THEME theme;

    protected boolean dismissedFlag = false;

    protected ALIGN align = ALIGN.DEFAULT;

    protected int layoutAlign = LayoutAlignment.CENTER;

    protected OnDismissListener onDismissListener;
    protected OnDismissListener dismissEvent;
    protected OnShowListener onShowListener;
    protected OnBackClickListener onBackClickListener;
    protected int customDialogStyleId;                                          //Dialog style resource file
    protected int backgroundColor = 0;

    public boolean isShow;

    protected boolean isAlreadyShown;
    private int screen_width;
    private int screen_height;

    protected enum BOOLEAN {
        NULL, FALSE, TRUE
    }

    public enum ALIGN {
        DEFAULT,
        TOP,
        BOTTOM
    }

    protected BOOLEAN cancelable;

    public DialogBase() {
        initDefaultSettings();
    }

    public void log(Object o) {
        if (DialogSettings.DEBUGMODE) Log.i(">>>", o.toString());
    }

    public void error(Object o) {
        if (DialogSettings.DEBUGMODE) Log.e(">>>", o.toString());
    }

    public DialogBase build(DialogBase dialogBase, int layoutId) {
        this.dialogBase = dialogBase;
        this.layoutId = layoutId;
        if ((style == DialogSettings.STYLE.STYLE_MIUI && dialogBase instanceof MessageDialog)
                || dialogBase instanceof ShareDialog
                || dialogBase instanceof BottomMenu) {
            align = ALIGN.BOTTOM;
        } else {
            align = ALIGN.DEFAULT;
        }
        return dialogBase;
    }

    protected boolean isNull(CharSequence s) {
        if (s == null || s.length() == 0 || s.toString().trim().isEmpty() || s.toString().equals("null") || s.toString().equals("(null)")) {
            return true;
        }
        return false;
    }

    public DialogBase build(DialogBase dialogBase) {
        this.dialogBase = dialogBase;
        this.layoutId = -1;
        return dialogBase;
    }

    protected void showDialog() {
        showDialog(-1, context.get());
    }

    protected void showDialog(int style, Context context) {
        if (isAlreadyShown) {
            return;
        }
        isAlreadyShown = true;
        dismissedFlag = false;
        if (DialogSettings.dialogLifeCycleListener != null)
            DialogSettings.dialogLifeCycleListener.onCreate(this);
        styleId = style;
        dismissEvent = new OnDismissListener() {
            @Override
            public void onDismiss() {
                dismissEvent();
                dismissedFlag = true;
                isShow = false;
                dialogList.remove(dialogBase);
                if (!(dialogBase instanceof TipDialog)) showNext(context);
                if (onDismissListener != null) onDismissListener.onDismiss();
                if (DialogSettings.dialogLifeCycleListener != null)
                    DialogSettings.dialogLifeCycleListener.onDismiss(dialogBase);
            }
        };
        dialogList.add(this);
        if (!DialogSettings.modalDialog) {
            showNow(context);
        } else {
            if (dialogBase instanceof TipDialog) {
                showNow(context);
            } else {
                showNext(context);
            }
        }
    }

    protected void dismissEvent() {
    }

    protected void showNext(Context context) {
        List<DialogBase> cache = new ArrayList<>();
        cache.addAll(DialogBase.dialogList);
        for (DialogBase dialog : dialogList) {
            if (!(dialog instanceof TipDialog)) {
                if (dialog.isShow) {
                    return;
                }
            }
        }
        for (DialogBase dialog : dialogList) {
            if (!(dialog instanceof TipDialog)) {
                dialog.showNow(context);
                return;
            }
        }
    }

    public void onDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;

    }

    public void onBackPressed(OnBackClickListener onBackClickListener) {
        dialog.get().setBackClickListener(onBackClickListener);
    }

    private void showNow(Context context) {
	    isShow = true;
        switch (align) {
            case TOP:
                layoutAlign = LayoutAlignment.TOP;
                break;
            case BOTTOM:
                layoutAlign = LayoutAlignment.BOTTOM;
                break;
            default:
                layoutAlign = LayoutAlignment.CENTER;
                break;
        }
        dialog = new WeakReference<>(new DialogHelper(context).setLayoutId(dialogBase, layoutId));
        dialog.get().showDialog(context);
        dialog.get().setSize(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
        if (dialogBase instanceof BottomMenu) {
            dialog.get().setTransparent(true);
            dialog.get().setAlignment(layoutAlign).setAutoClosable(true).show();
        } else if ((dialogBase instanceof InputDialog)) {
            if (DialogSettings.style == DialogSettings.STYLE.STYLE_IOS) {
                if (DialogSettings.customInputDialog) {
                    dialog.get().setSize(850, 850);
                } else {
                    dialog.get().setSize(850, ComponentContainer.LayoutConfig.MATCH_CONTENT);
                }
                dialog.get().setAlignment(layoutAlign).setOffset(0, -100);
                dialog.get().setCornerRadius(50).setAutoClosable(true).show();
            } else {
                dialog.get().setAlignment(layoutAlign).setOffset(0, -100);
                dialog.get().setAutoClosable(true).setCornerRadius(50).show();
            }
        } else if ((dialogBase instanceof MessageDialog) && ((DialogSettings.style == DialogSettings.STYLE.STYLE_IOS)
                || (DialogSettings.style == DialogSettings.STYLE.STYLE_MIUI))) {
            dialog.get().setAlignment(layoutAlign).setCornerRadius(50).setAutoClosable(true).show();
        } else if (dialogBase instanceof TipDialog) {
            dialog.get().setAlignment(layoutAlign).setCornerRadius(80).show();
        } else if (DialogSettings.style == DialogSettings.STYLE.STYLE_MIUI && !(dialogBase instanceof ShareDialog) && !(dialogBase instanceof CustomDialog)) {
            dialog.get().setSize(ComponentContainer.LayoutConfig.MATCH_PARENT, 1120);
            dialog.get().setAlignment(layoutAlign).setAutoClosable(true).show();
        } else if (dialogBase instanceof ShareDialog){
            dialog.get().getContentCustomComponent().addDrawTask((component, canvas) -> dialog.get().setSize(component.getWidth(), component.getHeight()+120));
            dialog.get().setAlignment(layoutAlign).setTransparent(true).setAutoClosable(true).show();
        } else {
            dialog.get().setAlignment(layoutAlign).setTransparent(true).setAutoClosable(true).show();
        }
    }

    public static void showKeyboard(DialogHelper dialog) {
        new EventHandler(EventRunner.create()).postTask(new Runnable() {
            @Override
            public void run() {

                SoftKeyBoardController ime = new SoftKeyBoardController(AccessibleAbility.SHOW_MODE_AUTO, null);
                ime.setShowMode(AccessibleAbility.SHOW_MODE_AUTO);
            }
        });
    }

    protected void initDefaultSettings() {
        if (theme == null) theme = DialogSettings.theme;
        if (style == null) style = DialogSettings.style;
        if (backgroundColor == 0) backgroundColor = DialogSettings.backgroundColor;
        if (titleTextInfo == null) titleTextInfo = DialogSettings.titleTextInfo;
        if (messageTextInfo == null) messageTextInfo = DialogSettings.contentTextInfo;
        if (tipTextInfo == null) tipTextInfo = DialogSettings.tipTextInfo;
        if (buttonTextInfo == null) buttonTextInfo = DialogSettings.buttonTextInfo;
        if (inputInfo == null) inputInfo = DialogSettings.inputInfo;
        if (buttonPositiveTextInfo == null) {
            if (DialogSettings.buttonPositiveTextInfo == null) {
                buttonPositiveTextInfo = buttonTextInfo;
            } else {
                buttonPositiveTextInfo = DialogSettings.buttonPositiveTextInfo;
            }
        }
    }

    protected void useTextInfo(Text textView, TextInfo textInfo) {
        if (textInfo == null) return;
        if (textView == null) return;
        if (textInfo.getFontSize() > 0) {
            textView.setTextSize(textInfo.getFontSize());
        }
        if (textInfo.getFontColor() != 1) {
            textView.setTextColor(new Color(textInfo.getFontColor()));
        }
        if (textInfo.getGravity() != -1) {
            textView.setTextAlignment(textInfo.getGravity());
        }
        Font font = Font.SANS_SERIF;
        textView.setFont(font);
    }

    public abstract void bindView(ComponentContainer rootView);

    public abstract void refreshView();

    public abstract void show(Context context);

    public void doDismiss() {
        dismissedFlag = true;
        if (dialog != null && dialog.get() != null) {
            dialog.get().destroy();
        }
    }

    protected int dip2px(float dpValue) {
        Optional<Display> optionalDisplay = DisplayManager.getInstance().getDefaultDisplay(context.get());
        Display display = null;
        float density = 0;
        if (optionalDisplay.isPresent()) {
            display = optionalDisplay.get();
        }

        if (display != null && display.getAttributes() != null) {
            density = display.getAttributes().densityPixels;
        }
        if (density == 0) {
            return (int) dpValue;
        }
        return (int) (dpValue * density + 0.5f);
    }

    private void getScreenSize() {
        Optional<Display> optionalDisplay = DisplayManager.getInstance().getDefaultDisplay(context.get());
        screen_width = optionalDisplay.get().getAttributes().width;
        screen_height = optionalDisplay.get().getAttributes().height;
    }
}
