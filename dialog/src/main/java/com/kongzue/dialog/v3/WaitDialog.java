package com.kongzue.dialog.v3;

import ohos.app.Context;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/9 14:08
 */

public class WaitDialog extends TipDialog {
    private WaitDialog() {
    }

    public static TipDialog show(Context context, String message) {
        return TipDialog.showWait(context, message);
    }

    public static TipDialog show(Context context, int messageResId) {
        return TipDialog.showWait(context, messageResId);
    }

    public WaitDialog setCustomDialogStyleId(int customDialogStyleId) {
        if (isAlreadyShown) {
            return this;
        }
        this.customDialogStyleId = customDialogStyleId;
        return this;
    }

    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
}
