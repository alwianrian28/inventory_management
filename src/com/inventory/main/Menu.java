package com.inventory.main;

import com.formdev.flatlaf.FlatClientProperties;
import com.inventory.form.FormBarang;
import com.inventory.form.FormBarangKeluar;
import com.inventory.form.FormBarangMasuk;
import com.inventory.form.FormDashboard;
import com.inventory.form.FormGudang;
import com.inventory.form.FormImages;
import com.inventory.form.FormKategori;
import com.inventory.form.FormMerk;
import com.inventory.form.FormRak;
import com.inventory.form.FormReportBarangKeluar;
import com.inventory.form.FormReportBarangMasuk;
import com.inventory.form.FormReportOpname;
import com.inventory.form.FormReportStok;
import com.inventory.form.FormSatuan;
import com.inventory.form.FormStokOpname;
import com.inventory.form.FormSupplier;
import com.inventory.form.FormUser;
import com.inventory.form.input.FormChangePassword;
import com.inventory.form.input.FormUserProfile;
import com.inventory.model.User;
import com.inventory.util.AppConfig;
import com.inventory.util.AppResources;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import raven.extras.AvatarIcon;
import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.data.Item;
import raven.modal.drawer.data.MenuItem;
import raven.modal.drawer.menu.MenuAction;
import raven.modal.drawer.menu.MenuEvent;
import raven.modal.drawer.menu.MenuOption;
import raven.modal.drawer.menu.MenuStyle;
import raven.modal.drawer.renderer.DrawerStraightDotLineStyle;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.drawer.simple.footer.LightDarkButtonFooter;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeaderData;

/**
 *
 * @author Dearclaudia
 */
public class Menu extends SimpleDrawerBuilder{

    public Menu() {
        super(createSimpleMenuOption());
        LightDarkButtonFooter lightDarkButtonFooter = (LightDarkButtonFooter) footer;
        lightDarkButtonFooter.removeAll();
        setColorTitle();
    }

