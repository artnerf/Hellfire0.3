package com.clover.fda.hellfire03.menu;

import java.util.ArrayList;

/**
 * Created by firstdata on 06.03.15.
 * NEW
 */
public class MenuElement {
    protected long msg_text_id;
    protected String msg_text;
    protected ArrayList<MenuElement> submenu;
    protected long menu_item_id;
    protected long item_property;
    protected long pre_param_id;
    protected long pw_level;
    protected long number;
    protected long hidden;
    protected MenuElement previousMenuElement;

    public MenuElement(boolean sub) {
        msg_text_id = -1;
        menu_item_id = -1;
        item_property = -1;
        pre_param_id = -1;
        pw_level = -1;
        number = -1;
        hidden = -1;
        submenu = null;
        previousMenuElement = null;
        if (sub) submenu = new ArrayList<>();
    }

    public long getMsg_text_id() {
        return msg_text_id;
    }

    public ArrayList<MenuElement> getSubmenu() {
        return submenu;
    }

    public long getMenu_item_id() {
        return menu_item_id;
    }

    public long getItem_property() {
        return item_property;
    }

    public long getPre_param_id() {
        return pre_param_id;
    }

    public long getPw_level() {
        return pw_level;
    }

    public long getNumber() {
        return number;
    }

    public long getHidden() {
        return hidden;
    }

    public MenuElement getPreviousMenuElement() {
        return previousMenuElement;
    }

    public void setMsg_text_id(long msg_text_id) {
        this.msg_text_id = msg_text_id;
    }

    public void setMsg_text(String msg_text) {
        this.msg_text = msg_text;
    }

    public void setSubmenu(ArrayList<MenuElement> submenu) {
        this.submenu = submenu;
    }

    public void setMenu_item_id(long menu_item_id) {
        this.menu_item_id = menu_item_id;
    }

    public void setItem_property(long item_property) {
        this.item_property = item_property;
    }

    public void setPre_param_id(long pre_param_id) {
        this.pre_param_id = pre_param_id;
    }

    public void setPw_level(long pw_level) {
        this.pw_level = pw_level;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void setHidden(long hidden) {
        this.hidden = hidden;
    }

    public void setPreviousMenuElement(MenuElement previousMenuElement) {
        this.previousMenuElement = previousMenuElement;
    }
}
