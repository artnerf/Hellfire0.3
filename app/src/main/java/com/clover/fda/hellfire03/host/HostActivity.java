package com.clover.fda.hellfire03.host;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.clover.fda.hellfire03.R;
import com.clover.fda.hellfire03.menu.ScMenu;
import com.clover.fda.hellfire03.util.Utils;

import java.io.IOException;

public class HostActivity extends ActionBarActivity {

    private String hostResult;
    private String sendToHost;
    private TextView say;
    protected int e0received;

    private class HostCommTask extends AsyncTask<String, Void, byte[]> {

        protected String returnString;

        protected byte[] doInBackground(String ... str){
            byte[] resp = null;
            try {
                resp = Host.sendReceiveHost();
             }
            catch (IOException ioe){
                ioe.printStackTrace();
            }
            return resp;
        }

        protected void onPostExecute(byte[] hostRes){

            StringBuilder strOut = new StringBuilder();
            Elements resp = Host.analyseHostResponse(hostRes, strOut);

            if(resp != null){
                int indentify = resp.getE4Identify();
                switch(indentify){
                    case 0x00:
                        new PinPadCommTask().execute(resp);
                        break;
                    case 0x01:
                    case 0x02:
                        new PrinterTask().execute(resp);
                        break;
                    case 0x03:
                        break;
                }
            }
            hostResult = "\nHost Response: " + strOut ;
            say.append(hostResult);
        }
    }

    private class PinPadCommTask extends AsyncTask<Elements, Void, byte[]> {

        protected byte[] doInBackground(Elements... respE4){
            byte[] resp = null;
                resp = PinPad.sendReceivePP(respE4[0]);
            return resp;
        }

        protected void onPostExecute(byte[] resp){
            say.append(Utils.Byte2Hex(resp));

            byte[] ppResp = Host.buildPinPadResponse(resp);
            say.append("\nTo Host: " + Utils.bytesToHex(ppResp));
            new HostCommTask().execute("PP Hallo");

        }
    }

    private class PrinterTask extends AsyncTask<Elements, Void, byte[]> {

        protected byte[] doInBackground(Elements... respE4){
            byte[] printerResp = null;
            printerResp = Printer.sendReceivePrinter(respE4[0]);
            return printerResp;
        }
        protected void onPostExecute(byte[] printerResp){
            say.append(Utils.Byte2Hex(printerResp));

            byte[] ppResp = Host.buildPrinterResponse(printerResp);
            say.append("\nTo Host: " + Utils.bytesToHex(ppResp));
            new HostCommTask().execute("PP Hallo");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        say = (TextView)findViewById(R.id.say_somethig);

        Host.getTriple(getApplicationContext());

        byte[] req = Host.buildEftRequest(ScMenu.PING);


        say.setText(Utils.bytesToHex(req));
        say.setVerticalScrollBarEnabled(true);
        say.setMovementMethod(new ScrollingMovementMethod());

        e0received = 0;
        new HostCommTask().execute("hallo");
    }



}
