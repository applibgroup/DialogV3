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
package com.kongzue.dialogdemo.slice;

import com.kongzue.dialog.interfaces.*;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.util.Log;
import com.kongzue.dialog.util.ResTUtil;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.*;
import com.kongzue.dialogdemo.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.RadioButton;
import ohos.agp.components.RadioContainer;
import ohos.agp.components.Text;
import ohos.agp.text.Font;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

import java.util.ArrayList;
import java.util.List;

public class MainAbilitySlice extends AbilitySlice {
    private static final String TAG = Notification.class.getSimpleName();
    private static final String CANCEL = "cancel";
    private static final String PROMPT = "prompt";
    private static final String MENU1 = "Menu1";
    private static final String MENU2 = "Menu2";
    private static final String MENU3 = "Menu3";
    private static final String CLICKED = "Clicked：";

    private static final String CUSTOMLAYOUT = "Clicked on Custom Layout";
    ComponentContainer rootLayout;
    Button messageDialog, selectionDialog, waitdialog, waitpromptdialog, input, serialization, closeability, dialogbox, inputbox, showBottomMenuBtn, showBottomMenuWithHeaderBtb, customBottomMenu;
    RadioButton ios, material, kongzue, miui, brightcolor, dark;
    private Text selectstyle;
    private Text selecttheme;
    private Text dialogboxText;
    private Text tips;
    private Text btnNotify;
    private Text btnCustomNotification;
    private Button btnCustomDialog;
    private Text btnShareDialog;
    private Text content;
    private Text demonstartionofotherbuttons;
    private Text notificationText;
    private Text bottommenuText;

    protected RadioContainer rgGroup;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        selectstyle = (Text) findComponentById(ResourceTable.Id_selectstyle);
        selectstyle.setFont(Font.DEFAULT_BOLD);
        selecttheme = (Text) findComponentById(ResourceTable.Id_selecttheme);
        selecttheme.setFont(Font.DEFAULT_BOLD);
        dialogboxText = (Text) findComponentById(ResourceTable.Id_dialogbox);
        dialogboxText.setFont(Font.DEFAULT_BOLD);
        tips = (Text) findComponentById(ResourceTable.Id_tips);
        tips.setFont(Font.DEFAULT_BOLD);
        btnCustomNotification = (Text) findComponentById(ResourceTable.Id_btn_customNotification);
        btnNotify = (Text) findComponentById(ResourceTable.Id_btn_notify);
        btnCustomDialog = (Button) findComponentById(ResourceTable.Id_btn_customDialog);
        btnShareDialog = (Text) findComponentById(ResourceTable.Id_btn_shareDialog);
        content = (Text) findComponentById(ResourceTable.Id_content);

        bottommenuText = (Text) findComponentById(ResourceTable.Id_bottommenu);
        bottommenuText.setFont(Font.DEFAULT_BOLD);
        notificationText = (Text) findComponentById(ResourceTable.Id_notification);
        notificationText.setFont(Font.DEFAULT_BOLD);
        content.setFont(Font.DEFAULT_BOLD);
        messageDialog = (Button) findComponentById(ResourceTable.Id_messagedialog);
        selectionDialog = (Button) findComponentById(ResourceTable.Id_selectiondialog);
        ios = (RadioButton) findComponentById(ResourceTable.Id_ios);
        material = (RadioButton) findComponentById(ResourceTable.Id_material);
        kongzue = (RadioButton) findComponentById(ResourceTable.Id_kongzue);
        miui = (RadioButton) findComponentById(ResourceTable.Id_miui);
        setRadioButtonElement(ios);
        setRadioButtonElement(material);
        setRadioButtonElement(kongzue);
        setRadioButtonElement(miui);
        ios.setChecked(true);
        setUncheckedCheckbox(material);
        material.setChecked(false);
        setUncheckedCheckbox(kongzue);
        kongzue.setChecked(false);
        setUncheckedCheckbox(miui);
        miui.setChecked(false);
        rgGroup = (RadioContainer) findComponentById(ResourceTable.Id_rgGroup);

        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.LIGHT;

        brightcolor = (RadioButton) findComponentById(ResourceTable.Id_light);
        dark = (RadioButton) findComponentById(ResourceTable.Id_dark);
        setRadioButtonElement(brightcolor);
        setRadioButtonElement(dark);
        brightcolor.setChecked(true);
        setUncheckedCheckbox(dark);
        dark.setChecked(false);

        demonstartionofotherbuttons = (Text)findComponentById(ResourceTable.Id_demonstartionofotherfunctions);
        demonstartionofotherbuttons.setFont(Font.DEFAULT_BOLD);

