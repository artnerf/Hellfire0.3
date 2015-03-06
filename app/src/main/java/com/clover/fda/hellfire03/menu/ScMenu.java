package com.clover.fda.hellfire03.menu;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by firstdata on 04.03.15.
 */
public class ScMenu {

    static private DoMenu.MenuDir currentMenuDir;
    static private int itemNbr = 0;
    static private int itemNbrs = 0;
    private DoMenu doMenu;
    String fileName = "myMenuObject.dat";

    public ScMenu(){
        doMenu = new DoMenu();
    }

    public void setDefaultMenu(Context context){


        try{
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            try{
                try{
                    currentMenuDir = (DoMenu.MenuDir) is.readObject();
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
            currentMenuDir = doMenu.createDefaultMenu();
            itemNbr = 0;
            itemNbrs = currentMenuDir.items.size();

            doDefault(context);
        }
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
