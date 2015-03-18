package com.clover.fda.hellfire03.host;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.clover.fda.hellfire03.R;
import com.clover.fda.hellfire03.menu.ScMenu;
import com.clover.fda.hellfire03.util.Utils;

import java.io.IOException;

public class HostActivity extends ActionBarActivity {

    private String hostResult;
    private TextView say;

    private class HostCommTask extends AsyncTask<String, Void, String> {

        protected String returnString;

        protected String doInBackground(String ... str){

            try {
                byte[] resp = Host.sendReceiveHost();
                returnString = Utils.bytesToHex(resp);
            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }
            return returnString;
        }

        protected void onPostExecute(String myStr){
            hostResult = "\nHost Response: " + myStr ;
            say.append(hostResult);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        say = (TextView)findViewById(R.id.say_somethig);

        Host.getTriple(getApplicationContext());

        byte[] req = Host.buildEftRequest(ScMenu.DIAGNOSE);


        say.setText(Utils.bytesToHex(req));

        new HostCommTask().execute("hallo");
    }



}
