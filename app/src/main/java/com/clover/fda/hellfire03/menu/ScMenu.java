package com.clover.fda.hellfire03.menu;

import android.content.Context;
import android.util.Log;

import com.clover.fda.hellfire03.tlv.TLV;
import com.clover.fda.hellfire03.tlv.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by firstdata on 04.03.15.
 */
public class ScMenu {

    static private MenuElement currentMenuDir;
    static private int itemNbr = 0;
    static private int itemNbrs = 0;
    private String fileName = "myMenuObject.dat";

    private String strMenu = "FF80228205CDDF8049020039FF8022820469DF8049020026FF80231BDF010400000001DF804902000ADF804602F800DF80480400000001FF802321DF010400000002DF8049020025DF8030020001DF8033020002DF80480400000004FF802321DF010400000003DF8049020007DF8030020002DF804602F800DF80480400000001FF80227BDF8049020003DF8030020001FF802321DF01040000000ADF804902003BDF8033020004DF804602F000DF80480400000001FF802321DF01040000000BDF8049020036DF8033020005DF8046027000DF80480400000001FF802321DF01040000000CDF804902003CDF8033020008DF8046027000DF80480400000001FF80227EDF8049020028FF80231BDF010400010004DF804902000ADF8046022000DF80480400000001FF802321DF010400010005DF8049020037DF8030020002DF8046022000DF80480400000001FF802313DF010400010006DF8049020009DF8046022000FF802319DF010400010105DF804902001BDF8030020001DF8046022000FF80225ADF804902002FFF802323DF010400012001DF804902000EDF8046022800DF80480400000001DF80480400000011FF802329DF010400012003DF804902002DDF8030020001DF8046022800DF80480400000001DF80480400000011FF802313DF010400000051DF8049020010DF8030020002FF80224ADF804902000BFF80231BDF010400001001DF804902000BDF8033020005DF80480400000012FF802321DF010400001003DF8049020034DF8030020002DF8033020006DF80480400000012FF802273DF804902001EFF80231BDF010400000004DF804902000CDF804602F800DF80480400000001FF802323DF010400000005DF804902003EDF8030020001DF80480400000004DF80480400000002FF802323DF010400000105DF804902002CDF8030020001DF80480400000004DF80480400000003FF802281C2DF804902002BFF80231BDF010400000006DF8049020035DF804602F800DF80480400000001FF802331DF010400000007DF804902003FDF8030020001DF804602F800DF80480400000006DF80480400000007DF80480400000001FF802264DF8049020023FF80232BDF010400000008DF8049020031DF804602F800DF80480400000006DF80480400000007DF80480400000001FF80232BDF010400000009DF8049020008DF804602F800DF80480400000006DF80480400000007DF80480400000001FF802281F3DF8049020011FF802323DF010400000041DF804902000ADF804602F800DF80480400000001DF80480400000007FF80225CDF804902001EFF802323DF010400000044DF804902000CDF804602F800DF80480400000001DF80480400000007FF80232BDF010400000045DF804902003EDF8030020001DF80480400000004DF80480400000002DF80480400000007FF802262DF804902002BFF802323DF010400000046DF8049020035DF804602F800DF80480400000001DF80480400000007FF802331DF010400000047DF804902003FDF8030020001DF804602F800DF80480400000006DF80480400000001DF80480400000007FF802281F9DF804902001FFF80231BDF010400020002DF8049020019DF8033020003DF80480400000005FF802319DF010400020001DF8049020027DF8030020001DF8033020001FF802319DF010400020004DF8049020012DF8030020001DF8033020007FF80228184DF8049020021DF8030020001FF80230DDF010400030002DF8049020022FF80230DDF010400030007DF804902002AFF80230DDF010400030008DF8049020020FF80230DDF010400030001DF804902002EFF802319DF010400040002DF8049020032DF8030020001DF8033020009FF802313DF010400040001DF804902003DDF8033020006FF80230DDF010400090001DF8049020004FF802312DF010400040003DF804902000FDF80320101FF802312DF010400040005DF8049020001DF80320101FF80232ADF010400099000DF8049020038DF80320101DF80480400000008DF80480400000009DF80480400000010";


    public ScMenu(){
        itemNbr = 0;
        itemNbrs = 0;
        currentMenuDir = null;
    }

