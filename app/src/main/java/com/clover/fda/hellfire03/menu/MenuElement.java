package com.clover.fda.hellfire03.menu;

import java.util.ArrayList;

/**
 * Created by nagyz on 05.03.2015.
 */
public class MenuElement {
        public  long msg_text_id;
        public String msg_text;
        public ArrayList<MenuElement> submenu;
        public long menu_item_id;
        public long item_property;
        public long pre_param_id;
        public long pw_level;
        public long number;
        public long hidden;
        public MenuElement previousMenuElement;

        public MenuElement() {
            msg_text_id = -1;
            submenu = new ArrayList<MenuElement>();
            menu_item_id = -1;
            item_property = -1;
            pre_param_id = -1;
            pw_level = -1;
            number = -1;
            hidden = -1;
            previousMenuElement = null;
        }

        public MenuElement(boolean bDir) {
            msg_text_id = -1;
            if (bDir) submenu = new ArrayList<MenuElement>();
            menu_item_id = -1;
            item_property = -1;
            pre_param_id = -1;
            pw_level = -1;
            number = -1;
            hidden = -1;
            previousMenuElement = null;
        }

}
