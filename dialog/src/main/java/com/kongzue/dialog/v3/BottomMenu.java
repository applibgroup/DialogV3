package com.kongzue.dialog.v3;

import com.kongzue.dialog.ResourceTable;
import com.kongzue.dialog.interfaces.*;
import com.kongzue.dialog.util.DialogBase;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.util.UiUtil;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.text.Font;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kongzue.dialog.util.DialogSettings.menuTitleInfo;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/12 18:33
 */
public class BottomMenu extends DialogBase {

    private List<CharSequence> menuTextList;
    private CharSequence title;
    private CharSequence cancelButtonText = DialogSettings.defaultCancelButtonText;
    private boolean showCancelButton = true;
    protected OnMenuItemClickListener onMenuItemClickListener;
    protected static OnMenuItemClickListener staticOnMenuItemClickListener;
    protected OnDialogButtonClickListener onCancelButtonClickListener;

    private TextInfo menuTitleTextInfo;
    private TextInfo cancelButtonTextInfo;
    private Element cancelButtonDrawable;

    private OnBindView onBindView;
    private DirectionalLayout boxRoot;
    private DirectionalLayout boxBody;
    private DependentLayout boxList;
    private Text txtTitle;
    private DependentLayout boxCustom;
    private Image titleSplitLine;
    private ListContainer listMenu;
    private ComponentContainer boxCancel;
    private Text btnCancel;
    private TextInfo menuTextInfo;
    private Image imgTab;
    private Image imgSplit;
    private DialogListAdapterIOS listAdapterIos;
    private DialogListAdapterMaterial listAdapterMaterial;
    private DialogListAdapteKongzue listAdapterKongzue;
    private DialogListAdapterMIUI listAdapterMIUI;

    private BottomMenu() {
    }

