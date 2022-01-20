package com.kongzue.dialog.util;

import com.kongzue.dialog.interfaces.OnBackClickListener;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.multimodalinput.event.KeyEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DialogHelper extends CommonDialog {
    private WeakReference<DialogBase> parent;
    private int layoutId;
    private String parentId;
    private Context mContext;

    private PreviewOnShowListener onShowListener;

    protected OnBackClickListener onBackClickListener;

    public DialogHelper(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    public void dealBackKeyDown() {
        if (onBackClickListener != null) {
            onBackClickListener.onBackClick();
        }
        super.dealBackKeyDown();
    }

    @Override
    public boolean clickKeyDown(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEY_BACK && onBackClickListener != null) {
            onBackClickListener.onBackClick();
            hide();
        }
        return super.clickKeyDown(event);
    }
	
	 @Override
    protected void onDestroy() {
        if (parent == null || parent.get() == null) {
            if (!findMyParent()) {
                return;
            }
        }
        if (parent != null && parent.get().dismissEvent != null) {
            parent.get().dismissEvent.onDismiss();
        }
        super.onDestroy();
        parent.clear();
        parent = null;

    }

    private boolean findMyParent() {
        boolean flag = false;
        List<DialogBase> cache = new ArrayList<>();
        cache.addAll(DialogBase.dialogList);
        DialogBase.newContext = new WeakReference<>(mContext);
        for (DialogBase baseDialog : cache) {
            baseDialog.context = new WeakReference<>(mContext);
            if (baseDialog.toString().equals(parentId)) {
                flag = true;
                parent = new WeakReference<>(baseDialog);
                parent.get().dialog = new WeakReference<>(this);
            }
        }
        return flag;
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    public void hide() {
        super.hide();
    }

    public void setCancelable(boolean bool) {
        parent.get().doDismiss();
    }

    public DialogHelper setLayoutId(DialogBase baseDialog, int layoutId) {
        this.layoutId = layoutId;
        this.parent = new WeakReference<>(baseDialog);
        this.parentId = baseDialog.toString();
        return this;
    }

    public void showDialog(Context context) {
        ComponentContainer rootView = (ComponentContainer) LayoutScatter.getInstance(context)
                .parse(layoutId, null, false);
        findMyParentAndBindView(rootView, context);
        setContentCustomComponent(rootView);
    }

    public void setOnShowListener(PreviewOnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }

    public void setBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    public interface PreviewOnShowListener {
        void onShow(DialogBase dialog);
    }

    private void findMyParentAndBindView(ComponentContainer rootView, Context context) {
        List<DialogBase> cache = new ArrayList<>();
        cache.addAll(DialogBase.dialogList);
        DialogBase.newContext = new WeakReference<>(context);
        for (DialogBase baseDialog : cache) {
            baseDialog.context = new WeakReference<>(context);
            if (baseDialog.toString().equals(parentId)) {
                parent = new WeakReference<>(baseDialog);
                parent.get().dialog = new WeakReference<>(this);
                parent.get().bindView(rootView);
            }
        }
    }
}
