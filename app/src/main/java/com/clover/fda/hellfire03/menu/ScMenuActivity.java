package com.clover.fda.hellfire03.menu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clover.fda.hellfire03.R;

public class ScMenuActivity extends ActionBarActivity {

    private ScMenu scMenu;
    private TextView titleText;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button buttonNext;
    private Button buttonPrev;
    private Button buttonBack;
    private MenuButton menuButton1;
    private MenuButton menuButton2;
    private MenuButton menuButton3;
    private MenuButton menuButton4;
    private MenuButton menuButton5;
    private MenuButton menuButton6;

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
        buttonNext = (Button)findViewById(R.id.buttonNext);
        buttonPrev = (Button)findViewById(R.id.buttonPrev);
        buttonBack = (Button)findViewById(R.id.buttonBack);

        scMenu = new ScMenu();
        scMenu.setDefaultMenu();

        bindButtons();
    }



    private void bindButtons() {

        if(scMenu.isNextItem()){

            titleText.setText(scMenu.getTitle());
            menuButton1 = new MenuButton(button1, scMenu.getNextMenuItem());
            menuButton2 = new MenuButton(button2, scMenu.getNextMenuItem());
            menuButton3 = new MenuButton(button3, scMenu.getNextMenuItem());
            menuButton4 = new MenuButton(button4, scMenu.getNextMenuItem());
            menuButton5 = new MenuButton(button5, scMenu.getNextMenuItem());
            menuButton6 = new MenuButton(button6, scMenu.getNextMenuItem());
        }
    }

    public void menuButtonClick(View view){
        long longMenuItemId = 0;
        DoMenu.MenuDir subMenu = null;
        int id = view.getId();

        switch (id){
            case R.id.button1:
                longMenuItemId = menuButton1.getMenuItem().itemId;
                subMenu = menuButton1.getSubMenu();
                break;
            case R.id.button2:
                longMenuItemId = menuButton2.getMenuItem().itemId;
                subMenu = menuButton2.getSubMenu();
                break;
            case R.id.button3:
                longMenuItemId = menuButton3.getMenuItem().itemId;
                subMenu = menuButton3.getSubMenu();
                break;
            case R.id.button4:
                longMenuItemId = menuButton4.getMenuItem().itemId;
                subMenu = menuButton4.getSubMenu();
                break;
            case R.id.button5:
                longMenuItemId = menuButton5.getMenuItem().itemId;
                subMenu = menuButton5.getSubMenu();
                break;
            case R.id.button6:
                longMenuItemId = menuButton6.getMenuItem().itemId;
                subMenu = menuButton6.getSubMenu();
                break;
            default:
        }

        if(subMenu != null){
            scMenu.setSubMenu(subMenu);
            bindButtons();
            Toast toast = Toast.makeText(this, "Sub Menu", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(this, "MenuId = "+longMenuItemId, Toast.LENGTH_SHORT);
            toast.show();
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
