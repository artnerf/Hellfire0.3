package com.clover.fda.hellfire03.menu;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by firstdata on 04.03.15.
 */
public class ScMenu {

    static private DoMenu.MenuDir currentMenuDir;
    static private int itemNbr = 0;
    static private int itemNbrs = 0;
    private DoMenu doMenu;

    public ScMenu(){
        doMenu = new DoMenu();
    }

    public void setDefaultMenu(){
        currentMenuDir = doMenu.createDefaultMenu();
        itemNbr = 0;
        itemNbrs = currentMenuDir.items.size();

        doDefault();
    }


    public void doDefault(){
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject myJson = jsonObject.put("myScMenu", currentMenuDir);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle(){
        if(currentMenuDir.title == null)
            return " ";
        else
            return currentMenuDir.title;
    }

    boolean isNextItem(){
        return itemNbr < itemNbrs ? true : false;
    }

    public DoMenu.MenuItem getNextMenuItem() {
        DoMenu.MenuItem menuItem = null;
        if(itemNbr < itemNbrs) {
            menuItem = currentMenuDir.items.get(itemNbr);
            itemNbr++;
        }

        return menuItem;
    }

    public void oneStepBack(){
        int modItemNbr = itemNbr%6;
        itemNbr = itemNbr - modItemNbr - 6;
        if(itemNbr < 0) itemNbr = 0;
    }

    public void setSubMenu(DoMenu.MenuDir subMenu){
        currentMenuDir = subMenu;
        itemNbr = 0;
        itemNbrs = currentMenuDir.items.size();
    }

    public boolean oneLevelUp(){
        DoMenu.MenuDir previous = currentMenuDir.previousMenuDir;
        if(previous == null){
            itemNbr = 0;
            return false;
        }
        else{
            currentMenuDir = previous;
            itemNbr = 0;
            itemNbrs = currentMenuDir.items.size();
            return true;
        }
    }
}
