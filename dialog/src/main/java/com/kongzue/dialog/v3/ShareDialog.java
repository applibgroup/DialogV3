package com.kongzue.dialog.v3;

import com.kongzue.dialog.ResourceTable;
import com.kongzue.dialog.interfaces.OnBackClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.util.DialogBase;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.ResTUtil;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.util.view.IOSItemImageView;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ScrollView;
import ohos.agp.components.Text;
import ohos.agp.components.element.ElementScatter;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.agp.window.service.Window;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.image.PixelMap;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/5/10 23:49
 */
public class ShareDialog extends DialogBase {
    private OnItemClickListener onItemClickListener;

    private CharSequence title = "Share";
    private List<Item> items;
    private CharSequence cancelButtonText = DialogSettings.defaultCancelButtonText;

    private TextInfo titleTextInfo;
    private TextInfo itemTextInfo;
    private TextInfo cancelButtonTextInfo;

    private DirectionalLayout boxBody;
    private DependentLayout boxShare;
    private Text txtTitle;
    private DependentLayout boxCustom;
    private Image titleSplitLine;
    private ComponentContainer boxItem;
    private ComponentContainer boxCancel;
    private Text btnCancel;
    private Image imgTab;
    private Image imgSplit;
    private ScrollView boxScroller;

    private ShareDialog() {
    }

    public static ShareDialog build(Context context) {
        synchronized (ShareDialog.class) {
            ShareDialog shareDialog = new ShareDialog();
            shareDialog.log("Load the sharing box: " + shareDialog.toString());
            shareDialog.context = new WeakReference<>(context);
            switch (shareDialog.style) {
                case STYLE_IOS:
                    shareDialog.build(shareDialog, ResourceTable.Layout_dialog_share_ios);
                    break;
                case STYLE_KONGZUE:
                    shareDialog.build(shareDialog, ResourceTable.Layout_dialog_share_kongzue);
                    break;
                case STYLE_MATERIAL:
                    shareDialog.build(shareDialog, ResourceTable.Layout_dialog_share_material);
                    break;
                case STYLE_MIUI:
                    shareDialog.build(shareDialog, ResourceTable.Layout_dialog_share_miui);
                    break;
            }
            return shareDialog;
        }
    }

    public static ShareDialog show(Context context, List<Item> itemList, OnItemClickListener onItemClickListener) {
        ShareDialog shareDialog = build(context);
        shareDialog.items = itemList;
        shareDialog.onItemClickListener = onItemClickListener;
        shareDialog.show(context);
        return shareDialog;
    }

    private Component rootView;

