package com.clover.fda.hellfire03.host;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.clover.fda.hellfire03.R;
import com.clover.fda.hellfire03.menu.ScMenu;

public class HostActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        TextView say = (TextView)findViewById(R.id.say_somethig);

        String terminalId = Host.buildEftRequest(getApplicationContext(), ScMenu.STARTUP_MESSAGE);
        say.setText(terminalId);
    }



}
