package com.kongzue.dialog.interfaces;

import com.kongzue.dialog.util.DialogBase;
import ohos.agp.window.dialog.BaseDialog;

public interface DialogLifeCycleListener {
    void onCreate(DialogBase dialog);

    void onShow(DialogBase dialog);

    void onDismiss(DialogBase dialog);

}