    @Override
    public void bindView(ComponentContainer rootView) {
        this.rootView = rootView;
        if (boxCustom != null) boxCustom.removeAllComponents();
        boxBody = (DirectionalLayout) rootView.findComponentById(ResourceTable.Id_box_body);
        boxScroller = (ScrollView) rootView.findComponentById(ResourceTable.Id_box_scroller);
        boxShare = (DependentLayout) rootView.findComponentById(ResourceTable.Id_box_share);
        txtTitle = (Text) rootView.findComponentById(ResourceTable.Id_txt_title);
        boxCustom = (DependentLayout) rootView.findComponentById(ResourceTable.Id_box_custom);
        titleSplitLine = (Image) rootView.findComponentById(ResourceTable.Id_title_split_line);
        boxItem = (ComponentContainer) rootView.findComponentById(ResourceTable.Id_box_item);
        boxCancel = (ComponentContainer) rootView.findComponentById(ResourceTable.Id_box_cancel);
        btnCancel = (Text) rootView.findComponentById(ResourceTable.Id_btn_cancel);
        imgTab = (Image) rootView.findComponentById(ResourceTable.Id_img_tab);
        imgSplit = (Image) rootView.findComponentById(ResourceTable.Id_img_split);

        Window window;
        switch (style) {
            case STYLE_IOS:
                final int bkgResId;
                if (theme == DialogSettings.THEME.LIGHT) {
                    bkgResId = ResourceTable.Graphic_rect_menu_bkg_ios;
                    ResTUtil.getLineShape(context.get(), 1, ResourceTable.Color_dialogSplitIOSLight);
                    btnCancel.setBackground(ElementScatter.getInstance(context.get()).parse(ResourceTable.Graphic_button_menu_ios_light));
                    titleSplitLine.setBackground(ResTUtil.getLineShape(context.get(), 1, ResourceTable.Color_dialogSplitIOSLight));
                } else {
                    bkgResId = ResourceTable.Graphic_rect_menu_bkg_ios;
                    btnCancel.setBackground(ElementScatter.getInstance(context.get()).parse(ResourceTable.Graphic_button_menu_ios_dark));
                    titleSplitLine.setBackground(ResTUtil.getLineShape(context.get(), 1, ResourceTable.Color_dialogSplitIOSDark));
                }
                boxShare.setBackground(new ShapeElement(context.get(), bkgResId));
                boxCancel.setBackground(new ShapeElement(context.get(), bkgResId));
                break;
            case STYLE_MATERIAL:
                boxBody.setContentPositionY(boxBody.getHeight());
//                new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable() {
//                    @Override
//                    public void run() {
//                        boxBody.createAnimatorProperty().setDuration(600).moveByY(boxBody.getHeight() / 2).start();
//                    }
//                });
                if (theme == DialogSettings.THEME.LIGHT) {
                    boxBody.setBackground(new ShapeElement(context.get(), ResourceTable.Graphic_rect_bottom_dialog));
                    imgTab.setBackground(new ShapeElement(context.get(), ResourceTable.Graphic_rect_share_material_tab));
                    txtTitle.setTextColor(new Color(context.get().getColor(ResourceTable.Color_tipTextColor)));
                } else {
                    boxBody.setBackground(new ShapeElement(context.get(), ResourceTable.Graphic_rect_bottom_dialog_dark));
                    imgTab.setBackground(new ShapeElement(context.get(), ResourceTable.Graphic_rect_share_material_tab_dark));
                    txtTitle.setTextColor(new Color(context.get().getColor(ResourceTable.Color_materialDarkTitleColor)));
                }

                rootView.setClickedListener(v -> doDismiss());
                break;
            case STYLE_KONGZUE:
                if (theme == DialogSettings.THEME.LIGHT) {
                    boxBody.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_menuSplitSpaceKongzue), boxBody.getRight(), boxBody.getBottom()));
                    txtTitle.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_white), txtTitle.getRight(), txtTitle.getBottom()));
                    boxCustom.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_white), boxCustom.getRight(), boxCustom.getBottom()));
                    boxCancel.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_white), boxCancel.getRight(), boxCancel.getBottom()));
                    boxScroller.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_white), boxScroller.getRight(), boxScroller.getBottom()));
                    imgSplit.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_menuSplitSpaceKongzue), imgSplit.getRight(), imgSplit.getBottom()));
                    btnCancel.setTextColor(new Color(context.get().getColor(ResourceTable.Color_dark)));
                    txtTitle.setTextColor(new Color(context.get().getColor(ResourceTable.Color_tipTextColor)));
                } else {
                    boxBody.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_kongzueDarkBkgColor), boxBody.getRight(), boxBody.getBottom()));
                    txtTitle.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_materialDarkBackgroundColor), txtTitle.getRight(), txtTitle.getBottom()));
                    boxCustom.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_materialDarkBackgroundColor), boxCustom.getRight(), boxCustom.getBottom()));
                    boxCancel.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_materialDarkBackgroundColor), boxCancel.getRight(), boxCancel.getBottom()));
                    boxScroller.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_materialDarkBackgroundColor), boxScroller.getRight(), boxScroller.getBottom()));
                    imgSplit.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_kongzueDarkBkgColor), imgSplit.getRight(), imgSplit.getBottom()));
                    btnCancel.setTextColor(new Color(context.get().getColor(ResourceTable.Color_materialDarkTextColor)));
                    btnCancel.setBackground(ResTUtil.getShapeRectElement(context.get().getColor(ResourceTable.Color_dark), btnCancel.getRight(), btnCancel.getBottom()));
                    txtTitle.setTextColor(new Color(context.get().getColor(ResourceTable.Color_materialDarkTitleColor)));
                }
                break;
            case STYLE_MIUI:
                if (theme == DialogSettings.THEME.LIGHT) {
                    boxBody.setBackground(new ShapeElement(context.get(), ResourceTable.Graphic_rect_selectdialog_miui_bkg_light));
                    btnCancel.setBackground(ElementScatter.getInstance(context.get()).parse(ResourceTable.Graphic_button_selectdialog_miui_gray));
                    btnCancel.setTextColor(new Color(context.get().getColor(ResourceTable.Color_dialogButtonMIUITextGray)));
                    txtTitle.setTextColor(new Color(context.get().getColor(ResourceTable.Color_black)));
                } else {
                    boxBody.setBackground(new ShapeElement(context.get(), ResourceTable.Graphic_rect_selectdialog_miui_bkg_dark));
                    btnCancel.setTextColor(new Color(context.get().getColor(ResourceTable.Color_lightgrey)));
                    txtTitle.setTextColor(new Color(context.get().getColor(ResourceTable.Color_lightgrey)));
                }
                break;
        }

        refreshView();
        if (onShowListener != null) onShowListener.onShow(this);
    }

    @Override
    public void refreshView() {
        if (cancelButtonTextInfo == null) cancelButtonTextInfo = buttonTextInfo;
        if (titleTextInfo == null) titleTextInfo = super.titleTextInfo;
        if (itemTextInfo == null) itemTextInfo = messageTextInfo;
        if (cancelButtonText == null) cancelButtonText = "cancel";

        if (rootView != null) {
            switch (style) {
                case STYLE_IOS:
                    if (items != null) {
                        boxItem.removeAllComponents();
                        for (int i = 0; i < items.size(); i++) {
                            final Item item = items.get(i);
                            Component itemView = LayoutScatter.getInstance(context.get()).parse(ResourceTable.Layout_item_share_ios, null, false);

                            final IOSItemImageView imgIcon = (IOSItemImageView) itemView.findComponentById(ResourceTable.Id_img_icon);
                            Text txtLabel = (Text) itemView.findComponentById(ResourceTable.Id_txt_label);
                            imgIcon.setImageBitmap(item.icon);
                            txtLabel.setText(item.getText().toString());
                            txtLabel.setTextColor(Color.BLACK);

//                            if (theme == DialogSettings.THEME.DARK) {
//                                txtLabel.setTextColor(Color.BLACK);
//                            } else {
//                                txtLabel.setTextColor(Color.BLACK);
//                            }

                            final int index = i;
                            itemView.setClickedListener(v -> {
                                if (onItemClickListener != null) {
                                    if (!onItemClickListener.onClick(ShareDialog.this, index, item)) {
                                        doDismiss();
                                    }
                                } else {
                                    doDismiss();
                                }
                            });
                            boxItem.addComponent(itemView);
                        }
                    }
                    break;
                case STYLE_MATERIAL:
                    if (items != null) {
                        boxItem.removeAllComponents();
                        int ht = dip2px(100);
                        int wd = calcWidth();
                        for (int i = 0; i < items.size(); i++) {
                            final Item item = items.get(i);
                            final Component itemView = LayoutScatter.getInstance(context.get()).parse(ResourceTable.Layout_item_share_material, null, false);

                            final Image imgIcon = (Image) itemView.findComponentById(ResourceTable.Id_img_icon);
                            Text txtLabel = (Text) itemView.findComponentById(ResourceTable.Id_txt_label);

                            imgIcon.setBackground(new PixelMapElement(item.getIcon()));
                            txtLabel.setText(item.getText().toString());

                            if (theme == DialogSettings.THEME.DARK) {
                                txtLabel.setTextColor(new Color(context.get().getColor(ResourceTable.Color_materialDarkTextColor)));
                            } else {
                                txtLabel.setTextColor(new Color(context.get().getColor(ResourceTable.Color_black)));
                            }

                            final int index = i;
                            itemView.setClickedListener(v -> {
                                if (onItemClickListener != null) {
                                    if (!onItemClickListener.onClick(ShareDialog.this, index, item)) {
                                        doDismiss();
                                    }
                                } else {
                                    doDismiss();
                                }
                            });
                            itemView.setHeight(ht);
                            itemView.setWidth(wd);
                            boxItem.addComponent(itemView);
                        }
                    }
                    break;
                case STYLE_MIUI:
                case STYLE_KONGZUE:
                    if (items != null) {
                        boxItem.removeAllComponents();
                        int ht = dip2px(100);
                        int wd = calcWidth();
                        for (int i = 0; i < items.size(); i++) {
                            final Item item = items.get(i);
                            final Component itemView = LayoutScatter.getInstance(context.get()).parse(ResourceTable.Layout_item_share_kongzue, null, false);

                            final IOSItemImageView imgIcon = (IOSItemImageView) itemView.findComponentById(ResourceTable.Id_img_icon);
                            Text txtLabel = (Text) itemView.findComponentById(ResourceTable.Id_txt_label);
                            imgIcon.setScaleMode(Image.ScaleMode.ZOOM_CENTER);
                            imgIcon.setBackground(new PixelMapElement(item.getIcon()));
                            txtLabel.setText(item.getText().toString());

                            if (theme == DialogSettings.THEME.DARK) {
                                txtLabel.setTextColor(new Color(context.get().getColor(ResourceTable.Color_materialDarkTextColor)));
                            } else {
                                txtLabel.setTextColor(new Color(context.get().getColor(ResourceTable.Color_black)));
                            }

                            useTextInfo(txtLabel, itemTextInfo);

                            final int index = i;
                            itemView.setClickedListener(v -> {
                                if (onItemClickListener != null) {
                                    if (!onItemClickListener.onClick(ShareDialog.this, index, item)) {
                                        doDismiss();
                                    }
                                } else {
                                    doDismiss();
                                }
                            });
                            itemView.setHeight(ht);
                            itemView.setWidth(wd);
                            boxItem.addComponent(itemView);
                        }
                    }
                    break;
            }

            if (!isNull(title)) {
                txtTitle.setText(title.toString());
                txtTitle.setVisibility(Component.VISIBLE);
                if (titleSplitLine != null) titleSplitLine.setVisibility(Component.VISIBLE);
            }

            if (btnCancel != null) {
                btnCancel.setText(cancelButtonText.toString());
                btnCancel.setClickedListener(v -> doDismiss());
            }
            if (customView != null) {
                boxCustom.removeAllComponents();
                if (customView.getComponentParent() != null && customView.getComponentParent() instanceof ComponentContainer) {
                    customView.getComponentParent().removeComponent(customView);
                }
                boxCustom.addComponent(customView);
                if (onBindView != null) onBindView.onBind(this, customView);
                boxCustom.setVisibility(Component.VISIBLE);
                if (titleSplitLine != null) titleSplitLine.setVisibility(Component.VISIBLE);
            } else {
                boxCustom.setVisibility(Component.HIDE);
            }

            refreshTextViews();
        }
    }

    private int calcWidth(){
        Optional<Display> displayMetrics = DisplayManager.getInstance().getDefaultDisplay(boxItem.getContext());
        return (displayMetrics.get().getAttributes().width - dip2px(30)) / 4;
    }

    @Override
    public void show(Context context) {
        showDialog();
    }

    protected void refreshTextViews() {
        useTextInfo(txtTitle, titleTextInfo);
        useTextInfo(btnCancel, cancelButtonTextInfo);
    }

    public List<Item> getItems() {
        return items;
    }

    public ShareDialog setItems(List<Item> items) {
        this.items = items;
        refreshView();
        return this;
    }

    public ShareDialog addItem(Item item) {
        if (items == null) items = new ArrayList<>();
        items.add(item);
        refreshView();
        return this;
    }

    public static class Item {
        private PixelMap icon;
        private CharSequence text;

        public Item(PixelMap icon, CharSequence text) {
            this.icon = icon;
            this.text = text;
        }

        public Item(Context context, int iconResId, CharSequence text) {
            this.icon = ResTUtil.getPixelMap(context, iconResId).get();
            this.text = text;
        }

        public PixelMap getIcon() {
            return icon;
        }

        public Item setIcon(PixelMap icon) {
            this.icon = icon;
            return this;
        }

        public CharSequence getText() {
            return text;
        }

        public Item setText(CharSequence text) {
            this.text = text;
            return this;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "text='" + text + '\'' +
                    '}';
        }
    }

    //其他设置
    public DialogSettings.STYLE getStyle() {
        return style;
    }

    public ShareDialog setStyle(DialogSettings.STYLE style) {
        if (isAlreadyShown) {
            error("You can use setStyle(...) to modify the dialog theme or style when you must create it using the build(...) method.。");
            return this;
        }

        this.style = style;
        switch (this.style) {
            case STYLE_IOS:
                build(this, ResourceTable.Layout_dialog_share_ios);
                break;
            case STYLE_MIUI:
            case STYLE_KONGZUE:
                build(this, ResourceTable.Layout_dialog_share_kongzue);
                break;
            case STYLE_MATERIAL:
                build(this, ResourceTable.Layout_dialog_share_material);
                break;
        }

        return this;
    }

    public DialogSettings.THEME getTheme() {
        return theme;
    }

    public ShareDialog setTheme(DialogSettings.THEME theme) {

        if (isAlreadyShown) {
            error("You can use setTheme(...) to modify the dialog theme or style when you must create it using the build(...) method.");
            return this;
        }

        this.theme = theme;
        refreshView();
        return this;
    }

    public CharSequence getTitle() {
        return title;
    }

    public ShareDialog setTitle(CharSequence title) {
        this.title = title;
        refreshView();
        return this;
    }

    public ShareDialog setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        refreshView();
        return this;
    }

    public CharSequence getCancelButtonText() {
        return cancelButtonText;
    }

    public ShareDialog setCancelButtonText(CharSequence cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
        refreshView();
        return this;
    }

    public ShareDialog setCancelButtonText(int cancelButtonTextResId) {
        this.cancelButtonText = context.get().getString(cancelButtonTextResId);
        refreshView();
        return this;
    }

    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }

    public ShareDialog setTitleTextInfo(TextInfo menuTitleTextInfo) {
        this.titleTextInfo = menuTitleTextInfo;
        refreshView();
        return this;
    }

    public TextInfo getItemTextInfo() {
        return itemTextInfo;
    }

    public ShareDialog setItemTextInfo(TextInfo itemTextInfo) {
        this.itemTextInfo = itemTextInfo;
        refreshView();
        return this;
    }

    public TextInfo getCancelButtonTextInfo() {
        return cancelButtonTextInfo;
    }

    public ShareDialog setCancelButtonTextInfo(TextInfo cancelButtonTextInfo) {
        this.cancelButtonTextInfo = cancelButtonTextInfo;
        refreshView();
        return this;
    }

    public OnDismissListener getOnDismissListener() {
        return onDismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        } : onDismissListener;
    }

    public ShareDialog setOnDismissListener(OnDismissListener onDismissListener) {
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

    public ShareDialog setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }

    public Component getCustomView() {
        return customView;
    }

    public ShareDialog setCustomView(Component customView) {
        this.customView = customView;
        refreshView();
        return this;
    }

    private OnBindView onBindView;

    public ShareDialog setCustomView(int customViewLayoutId, OnBindView onBindView) {
        customView = LayoutScatter.getInstance(context.get()).parse(customViewLayoutId, null, false);
        this.onBindView = onBindView;
        refreshView();
        return this;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public ShareDialog setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public interface OnBindView {
        void onBind(ShareDialog shareDialog, Component v);
    }

    public interface OnItemClickListener {
        boolean onClick(ShareDialog shareDialog, int index, Item item);
    }

    private int getStatusBarHeight() {
        return dip2px(20);
    }

    public ShareDialog setCustomDialogStyleId(int customDialogStyleId) {
        if (isAlreadyShown) {
            error("You can use setTheme(...) to modify the dialog theme or style when you must create it using the build(...) method.");
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

    public ShareDialog setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
        return this;
    }

    public ALIGN getAlign() {
        return align;
    }

    public ShareDialog setAlign(ALIGN align) {
        this.align = align;
        return this;
    }
}