    public void setDefaultMenu(Context context){


        try{
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            try{
                try{
                    currentMenuDir = (MenuElement) is.readObject();
                    is.close();
                    fis.close();
                }catch(ClassNotFoundException cnfe) {
                    cnfe.printStackTrace();
                }
            } catch (ClassCastException cce){
                cce.printStackTrace();
            }

        } catch (IOException ioe){
            ioe.printStackTrace();
        }

        if(currentMenuDir == null){
            //currentMenuDir = createDefaultMenu();
            currentMenuDir = getMenuFromTLV();
            itemNbr = 0;
            itemNbrs = currentMenuDir.submenu.size();

//            doDefault(context);
        }
    }

    private MenuElement getMenuFromTLV(){
        byte[] baMenu = Utils.hexStringToBytes(strMenu);
        TLV tlv = new TLV();
        MenuElement menu = new MenuElement(true);
        int iRet = tlv.GetTLVMenu(baMenu, menu);
        Log.d("MENU", "GetTLVMenu=" + iRet);
        // test, if the root menu is not the "Hauptmenu", but the Hauptmenu is the first MenuElement of the ArrayList
        // the root menu has only a submenu with one MenuElement what is the Hauptmenu. Clear?
        if(menu.submenu != null && menu.previousMenuElement == null && menu.msg_text_id == -1 && menu.submenu.size() == 1){
            ArrayList<MenuElement> elements = menu.submenu;
            return elements.get(0);
        }
        else{
            return menu;
        }
    }

    public MenuElement createDefaultMenu(){
        MenuElement basisMenu = new MenuElement(true);
        basisMenu.msg_text = "Hauptmenu";
        for(int i = 0; i < 10; i++){
            if(i == 2) {
                MenuElement element = new MenuElement(true);
                element.msg_text = "Button"+i;
                element.previousMenuElement = basisMenu;
                element.msg_text = "Submenu1";
                for(int j = 0; j < 7; j++){
                    MenuElement subItem = new MenuElement(false);
                    subItem.msg_text = "SubButton"+j;
                    subItem.menu_item_id = j + 300;
                    element.submenu.add(subItem);
                }
                basisMenu.submenu.add(element);
            }
            else if(i == 5) {
                MenuElement element = new MenuElement(true);
                element.msg_text = "Button"+i;
                element.previousMenuElement = basisMenu;
                element.msg_text = "Submenu2";
                for(int j = 0; j < 5; j++){
                    MenuElement subItem = new MenuElement(false);
                    subItem.msg_text = "SubButton"+j;
                    subItem.menu_item_id = j + 400;
                    element.submenu.add(subItem);
                }
                basisMenu.submenu.add(element);
            }
            else {
                MenuElement element = new MenuElement(false);
                element.msg_text = "Button"+i;
                element.menu_item_id = i + 200;
                basisMenu.submenu.add(element);
            }
        }
        return basisMenu;
    }


    public void doDefault(Context context){
//        JSONObject jsonObject = new JSONObject();

//        try {
//            JSONObject myJson = jsonObject.put("myScMenu", currentMenuDir);
            try{
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                try{
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    os.writeObject(currentMenuDir);
                    os.close();
                    fos.close();
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
            } catch (FileNotFoundException ex){
                ex.printStackTrace();
            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public String getTitle(){
        if(currentMenuDir.msg_text == null)
            return " ";
        else
            return currentMenuDir.msg_text;
    }

    boolean isNextItem(){
        return itemNbr < itemNbrs ? true : false;
    }

    public MenuElement getNextMenuItem() {
        MenuElement menuItem = null;
        if(itemNbr < itemNbrs) {
            menuItem = currentMenuDir.submenu.get(itemNbr);
            itemNbr++;
        }

        return menuItem;
    }

    public void oneStepBack(){
        int modItemNbr = itemNbr%6;
        itemNbr = itemNbr - modItemNbr - 6;
        if(itemNbr < 0) itemNbr = 0;
    }

    public void setSubMenu(MenuElement subMenu){
        currentMenuDir = subMenu;
        itemNbr = 0;
        itemNbrs = currentMenuDir.submenu.size();
    }

    public boolean oneLevelUp(){
        MenuElement previous = currentMenuDir.previousMenuElement;
        if(previous.submenu != null && previous.previousMenuElement == null && previous.msg_text_id == -1 && previous.submenu.size() == 1){
            itemNbr = 0;
            return false;
        }
        else{
            currentMenuDir = previous;
            itemNbr = 0;
            itemNbrs = currentMenuDir.submenu.size();
            return true;
        }
    }
}
