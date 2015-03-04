package com.clover.fda.hellfire03.menu;

import java.util.ArrayList;

/**
 * Created by firstdata on 04.03.15.
 */
public class DoMenu {
    String defaultMenu = "{\"menu_directory\":true,\"message_text_id\":\"0039\"}";

    public class MenuItem {
        protected String name;
        protected long itemId;
        protected MenuDir nextMenuDir;

    }
    public class MenuDir {
        protected String title;
        protected ArrayList<MenuItem> items;
        protected MenuDir previousMenuDir;

        public MenuDir() {
            items = new ArrayList<MenuItem>();
        }
    }

    public MenuDir createDefaultMenu(){
        MenuDir basisMenu = new MenuDir();
        basisMenu.title = "Hauptmenu";
        for(int i = 0; i < 10; i++){
            MenuItem item = new MenuItem();
            item.name = "Button"+i;
            if(i == 2) {
                item.nextMenuDir = new MenuDir();
                item.nextMenuDir.previousMenuDir = basisMenu;
                item.nextMenuDir.title = "Submenu1";
                item.itemId = 0;
                for(int j = 0; j < 7; j++){
                    MenuItem subItem = new MenuItem();
                    subItem.name = "SubButton"+j;
                    subItem.itemId = j + 300;
                    subItem.nextMenuDir = null;
                    item.nextMenuDir.items.add(subItem);
                }
            }
            else if(i == 5) {
                item.nextMenuDir = new MenuDir();
                item.nextMenuDir.previousMenuDir = basisMenu;
                item.nextMenuDir.title = "Submenu2";
                item.itemId = 0;
                for(int j = 0; j < 5; j++){
                    MenuItem subItem = new MenuItem();
                    subItem.name = "SubButton"+j;
                    subItem.itemId = j + 400;
                    subItem.nextMenuDir = null;
                    item.nextMenuDir.items.add(subItem);
                }
            }
            else {
                item.itemId = i + 200;
                item.nextMenuDir = null;
            }
            basisMenu.items.add(item);
        }
        return basisMenu;
    }
}