    private void setColorTitle(){
        if(header instanceof JComponent){
            for(Component comp : ((JComponent) header).getComponents()){
                if(comp instanceof JPanel){
                    int labelCount = 0;
                    
                    for(Component subComp : ((JPanel) comp).getComponents()){
                        if(subComp instanceof JLabel){
                            if(labelCount == 0 ){
                                ((JLabel) subComp).setForeground(Color.WHITE);
                            }else if(labelCount == 1){
                                ((JLabel) subComp).setForeground(Color.GRAY);
                            }
                            labelCount++;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        AvatarIcon icon;
        icon = new AvatarIcon(getClass().getResource("/com/inventory/assets/Logo1.png"), 55, 55, 10f);
        icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        icon.setBorder(2, 2);
        
        changeAvatarIconBorderColor(icon);
        
        UIManager.addPropertyChangeListener((evt) -> {
            if(evt.getPropertyName().equals("lookAndFeel")){
                changeAvatarIconBorderColor(icon);
            }
        });
        
        return new SimpleHeaderData()
                .setIcon(icon)
                .setTitle(AppConfig.get("app.name"))
                .setDescription(AppConfig.get("app.perusahaan"));
    }

    private void changeAvatarIconBorderColor(AvatarIcon icon){
        icon.setBorderColor(new AvatarIcon.BorderColor(UIManager.getColor("Component.accentColor"),0.7f));
    }
    
    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData().setDescription("Version " + AppConfig.get("app.version"));
    }
    
    public static MenuOption createSimpleMenuOption() {
        User loggedInUser = FormManager.getLoggedInUser();
        String role = loggedInUser != null ? loggedInUser.getRole() : "Guest";

        MenuOption simpleMenuOption = new MenuOption();

        // === Menu Admin ===
        MenuItem[] adminMenu = new MenuItem[]{
            new Item.Label("MAIN"),
            new Item("Dashboard", "dashboard.svg",FormDashboard.class),
            // Master
            new Item.Label("MASTER"),
            new Item("Barang", "barang.svg",FormBarang.class),
            new Item("Kategori", "kategori.svg",FormKategori.class),
            new Item("Merk", "merk.svg",FormMerk.class),
            new Item("Satuan", "satuan.svg",FormSatuan.class),
            new Item("Rak", "rak.svg",FormRak.class),
            new Item("Gudang", "gudang.svg",FormGudang.class),
            new Item("Supplier", "supplier.svg",FormSupplier.class),
            // Transaksi
            new Item.Label("TRANSAKSI"),
            new Item("Barang Masuk", "barang_masuk.svg",FormBarangMasuk.class),
            new Item("Barang Keluar", "barang_keluar.svg",FormBarangKeluar.class),
            new Item("Stok Opname", "stock_opname.svg",FormStokOpname.class),
            // Laporan
            new Item.Label("LAPORAN"),
            new Item("LAPORAN", "report.svg")
                .subMenu("Laporan Stok",FormReportStok.class)
                .subMenu("Laporan Barang Masuk",FormReportBarangMasuk.class)
                .subMenu("Laporan Barang Keluar",FormReportBarangKeluar.class)
                .subMenu("Laporan Stok Opname",FormReportOpname.class),
            new Item.Label("OTHER"),
            new Item("Management User", "user.svg")
                .subMenu("Data User",FormUser.class)
                .subMenu("Profile",FormUserProfile.class)
                .subMenu("Pergantian Password",FormChangePassword.class),
            new Item("About", "about.svg"),
            new Item("Images","images.svg",FormImages.class),
            new Item("Logout", "logout.svg")
        };

        MenuItem[] gudangMenu = new MenuItem[]{
            new Item.Label("MAIN"),
            new Item("Dashboard", "dashboard.svg", FormDashboard.class),

            new Item.Label("MASTER"),
            new Item("Barang", "barang.svg", FormBarang.class),
            new Item("Rak", "rak.svg", FormRak.class),
            new Item("Gudang", "gudang.svg", FormGudang.class),

            new Item.Label("TRANSAKSI"),
            new Item("Barang Masuk", "barang_masuk.svg", FormBarangMasuk.class),
            new Item("Barang Keluar", "barang_keluar.svg", FormBarangKeluar.class),
            new Item("Stok Opname", "stock_opname.svg", FormStokOpname.class),

            new Item.Label("LAPORAN"),
            new Item("Laporan Stok", "report.svg", FormReportStok.class),

            new Item("Logout", "logout.svg")
        };

        MenuItem[] staffMenu = new MenuItem[]{
            new Item.Label("MAIN"),
            new Item("Dashboard", "dashboard.svg", FormDashboard.class),

            new Item.Label("LAPORAN"),
            new Item("Laporan", "report.svg")
                .subMenu("Laporan Stok", FormReportStok.class)
                .subMenu("Laporan Barang Masuk", FormReportBarangMasuk.class)
                .subMenu("Laporan Barang Keluar", FormReportBarangKeluar.class)
                .subMenu("Laporan Stok Opname", FormReportOpname.class),

            new Item("About", "about.svg"),
            new Item("Logout", "logout.svg")
        };

        MenuItem[] menuToUse;
        switch (role) {
            case "Admin":
                menuToUse = adminMenu;
                break;
            case "Gudang":
                menuToUse = gudangMenu;
                break;
            case "Staff":
                menuToUse = staffMenu;
                break;
            default:
                menuToUse = new MenuItem[0];
                break;
        }
        
        simpleMenuOption.setMenuStyle(new MenuStyle() {
            @Override
            public void styleMenu(JComponent component) {
                component.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
            }

            @Override
            public void styleMenuItem(JButton menu, int[] index, boolean isMainItem) {
                menu.setForeground(Color.WHITE);
                menu.putClientProperty("Button.selectedBackground", Color.BLUE);
            }
        });

        simpleMenuOption.getMenuStyle().setDrawerLineStyleRenderer(new DrawerStraightDotLineStyle());
        simpleMenuOption.setMenuItemAutoSelectionMode(MenuOption.MenuItemAutoSelectionMode.SELECT_SUB_MENU_LEVEL);
        simpleMenuOption.addMenuEvent(new MenuEvent() {
            @Override
            public void selected(MenuAction action, int[] ints) {
                String menuName = action.getItem().getName();
                Class<?> itemClass = action.getItem().getItemClass();

                if ("About".equalsIgnoreCase(menuName)) {
                    action.consume();
                    FormManager.showAbout();
                    return;
                } else if ("Logout".equalsIgnoreCase(menuName)) {
                    action.consume();
                    FormManager.logout();
                    return;
                }

                handleFormAction(itemClass, action);
            }

            private void handleFormAction(Class<?> itemClass, MenuAction action) {
                if (itemClass == null || !Form.class.isAssignableFrom(itemClass)) {
                    action.consume();
                    return;
                }

                @SuppressWarnings("unchecked")
                Class<? extends Form> formClass = (Class<? extends Form>) itemClass;
                FormManager.showForm(AllForms.getForm(formClass));
            }
        });

        simpleMenuOption.setMenus(menuToUse)
                .setBaseIconPath(AppResources.ICON_BASE)
                .setIconScale(0.45f);

        return simpleMenuOption;
    }


    @Override
    public int getDrawerWidth() {
        return 270;
    }

    @Override
    public int getDrawerCompactWidth() {
        return 80;
    }

    @Override
    public int getOpenDrawerAt() {
        return 1000;
    }

    @Override
    public void build(DrawerPanel drawerPanel) {
        drawerPanel.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
    }
    
    private static String getDrawerBackgroundStyle(){
        return "background:@accentColor";
    }
    
}
