package com.clover.fda.hellfire03.menu;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;


import com.clover.fda.hellfire03.R;
import com.clover.fda.hellfire03.host.Host;

import java.io.IOException;

public class ScMenuActivity extends ActionBarActivity {

    private ScMenu scMenu;
    private TextView titleText;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button buttonNext;
    private Button buttonPrev;
    private Button buttonBack;
    private MenuButton menuButton1;
    private MenuButton menuButton2;
    private MenuButton menuButton3;
    private MenuButton menuButton4;
    private MenuButton menuButton5;
    private MenuButton menuButton6;
    private MenuButton menuButton7;
    private MenuButton menuButton8;
    private MenuButton menuButton9;

    protected String hostResult;

    private class HostCommTask extends AsyncTask<String, Void, String> {

        protected String returnString;

        protected String doInBackground(String ... str){
            returnString = str[0] + " dachte ich doch!";
            try {
                Host.sendReceiveHost();
            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }
            return returnString;
        }

        protected void onPostExecute(String myStr){
            hostResult = myStr + " sicher?";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sc_menu);

        titleText = (TextView)findViewById(R.id.menuTitle);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        button5 = (Button)findViewById(R.id.button5);
        button6 = (Button)findViewById(R.id.button6);
        button7 = (Button)findViewById(R.id.button7);
        button8 = (Button)findViewById(R.id.button8);
        button9 = (Button)findViewById(R.id.button9);
        buttonNext = (Button)findViewById(R.id.buttonNext);
        buttonPrev = (Button)findViewById(R.id.buttonPrev);
        buttonBack = (Button)findViewById(R.id.buttonBack);

        scMenu = new ScMenu();
        Context context = getApplicationContext();
        scMenu.setDefaultMenu(context);
        bindButtons();
    }



    private void bindButtons() {

        if(scMenu.isNextItem()){

            titleText.setText(scMenu.getTitle());

            menuButton1 = new MenuButton(button1, scMenu.getNextUnhiddenMenuItem());
            menuButton2 = new MenuButton(button2, scMenu.getNextUnhiddenMenuItem());
            menuButton3 = new MenuButton(button3, scMenu.getNextUnhiddenMenuItem());
            menuButton4 = new MenuButton(button4, scMenu.getNextUnhiddenMenuItem());
            menuButton5 = new MenuButton(button5, scMenu.getNextUnhiddenMenuItem());
            menuButton6 = new MenuButton(button6, scMenu.getNextUnhiddenMenuItem());
            menuButton7 = new MenuButton(button7, scMenu.getNextUnhiddenMenuItem());
            menuButton8 = new MenuButton(button8, scMenu.getNextUnhiddenMenuItem());
            menuButton9 = new MenuButton(button9, scMenu.getNextUnhiddenMenuItem());
        }
    }

    public void menuButtonClick(View view){
        long longMenuItemId = 0;
        MenuElement subMenu = null;
        int id = view.getId();

        switch (id){
            case R.id.button1:
                longMenuItemId = menuButton1.getMenuItem().menu_item_id;
                subMenu = menuButton1.getMenuItem();
                break;
            case R.id.button2:
                longMenuItemId = menuButton2.getMenuItem().menu_item_id;
                subMenu = menuButton2.getMenuItem();
                break;
            case R.id.button3:
                longMenuItemId = menuButton3.getMenuItem().menu_item_id;
                subMenu = menuButton3.getMenuItem();
                break;
            case R.id.button4:
                longMenuItemId = menuButton4.getMenuItem().menu_item_id;
                subMenu = menuButton4.getMenuItem();
                break;
            case R.id.button5:
                longMenuItemId = menuButton5.getMenuItem().menu_item_id;
                subMenu = menuButton5.getMenuItem();
                break;
            case R.id.button6:
                longMenuItemId = menuButton6.getMenuItem().menu_item_id;
                subMenu = menuButton6.getMenuItem();
                break;
            case R.id.button7:
                longMenuItemId = menuButton7.getMenuItem().menu_item_id;
                subMenu = menuButton7.getMenuItem();
                break;
            case R.id.button8:
                longMenuItemId = menuButton8.getMenuItem().menu_item_id;
                subMenu = menuButton8.getMenuItem();
                break;
            case R.id.button9:
                longMenuItemId = menuButton9.getMenuItem().menu_item_id;
                subMenu = menuButton9.getMenuItem();
                break;
            default:
        }

        if(subMenu != null && longMenuItemId == -1){
            scMenu.setSubMenu(subMenu);
            bindButtons();
            Toast toast = Toast.makeText(this, "Sub Menu", Toast.LENGTH_SHORT);
            toast.show();

        }
        else{
            Toast toast = Toast.makeText(this, "MenuId = "+longMenuItemId, Toast.LENGTH_SHORT);
            toast.show();

            if(longMenuItemId == 1){
                new HostCommTask().execute("hallo");
            }
        }
    }

    public void nextButtonClick(View view){
        bindButtons();
    }

    public void peviousButtonClick(View view){
        scMenu.oneStepBack();
        bindButtons();
    }

    public void backButtonClick(View view){
        scMenu.oneLevelUp();
        bindButtons();
    }

}