        messageDialog.setClickedListener(component -> {
            MessageDialog.show(getContext(), "Tips", "This is a message", "Ok");
        });

        selectionDialog.setClickedListener(component -> {
            MessageDialog.show(getContext(), "Tips", "This is a message", "Ok", CANCEL, "back")
                    .setButtonOrientation(LayoutAlignment.VERTICAL_CENTER);
        });

        waitdialog = (Button) findComponentById(ResourceTable.Id_waitdialog);
        waitdialog.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                WaitDialog.show(getContext(), "Test")
                        .setOnBackClickListener(new OnBackClickListener() {
                            @Override
                            public boolean onBackClick() {
                                toast("Press return!");
                                return false;
                            }
                        });
                WaitDialog.dismiss(3000);
            }
        });


        waitpromptdialog = (Button) findComponentById(ResourceTable.Id_waitpromptdialog);
        waitpromptdialog.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                WaitDialog.show(getContext(), "Please wait...");
                EventHandler handler = new EventHandler(EventRunner.getMainEventRunner());
                handler.postTask(new Runnable() {
                    @Override
                    public void run() {
                        getUITaskDispatcher().asyncDispatch(() -> {
                            TipDialog.show(getContext(), "success", TipDialog.TYPE.SUCCESS).setOnDismissListener(new OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    //TODO
                                }
                            });
                        });
                    }
                }, 1000);
            }
        });

        input = (Button) findComponentById(ResourceTable.Id_input);
        input.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                InputDialog.build(getContext())
                        .setTitle(PROMPT).setMessage("Please enter the password(123456)")
                        .setInputText("111111")
                        .setOkButton("ok", new OnInputDialogButtonClickListener(){

                            @Override
                            public boolean onClick( String inputStr) {
                                if (inputStr.equals("123456")) {
                                    TipDialog.show(getContext(), "success!", TipDialog.TYPE.SUCCESS);
                                    return false;
                                } else {
                                    TipDialog.show(getContext(), "Incorrect password", TipDialog.TYPE.ERROR);
                                    return true;
                                }
                            }
                        })
                        .setCancelButton(CANCEL)
                        .setHintText("Please enter the password")
                        .setInputInfo(new InputInfo()
                                        .setMAX_LENGTH(6)
                        )
                        .setCancelable(true)
                        .show(getContext());
            }
        });

        serialization = (Button) findComponentById(ResourceTable.Id_serialization);
        serialization.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                DialogSettings.modalDialog = true;
                MessageDialog.build(getContext())
                        .setTitle("Tips")
                        .setMessage("A serialized dialog box, that is, a modal dialog box, displays multiple dialog boxes at a time through code, and displays only one dialog box at a time. After a dialog box is closed, the next dialog box is displayed.")
                        .setOkButton("ok", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick() {
                                return false;
                            }
                        })
                        .show(getContext());
               MessageDialog.show(getContext(), "More",
                        "You cannot close this dialog box by clicking the button on the left. Kongzue Dialog provides callback functions to help you implement the judgment function you want.",
                        "Point", "I can't turn it off.")
                        .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick() {
                                return true;
                            }
                        });
                 MessageDialog.show(getContext(), "Longitudinal arrangement", "If you're using iOS or Kongzue, the buttons can be arranged vertically to provide more options", "ok", CANCEL, "whatelse?")
                        .setButtonOrientation(LayoutAlignment.VERTICAL_CENTER);
                InputDialog.show(getContext(), "Input DialogBox", "You can also show just one button, like this", "ok");
                MessageDialog.show(getContext(), "Tips", "The prompt dialog box is not affected by modalization and can be displayed immediately as the dialog box is displayed", "Give me a hint", CANCEL)
                        .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick() {
                                waitpromptdialog.callOnClick();
                                return true;
                            }
                        }).setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DialogSettings.modalDialog = false;
                    }
                });
            }
        });

        closeability = (Button) findComponentById(ResourceTable.Id_closeability);
        closeability.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                MessageDialog.show(getContext(), "Tips", "When an AlertDialog is displayed, if the ability attached to it is finished, the WindowLeaked error will occur, causing the program to crash. However, Kongzue Dialog does not have this problem. You can click the button at the bottom to start and wait for a few seconds. The ability will be finished. But you won't have any crash issues", "Starts crashing", CANCEL)
                        .setMessageTextInfo(new TextInfo().setGravity(TextAlignment.LEFT))
                        .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick() {
                                WaitDialog.show(getContext(), "Preparing to crash...");
                                WaitDialog.show(getContext(), "Please wait...");
                                EventHandler handler = new EventHandler(EventRunner.getMainEventRunner());
                                handler.postTask(new Runnable() {
                                    @Override
                                    public void run() {
                                        getUITaskDispatcher().asyncDispatch(() -> {
                                            TipDialog.show(getContext(), "success", TipDialog.TYPE.SUCCESS).setOnDismissListener(new OnDismissListener() {
                                                @Override
                                                public void onDismiss() {
                                                  //  jump(MainActivity.class);

                                                    Operation operation = new Intent.OperationBuilder()
                                                            .withBundleName("com.kongzue.dialogdemo.MainAbility")
                                                            .withAbilityName(MainAbilitySlice.class.getSimpleName())
                                                            .build();
                                                    Intent intent = new Intent();
                                                    intent.setOperation(operation);
                                                    startAbility(intent);
                                                    terminateAbility();
                                                }
                                            });
                                        });
                                    }
                                }, 2000);
                                return true;
                            }
                        });
            }
        });

        dialogbox = (Button)findComponentById(ResourceTable.Id_dialogboxcustomize);
        dialogbox.setClickedListener(component -> {
            DialogSettings.customDialog = true;
            MessageDialog.show(getContext(), PROMPT, "This window comes with a custom layout.", "ok")
                    .setCustomView(ResourceTable.Layout_layout_custom, new MessageDialog.OnBindView() {
                        @Override
                        public void onBind(MessageDialog dialog, Component v) {
                            v.setClickedListener(new Component.ClickedListener() {
                                @Override
                                public void onClick(Component component) {
                                    toast(CUSTOMLAYOUT);
                                }
                            });
                        }
                    });
        });

        inputbox = (Button)findComponentById(ResourceTable.Id_inputboxcustomize);
        inputbox.setClickedListener(component -> {
            DialogSettings.customInputDialog = true;
            InputDialog.show(getContext(), PROMPT, "This window comes with a custom layout.", "Got it")
                    .setCustomView(ResourceTable.Layout_layout_custom, new InputDialog.OnBindView() {
                        @Override
                        public void onBind(InputDialog dialog, Component v) {
                            v.setClickedListener(new Component.ClickedListener() {
                                @Override
                                public void onClick(Component component) {
                                    toast(CUSTOMLAYOUT);
                                }
                            });
                        }
                    });
        });


        showBottomMenuBtn = (Button) findComponentById(ResourceTable.Id_btn_showBottomMenu);
        showBottomMenuBtn.setClickedListener(component -> {
            BottomMenu.show(this, new String[]{MENU1, MENU2, MENU3}, new OnMenuItemClickListener() {
                @Override
                public void onClick(String text, int index) {
                    toast(CLICKED + text);
                }
            });
        });

        showBottomMenuWithHeaderBtb = (Button) findComponentById(ResourceTable.Id_btn_showBottomMenuWithHeader);
        showBottomMenuWithHeaderBtb.setClickedListener(component -> {
            BottomMenu.show(this, "Here is the title text", new String[]{MENU1, MENU2, MENU3, "Menu4", "Menu6", "Menu7", "Menu8", "Menu9", "Menu10", "Menu11"}, new OnMenuItemClickListener() {
                @Override
                public void onClick(String text, int index) {
                    toast(CLICKED + text);
                }
            });
        });

        customBottomMenu = (Button) findComponentById(ResourceTable.Id_btn_customizeBottomMenu);
        customBottomMenu.setClickedListener(component -> {
            BottomMenu.show(this, new String[]{MENU1, MENU2, MENU3}, new OnMenuItemClickListener() {
                @Override
                public void onClick(String text, int index) {
                     toast(text);
                }
            }, ResourceTable.Layout_layout_custom, new BottomMenu.OnBindView() {
                @Override
                public void onBind(BottomMenu bottomMenu, Component v) {
                    v.setClickedListener(new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            toast(CUSTOMLAYOUT);
                        }
                    });
                }
            });
        });

        ios.setClickedListener(component -> DialogSettings.style = DialogSettings.STYLE.STYLE_IOS);

        material.setClickedListener(component -> DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL);

        kongzue.setClickedListener(component -> {
            DialogSettings.style = DialogSettings.STYLE.STYLE_KONGZUE;
        });

        miui.setClickedListener(component -> {
            DialogSettings.style = DialogSettings.STYLE.STYLE_MIUI;
        });

        brightcolor.setClickedListener(component -> {
            DialogSettings.theme = DialogSettings.THEME.LIGHT;
                DialogSettings.tipTheme = DialogSettings.THEME.DARK;
        });

        dark.setClickedListener(component -> {
            DialogSettings.theme = DialogSettings.THEME.DARK;
            DialogSettings.tipTheme = DialogSettings.THEME.LIGHT;
        });

        btnCustomNotification.setClickedListener(component -> {
            Notification.show(this, PROMPT, "Prompt Message",
                    ResourceTable.Media_ico_wechat).setCustomView(ResourceTable.Layout_custom_notification_button, (btnCustomNotification, v) -> {
                Text btnReply;
                Text dismiss;

                btnReply = (Text) v.findComponentById(ResourceTable.Id_btn_reply);
                dismiss = (Text) v.findComponentById(ResourceTable.Id_dismiss);

                btnReply.setClickedListener(v1 -> {
                    btnCustomNotification.dismiss();
                });
                dismiss.setClickedListener(v12 -> btnCustomNotification.dismiss());
            });
        });

        btnNotify.setClickedListener(component -> {
            Notification.show(this, PROMPT, "Prompt message", ResourceTable.Media_ico_wechat).setOnNotificationClickListener(new OnNotificationClickListener() {
                @Override
                public void onClick() {
                    //TODO
                }
            }).setOnDismissListener(() -> Log.d(TAG, "The news slipped away."));
        });

        //分享
        btnShareDialog.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component v) {
                List<ShareDialog.Item> itemList = new ArrayList<>();
                if (DialogSettings.style == DialogSettings.STYLE.STYLE_IOS) {
                    itemList.add(new ShareDialog.Item(getContext(), ResourceTable.Media_img_email_ios, "email"));
                    itemList.add(new ShareDialog.Item(getContext(), ResourceTable.Media_img_qq_ios, "QQ"));
                    itemList.add(new ShareDialog.Item(getContext(), ResourceTable.Media_img_wechat_ios, "WeChat"));
                    itemList.add(new ShareDialog.Item(getContext(), ResourceTable.Media_img_weibo_ios, "weibo"));
                    itemList.add(new ShareDialog.Item(getContext(), ResourceTable.Media_img_memorandum_ios, "Add to Memo”"));
                    itemList.add(new ShareDialog.Item(getContext(), ResourceTable.Media_img_remind_ios, "Reminder"));
                } else {
                    itemList.add(new ShareDialog.Item(getContext(), ResourceTable.Media_img_email_material, "email"));
                    itemList.add(new ShareDialog.Item(getContext(), ResourceTable.Media_img_qq_material, "QQ"));
                    itemList.add(new ShareDialog.Item(getContext(), ResourceTable.Media_img_wechat_material, "WeChat"));
                    itemList.add(new ShareDialog.Item(getContext(), ResourceTable.Media_img_weibo_material, "Weibo"));
                    itemList.add(new ShareDialog.Item(getContext(), ResourceTable.Media_img_hangout_material, "RoundChat"));
                    itemList.add(new ShareDialog.Item(getContext(), ResourceTable.Media_img_remind_material, "Keep"));
                }
                ShareDialog.show(getContext(), itemList, (shareDialog, index, item) -> {
                    toast(CLICKED + item.getText());
                    return false;
                });
            }
        });

        //Fully Customize Dialog Box
        btnCustomDialog.setClickedListener(component -> {
            Log.d(TAG, "btnCustomDialog");
            CustomDialog.build(this, ResourceTable.Layout_layout_custom_dialog, new CustomDialog.OnBindView() {
                @Override
                public void onBind(final CustomDialog dialog, Component v) {
                    Image btnOk = (Image) v.findComponentById(ResourceTable.Id_btn_ok);

                    btnOk.setClickedListener(v13 -> {
                        Log.d(TAG, "btnCustomDialog onClick");
                        dialog.doDismiss();
                    });
                }
            })
            .setFullScreen(false)
            .show(getContext());
        });
    }

    public void toast(final Object obj) {
        new ToastDialog(this).setText(obj.toString()).setDuration(1000).show();
    }

    private void setUncheckedCheckbox(RadioButton radioButton) {
        radioButton.setButtonElement(ResTUtil.getPixelMapDrawable(getContext(), ResourceTable.Media_radio_unchecked));
    }

    private void setRadioButtonElement(RadioButton radioButton) {
        radioButton.setCheckedStateChangedListener((absButton, isChecked) -> {
            if (radioButton.isChecked()) {
                radioButton.setButtonElement(ResTUtil.getPixelMapDrawable(getContext(), ResourceTable.Media_radio_checked));
            } else {
                radioButton.setButtonElement(ResTUtil.getPixelMapDrawable(getContext(), ResourceTable.Media_radio_unchecked));
            }
        });
    }
	
}
