package com.clover.fda.hellfire03.menu;

import android.view.View;
import android.widget.Button;
/**
 * Created by firstdata on 04.03.15.
 */
public class MenuButton {

    private Button mButton;
    private MenuElement menuItem;

    public MenuButton(Button button, MenuElement item){

        mButton = button;
        menuItem = item;
        if(mButton != null){
            if(menuItem != null){
                mButton.setText(menuItem.msg_text);
                mButton.setVisibility(View.VISIBLE);
            }
            else {
                mButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    public MenuElement getMenuItem(){
        return menuItem;
    }

//    public MenuElement getSubMenu(){
//        return menuItem;
//    }
}