    public static BottomMenu build(Context context) {
        synchronized (BottomMenu.class) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.context = new WeakReference<>(context);

            switch (bottomMenu.style) {
                case STYLE_IOS:
                    bottomMenu.build(bottomMenu, ResourceTable.Layout_bottom_menu_ios);
                    break;
                case STYLE_KONGZUE:
                    bottomMenu.build(bottomMenu, ResourceTable.Layout_bottom_menu_kongzue);
                    break;
                case STYLE_MATERIAL:
                    bottomMenu.build(bottomMenu, ResourceTable.Layout_bottom_menu_material);
                    break;
                case STYLE_MIUI:
                    if (DialogSettings.theme == DialogSettings.THEME.LIGHT) {
                        bottomMenu.build(bottomMenu, ResourceTable.Layout_bottom_menu_miui);
                    } else {
                        bottomMenu.build(bottomMenu, ResourceTable.Layout_bottom_menu_miui_dark);
                    }
                    break;
            }
            return bottomMenu;
        }
    }

    public static BottomMenu show(Context context, List<CharSequence> menuTextList, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        bottomMenu.menuTextList = menuTextList;
        setMenuClickListener(onMenuItemClickListener);
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }

    public static void setMenuClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        new BottomMenu().setMenuClick(onMenuItemClickListener);
    }

    public void setMenuClick(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public static BottomMenu showAsStringList(Context context, List<String> menuTextList, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        bottomMenu.menuTextList = new ArrayList<>();
        bottomMenu.menuTextList.addAll(menuTextList);
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }

    public static BottomMenu show(Context context, CharSequence title, List<CharSequence> menuTextList, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        bottomMenu.menuTextList = menuTextList;
        bottomMenu.title = title;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }

    public static BottomMenu showAsStringList(Context context, CharSequence title, List<String> menuTextList, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        bottomMenu.menuTextList = new ArrayList<>();
        bottomMenu.menuTextList.addAll(menuTextList);
        bottomMenu.title = title;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }

    public static BottomMenu show(Context context, String[] menuTexts, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        List<CharSequence> list = new ArrayList<>();
        list.addAll(Arrays.asList(menuTexts));
        bottomMenu.menuTextList = list;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }

    public static BottomMenu show(Context context, CharSequence[] menuTexts, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        List<CharSequence> list = new ArrayList<>(Arrays.asList(menuTexts));
        bottomMenu.menuTextList = list;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }

    public static BottomMenu show(Context context, CharSequence title, String[] menuTexts, OnMenuItemClickListener onMenuItemClickListener) {
        DialogSettings.bottomMenuTitle = true;
        BottomMenu bottomMenu = build(context);
        List<CharSequence> list = new ArrayList<>();
        list.addAll(Arrays.asList(menuTexts));
        bottomMenu.menuTextList = list;
        bottomMenu.title = title;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }

    public static BottomMenu show(Context context, CharSequence title, CharSequence[] menuTexts, OnMenuItemClickListener onMenuItemClickListener) {
        BottomMenu bottomMenu = build(context);
        List<CharSequence> list = new ArrayList<>(Arrays.asList(menuTexts));
        bottomMenu.menuTextList = list;
        bottomMenu.title = title;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.showDialog();
        return bottomMenu;
    }

    public static BottomMenu show(Context context, CharSequence[] menuTexts, OnMenuItemClickListener onMenuItemClickListener, int customViewLayoutId, OnBindView onBindView) {
        BottomMenu bottomMenu = build(context);
        List<CharSequence> list = new ArrayList<>(Arrays.asList(menuTexts));
        bottomMenu.menuTextList = list;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.customView = LayoutScatter.getInstance(context).parse(customViewLayoutId, null, false);
        bottomMenu.onBindView = onBindView;
        DialogSettings.customDialog = true;
        bottomMenu.showDialog();
        return bottomMenu;
    }

    public static BottomMenu show(Context context, CharSequence title, CharSequence[] menuTexts, OnMenuItemClickListener onMenuItemClickListener, int customViewLayoutId, OnBindView onBindView) {
        BottomMenu bottomMenu = build(context);
        List<CharSequence> list = new ArrayList<>(Arrays.asList(menuTexts));
        bottomMenu.menuTextList = list;
        bottomMenu.title = title;
        bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
        bottomMenu.customView = LayoutScatter.getInstance(context).parse(customViewLayoutId, null, false);
        bottomMenu.onBindView = onBindView;
        DialogSettings.customDialog = true;
        bottomMenu.showDialog();
        return bottomMenu;
    }

    private Component rootView;

    @Override
    public void bindView(ComponentContainer rootView) {
        this.rootView = rootView;
        if (boxCustom != null) boxCustom.removeAllComponents();
        boxRoot = UiUtil.getComponent(rootView, ResourceTable.Id_box_root);
        boxBody = UiUtil.getComponent(rootView, ResourceTable.Id_box_body);
        imgTab = UiUtil.getComponent(rootView, ResourceTable.Id_img_tab);
        boxList = UiUtil.getComponent(rootView, ResourceTable.Id_box_list);
        txtTitle = UiUtil.getComponent(rootView, ResourceTable.Id_txt_title);
        txtTitle.setFont(Font.DEFAULT_BOLD);
        boxCustom = UiUtil.getComponent(rootView, ResourceTable.Id_box_custom);
        titleSplitLine = UiUtil.getComponent(rootView, ResourceTable.Id_title_split_line);
        listMenu = UiUtil.getComponent(rootView, ResourceTable.Id_list_menu);
        listMenu.enableScrollBar(Component.VERTICAL, true);
        boxCancel = UiUtil.getComponent(rootView, ResourceTable.Id_box_cancel);
        btnCancel = UiUtil.getComponent(rootView, ResourceTable.Id_btn_cancel);
        imgSplit = UiUtil.getComponent(rootView, ResourceTable.Id_img_split);

        if (DialogSettings.style == DialogSettings.STYLE.STYLE_IOS) {
            listAdapterIos = new DialogListAdapterIOS(rootView.getContext(), menuTextList);
            listMenu.setItemProvider(listAdapterIos);
        } else if ((DialogSettings.style == DialogSettings.STYLE.STYLE_MATERIAL)) {
            listAdapterMaterial = new DialogListAdapterMaterial(rootView.getContext(), menuTextList);
            listMenu.setItemProvider(listAdapterMaterial);
        } else if ((DialogSettings.style == DialogSettings.STYLE.STYLE_KONGZUE)) {
            listAdapterKongzue = new DialogListAdapteKongzue(rootView.getContext(), menuTextList);
            listMenu.setItemProvider(listAdapterKongzue);
        } else if (DialogSettings.style == DialogSettings.STYLE.STYLE_MIUI) {
            listAdapterMIUI = new DialogListAdapterMIUI(rootView.getContext(), menuTextList);
            listMenu.setItemProvider(listAdapterMIUI);
        }

        refreshView();
    }
    private void textNullCheck(){

        if (cancelButtonTextInfo == null) cancelButtonTextInfo = menuTextInfo;
        if (menuTitleTextInfo == null) menuTitleTextInfo = menuTitleInfo;
        if (menuTextInfo == null) menuTextInfo = DialogSettings.menuTextInfo;
        if (cancelButtonText == null) cancelButtonText = "cancel";
    }

    private void styleIOS(){
        int bkgResId = 0;
        if (theme == DialogSettings.THEME.LIGHT) {
            bkgResId = ResourceTable.Graphic_rect_menu_bkg_ios;
            if (DialogSettings.customDialog == true || DialogSettings.bottomMenuTitle == true) {
                ShapeElement pathItemBackgrouond = new ShapeElement();
                pathItemBackgrouond.setShape(ShapeElement.RECTANGLE);
                pathItemBackgrouond.setRgbColor(new RgbColor(244, 245, 246));
                pathItemBackgrouond.setCornerRadiiArray(new float[]{0, 0, 0, 0, 50, 50, 50, 50});
                listMenu.setBackground(pathItemBackgrouond);
            } else {
                listMenu.setBackground(new ShapeElement(context.get(), bkgResId));
            }
        } else {
            bkgResId = ResourceTable.Graphic_rect_menu_bkg_ios_dark;
            if (DialogSettings.customDialog == true || DialogSettings.bottomMenuTitle == true) {
                ShapeElement pathItemBackgrouond = new ShapeElement();
                pathItemBackgrouond.setShape(ShapeElement.RECTANGLE);
                pathItemBackgrouond.setRgbColor(new RgbColor(38, 38, 38));
                pathItemBackgrouond.setCornerRadiiArray(new float[]{0, 0, 0, 0, 50, 50, 50, 50});
                listMenu.setBackground(pathItemBackgrouond);
            } else {
                listMenu.setBackground(new ShapeElement(context.get(), bkgResId));
            }
        }
        boxCancel.setBackground(new ShapeElement(context.get(), bkgResId));
        boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));
    }

    private void styleMaterial(){
        boxCancel.setVisibility(Component.HIDE);
        boxBody.setVisibility(Component.VISIBLE);
        if (theme == DialogSettings.THEME.LIGHT) {
            ShapeElement pathItemBackgrouond = new ShapeElement();
            pathItemBackgrouond.setShape(ShapeElement.RECTANGLE);
            pathItemBackgrouond.setRgbColor(new RgbColor(244, 245, 246));
            pathItemBackgrouond.setCornerRadiiArray(new float[]{50, 50, 50, 50, 0, 0, 0, 0});
            boxBody.setBackground(pathItemBackgrouond);
        } else {
            ShapeElement pathItemBackgrouond = new ShapeElement();
            pathItemBackgrouond.setShape(ShapeElement.RECTANGLE);
            pathItemBackgrouond.setRgbColor(new RgbColor(38, 38, 38));
            pathItemBackgrouond.setCornerRadiiArray(new float[]{50, 50, 50, 50, 0, 0, 0, 0});
            boxBody.setBackground(pathItemBackgrouond);
        }
        boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));
    }

    private void styleKongzue(){
        if (theme == DialogSettings.THEME.LIGHT) {
            ShapeElement pathItemBackgrouond = new ShapeElement();
            pathItemBackgrouond.setShape(ShapeElement.RECTANGLE);
            pathItemBackgrouond.setRgbColor(new RgbColor(241, 242, 244));
            //    pathItemBackgrouond.setCornerRadiiArray(new float[]{50, 50, 50, 50, 0, 0, 0, 0});
            boxRoot.setBackground(pathItemBackgrouond);

        } else {
            ShapeElement pathItemBackgrouond = new ShapeElement();
            pathItemBackgrouond.setShape(ShapeElement.RECTANGLE);
            pathItemBackgrouond.setRgbColor(new RgbColor(38, 38, 38));
            //   pathItemBackgrouond.setCornerRadiiArray(new float[]{50, 50, 50, 50, 0, 0, 0, 0});
            boxRoot.setBackground(pathItemBackgrouond);
            boxBody.setBackground(pathItemBackgrouond);
            listMenu.setBackground(pathItemBackgrouond);
            btnCancel.setBackground(pathItemBackgrouond);
        }
        boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));
    }

    private void styleSwitch(){

        switch (style) {
            case STYLE_IOS:
                styleIOS();
                break;
            case STYLE_MATERIAL:
                styleMaterial();
                break;
            case STYLE_KONGZUE:
                styleKongzue();
                break;
            default:
                boxRoot.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                break;
        }
    }

    private void lightBoxCustomView(){
        if (!isNull(title)) {
            ShapeElement pathItemBackgrouond = new ShapeElement();
            pathItemBackgrouond.setShape(ShapeElement.RECTANGLE);
            if (DialogSettings.style == DialogSettings.STYLE.STYLE_KONGZUE || DialogSettings.style == DialogSettings.STYLE.STYLE_MIUI) {
                pathItemBackgrouond.setRgbColor(new RgbColor(255, 255, 255));
            } else {
                pathItemBackgrouond.setRgbColor(new RgbColor(244, 245, 246));
            }
            pathItemBackgrouond.setCornerRadiiArray(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
            boxCustom.setBackground(pathItemBackgrouond);
        } else {
            ShapeElement pathItemBackgrouond = new ShapeElement();
            pathItemBackgrouond.setShape(ShapeElement.RECTANGLE);
            if (DialogSettings.style == DialogSettings.STYLE.STYLE_KONGZUE || DialogSettings.style == DialogSettings.STYLE.STYLE_MIUI) {
                pathItemBackgrouond.setRgbColor(new RgbColor(255, 255, 255));
            } else {
                pathItemBackgrouond.setRgbColor(new RgbColor(244, 245, 246));
            }
            pathItemBackgrouond.setCornerRadiiArray(new float[]{50, 50, 50, 50, 0, 0, 0, 0});
            boxCustom.setBackground(pathItemBackgrouond);
        }
    }

    private void darkBoxCustomView(){
        if (!isNull(title)) {
            ShapeElement pathItemBackgrouond = new ShapeElement();
            pathItemBackgrouond.setShape(ShapeElement.RECTANGLE);
            pathItemBackgrouond.setRgbColor(new RgbColor(38, 38, 38));
            pathItemBackgrouond.setCornerRadiiArray(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
            boxCustom.setBackground(pathItemBackgrouond);
        } else {
            ShapeElement pathItemBackgrouond = new ShapeElement();
            pathItemBackgrouond.setShape(ShapeElement.RECTANGLE);
            pathItemBackgrouond.setRgbColor(new RgbColor(38, 38, 38));
            pathItemBackgrouond.setCornerRadiiArray(new float[]{50, 50, 50, 50, 0, 0, 0, 0});
            boxCustom.setBackground(pathItemBackgrouond);
        }
    }

    private void setBoxCustomView(){

        if (customView != null) {
            boxCustom.removeAllComponents();
            if (customView.getComponentParent() != null && customView.getComponentParent() instanceof ComponentContainer) {
                ((ComponentContainer) customView.getComponentParent()).removeComponent(customView);
            }
            boxCustom.addComponent(customView);
            boxCustom.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 320));
            if (onBindView != null) onBindView.onBind(this, customView);
            boxCustom.setVisibility(Component.VISIBLE);
            if (titleSplitLine != null) titleSplitLine.setVisibility(Component.VISIBLE);
            //  final int bkgResId;
            if (theme == DialogSettings.THEME.LIGHT) {
                lightBoxCustomView();
            } else {
                darkBoxCustomView();
            }
        } else {
            boxCustom.setVisibility(Component.HIDE);
        }
    }

    private void setTitleView(){
        if (!isNull(title)) {
            txtTitle.setText(title.toString());
            txtTitle.setVisibility(Component.VISIBLE);
            //   final int bkgResId;
            ShapeElement pathItemBackgrouond = new ShapeElement();
            pathItemBackgrouond.setShape(ShapeElement.RECTANGLE);
            if (DialogSettings.style == DialogSettings.STYLE.STYLE_KONGZUE || DialogSettings.style == DialogSettings.STYLE.STYLE_MIUI) {
                if (theme == DialogSettings.THEME.LIGHT) {
                    pathItemBackgrouond.setRgbColor(new RgbColor(255, 255, 255));
                } else {
                    pathItemBackgrouond.setRgbColor(new RgbColor(38, 38, 38));
                }
            } else {
                if (theme == DialogSettings.THEME.LIGHT) {
                    pathItemBackgrouond.setRgbColor(new RgbColor(244, 245, 246));
                } else {
                    pathItemBackgrouond.setRgbColor(new RgbColor(38, 38, 38));
                }
            }
            pathItemBackgrouond.setCornerRadiiArray(new float[]{50, 50, 50, 50, 0, 0, 0, 0});
            txtTitle.setBackground(pathItemBackgrouond);

            if (titleSplitLine != null) titleSplitLine.setVisibility(Component.VISIBLE);
        }
    }

    @Override
    public void refreshView() {
        textNullCheck();
        styleSwitch();
        setBoxCustomView();
        setTitleView();

        if (rootView != null) {
            btnCancel.setText(cancelButtonText.toString());

            ((ComponentContainer) boxBody.getComponentParent()).setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    doDismiss();
                }
            });

            if (menuTextList == null) {
                menuTextList = new ArrayList<>();
            }
        }

        listMenu.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                if (onMenuItemClickListener != null) {
                    onMenuItemClickListener.onClick(menuTextList.get(i).toString(), i);
                }
                doDismiss();
            }
        });

        btnCancel.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (onCancelButtonClickListener != null) {
                    if (!onCancelButtonClickListener.onClick()) {
                        doDismiss();
                    }
                } else {
                    doDismiss();
                }
            }
        });

        useTextInfo(txtTitle, menuTitleTextInfo);
        useTextInfo(btnCancel, cancelButtonTextInfo);
        DialogSettings.customDialog = false;
        DialogSettings.bottomMenuTitle = false;
    }

    @Override
    public void show(Context context) {
        showDialog();
    }


    public List<CharSequence> getMenuTextList() {
        return menuTextList;
    }

    public CharSequence getTitle() {
        return title;
    }

    public BottomMenu setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        refreshView();
        return this;
    }

    public CharSequence getCancelButtonText() {
        return cancelButtonText;
    }

    public BottomMenu setCancelButtonText(CharSequence cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
        refreshView();
        return this;
    }

    public BottomMenu setCancelButtonText(int cancelButtonTextResId) {
        this.cancelButtonText = context.get().getString(cancelButtonTextResId);
        refreshView();
        return this;
    }

    public OnDismissListener getOnDismissListener() {
        return onDismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO
            }
        } : onDismissListener;
    }

    public BottomMenu setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public BottomMenu setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }

    public interface OnBindView {
        void onBind(BottomMenu bottomMenu, Component v);
    }

    public DialogSettings.STYLE getStyle() {
        return style;
    }

    public OnDialogButtonClickListener getOnCancelButtonClickListener() {
        return onCancelButtonClickListener;
    }

    public BottomMenu setOnCancelButtonClickListener(OnDialogButtonClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        return this;
    }

    public BottomMenu setStyle(DialogSettings.STYLE style) {
        if (isAlreadyShown) {
            return this;
        }

        this.style = style;
        switch (this.style) {
            case STYLE_IOS:
                build(this, ResourceTable.Layout_bottom_menu_ios);
                break;
            case STYLE_KONGZUE:
                build(this, ResourceTable.Layout_bottom_menu_kongzue);
                break;
            case STYLE_MATERIAL:
                build(this, ResourceTable.Layout_bottom_menu_material);
                break;
            case STYLE_MIUI:
                if (DialogSettings.theme == DialogSettings.THEME.LIGHT) {
                    build(this, ResourceTable.Layout_bottom_menu_miui);
                } else {
                    build(this, ResourceTable.Layout_bottom_menu_miui_dark);
                }
                break;
        }

        return this;
    }

    public DialogSettings.THEME getTheme() {
        return theme;
    }

    public BottomMenu setTheme(DialogSettings.THEME theme) {

        if (isAlreadyShown) {
           return this;
        }

        this.theme = theme;
        refreshView();
        return this;
    }

    private float boxBodyOldY;
    private int step;
    private boolean isTouchDown;
    private float touchDownY;

    public BottomMenu setCustomDialogStyleId(int customDialogStyleId) {
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

    public BottomMenu setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
        return this;
    }

    public boolean getCancelable() {
        return cancelable == BOOLEAN.TRUE;
    }

    public BottomMenu setCancelable(boolean enable) {
        this.cancelable = enable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        if (dialog != null) dialog.get().setCancelable(cancelable == BOOLEAN.TRUE);
        return this;
    }

    public class DialogListAdapterIOS extends BaseItemProvider {
        private static final String TAG = "ListItemProvider";

        private Context context;

        private List<CharSequence> itemList;

        public DialogListAdapterIOS(Context context, List<CharSequence> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
            return getRootView(position);
        }

        private Component getRootView(int position) {
            Component rootView = LayoutScatter.getInstance(context)
                    .parse(ResourceTable.Layout_item_bottom_menu_ios, null, false);
            Text deviceName = (Text) rootView.findComponentById(ResourceTable.Id_text);

            String bluetoothDevice = itemList.get(position).toString();
            deviceName.setText(bluetoothDevice);
            deviceName.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    onMenuItemClickListener.onClick(itemList.get(position).toString(),position);
                    doDismiss();
                }
            });
            return rootView;
        }

    }

    public class DialogListAdapterMaterial extends BaseItemProvider {
        private static final String TAG = "ListItemProvider";

        private Context context;

        private List<CharSequence> itemList;

        public DialogListAdapterMaterial(Context context, List<CharSequence> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
            return getRootView(position);
        }

        private Component getRootView(int position) {
            Component rootView = LayoutScatter.getInstance(context)
                    .parse(ResourceTable.Layout_item_bottom_menu_material, null, false);
            Text deviceName = (Text) rootView.findComponentById(ResourceTable.Id_text);

            String bluetoothDevice = itemList.get(position).toString();
            deviceName.setText(bluetoothDevice);
            deviceName.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    doDismiss();
                }
            });
            return rootView;
        }

    }

    public class DialogListAdapteKongzue extends BaseItemProvider {
        private static final String TAG = "ListItemProvider";

        private Context context;

        private List<CharSequence> itemList;

        public DialogListAdapteKongzue(Context context, List<CharSequence> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
            return getRootView(position);
        }

        private Component getRootView(int position) {
            Component rootView = LayoutScatter.getInstance(context)
                    .parse(ResourceTable.Layout_item_bottom_menu_material, null, false);
            Text deviceName = (Text) rootView.findComponentById(ResourceTable.Id_text);

            String bluetoothDevice = itemList.get(position).toString();
            deviceName.setText(bluetoothDevice);
            deviceName.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    doDismiss();
                }
            });
            return rootView;
        }

    }

    public class DialogListAdapterMIUI extends BaseItemProvider {
        private static final String TAG = "ListItemProvider";

        private Context context;

        private List<CharSequence> itemList;

        public DialogListAdapterMIUI(Context context, List<CharSequence> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
            return getRootView(position);
        }

        private Component getRootView(int position) {
            Component rootView = LayoutScatter.getInstance(context)
                    .parse(ResourceTable.Layout_item_bottom_menu_material, null, false);
            Text deviceName = (Text) rootView.findComponentById(ResourceTable.Id_text);

            String bluetoothDevice = itemList.get(position).toString();
            deviceName.setText(bluetoothDevice);
            deviceName.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    doDismiss();
                }
            });
            return rootView;
        }

    }
}
