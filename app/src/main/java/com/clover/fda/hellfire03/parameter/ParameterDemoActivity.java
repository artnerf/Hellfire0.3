package com.clover.fda.hellfire03.parameter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clover.fda.hellfire03.R;
import com.clover.fda.hellfire03.common.DefaultPassword;
import com.clover.fda.hellfire03.common.PasswordDialogFragment;

/**
 * Created by veselskyr on 09/03/15.
 */
public class ParameterDemoActivity extends Activity implements PasswordDialogFragment.PasswordDialogListener {
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        Button btnPrefs = (Button) findViewById(R.id.btnPrefs);
        Button btnGetPrefs = (Button) findViewById(R.id.btnGetPreferences);
        Button btnDispNetwork = (Button) findViewById(R.id.btnDisplayNetworkSettings);
        Button btnBkseIni = (Button) findViewById(R.id.btnCreateParameterFile);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        textView = (TextView) findViewById(R.id.txtPrefs);

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnPrefs:
                        Intent intent = new Intent(ParameterDemoActivity.this, ParameterActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.btnGetPreferences:
                        displaySharedPreferences();
                        break;

                    case R.id.btnDisplayNetworkSettings:
                        displayNetworkSettings();
                        break;

                    case R.id.btnCreateParameterFile:
                        init_bkse_parameter();
                        break;

                    case R.id.btnLogin:
                        display_password_screen();
                        break;

                    default:
                        break;
                }
            }
        };

        btnPrefs.setOnClickListener(listener);
        btnGetPrefs.setOnClickListener(listener);
        btnDispNetwork.setOnClickListener(listener);
        btnBkseIni.setOnClickListener(listener);
        btnLogin.setOnClickListener(listener);
    }

    private void display_password_screen() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new PasswordDialogFragment();
        dialog.show(getFragmentManager(), "PasswordDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(String entered_pwd) {
        Log.d("Positive", entered_pwd);

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.d("Negative", "Cancel");
    }

    private void init_bkse_parameter() {
        SharedPreferences bkse_par = PreferenceManager.getDefaultSharedPreferences(ParameterDemoActivity.this);
        SharedPreferences.Editor editor = bkse_par.edit();

        editor.putString (Param.TERMINAL_ID, "54047754");
        editor.putString (Param.LANGUAGE, "de");

        // server
        editor.putString (Param.IP_ENCRYPTION, "NONE"); // NONE, SSL, DATAWIRE
        editor.putString (Param.SERVERIP, "217.86.142.9");  // Hamburg server
        editor.putString(Param.SERVERPORT, "9013");
        editor.putString (Param.SERVERIP_2, "217.86.142.9"); // Hamburg server
        editor.putString(Param.SERVERPORT_2, "9013");
        editor.putString (Param.PING_HOST, "www.orf.at");

        // printer
        editor.putString(Param.DELAY_PRINT, "5");
        editor.putString(Param.ELV_TEXT_NEEDED, "0");
        editor.putString(Param.PRINTER_WIDTH, "32");

        // PIN-Pad
        editor.putString (Param.PINPAD_CONNECTION, "USB");
        editor.putString (Param.PINPADIP, "192.168.1.3");
        editor.putString (Param.PINPAD_TYPE, "APASPP"); // INTERN, APASPP
        editor.putString (Param.PINPAD_CARDREADER, "SEPERATE"); // SEPERATE, HYBRID
        editor.putString (Param.PINPAD_PORT, "9050");

        // Proxy
        editor.putString (Param.PROXY_SERVER, "0");
        editor.putString(Param.PROXY_ADDR, "");
        editor.putString(Param.PROXY_PORT, "8080");
        editor.putString(Param.PROXY_USER, "");
        editor.putString (Param.PROXY_PWD, "");

        // ECR
        editor.putString (Param.ECR_CONNECTION, "0");
        editor.putString (Param.ECR_PROTOCOL, "RIA");
        editor.putString(Param.ECR_PORT, "9010");
        editor.putString(Param.ECR_LEN_INFO, "4");
        editor.putString(Param.ECR_ENC, "0");
        editor.putString (Param.ECR_RECEIPT, "1");


        // Pay@table
        editor.putString (Param.PAY_AT_TABLE_IP, "");
        editor.putString(Param.PAY_AT_TABLE_PORT, "1234");


        // tracer
        editor.putString (Param.TRACERIP, "192.168.1.20");
        editor.putString(Param.TRACE_LEVEL, "1");

        // bkse.dat
        editor.putString (Param.COMPANYNO, "00000666");
        editor.putString (Param.STORENO, "00000001");
        editor.putString (Param.POSNO, "54047754");
        editor.putString (Param.CURRENCY, "0978");
        editor.putString(Param.CURRENCY_EXP, "2");
        editor.putString (Param.ECR_VERSION, "90000001");
        editor.putString (Param.TRACE_NUM, "000000");
        editor.putString (Param.RECEIPT_NUM, "0000");
        editor.putString (Param.REQUEST_ID, "0000");
        editor.putString(Param.OFFLINE_SYNC, "0");
        editor.putString(Param.TA_DATE, "00000000");
        editor.putString (Param.TA_TIME, "000000");
        editor.putString (Param.T_SNR, "00000000");
        editor.putString (Param.PP_VENDOR, "FD100");
        editor.putString (Param.TIMER_VERSION, "00000000");
        editor.putString (Param.CAPABILITIES, "00E00000");
        editor.putString (Param.MERCHANT_PWD, DefaultPassword.MERCHANT);
        editor.putString (Param.CASHIER_PWD,  DefaultPassword.CASHIER);
        editor.putString (Param.SERVICE_PWD,  DefaultPassword.SERVICE);
        editor.putString (Param.LOCAL_VERSION, "00000000");
        editor.putString (Param.LAST_ERROR, "00000000");
        editor.putString (Param.LAST_ERROR_TAG, "00000000");
        editor.putString (Param.LAST_ERROR_TEXT, "");
        editor.putString(Param.SIGNATURE_LEN, "192");
        editor.putString(Param.RC_BEEP, "0");
        editor.putString(Param.PARAM_CHANGED, "1");
        editor.putString (Param.SPLIT_SIZE, "0");

        // Commit the edits!
        editor.commit();

        displaySharedPreferences();
    }

    private void displayNetworkSettings() {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    private void displaySharedPreferences() {
        SharedPreferences bkse_par = PreferenceManager.getDefaultSharedPreferences(ParameterDemoActivity.this);

        StringBuilder builder = new StringBuilder();

        builder.append (Param.TERMINAL_ID + ": " + bkse_par.getString(Param.TERMINAL_ID, "") + "\n");
        builder.append (Param.LANGUAGE + ": " + bkse_par.getString(Param.LANGUAGE, "") + "\n");
        builder.append (Param.IP_ENCRYPTION + ": " + bkse_par.getString(Param.IP_ENCRYPTION, "") + "\n");
        builder.append (Param.SERVERIP + ": " + bkse_par.getString(Param.SERVERIP, "") + "\n");
        builder.append (Param.SERVERPORT + ": " + bkse_par.getString(Param.SERVERPORT, "") + "\n");
        builder.append (Param.SERVERIP_2 + ": " + bkse_par.getString(Param.SERVERIP_2, "") + "\n");
        builder.append (Param.SERVERPORT_2 + ": " + bkse_par.getString(Param.SERVERPORT_2, "") + "\n");
        builder.append (Param.PING_HOST + ": " + bkse_par.getString(Param.PING_HOST, "") + "\n");
        builder.append (Param.DELAY_PRINT + ": " + bkse_par.getString(Param.DELAY_PRINT, "") + "\n");
        builder.append (Param.ELV_TEXT_NEEDED + ": " + bkse_par.getString(Param.ELV_TEXT_NEEDED, "") + "\n");
        builder.append (Param.PRINTER_WIDTH + ": " + bkse_par.getString(Param.PRINTER_WIDTH, "") + "\n");
        builder.append (Param.PINPAD_CONNECTION + ": " + bkse_par.getString(Param.PINPAD_CONNECTION, "") + "\n");
        builder.append (Param.PINPADIP + ": " + bkse_par.getString(Param.PINPADIP, "") + "\n");
        builder.append (Param.PINPAD_TYPE + ": " + bkse_par.getString(Param.PINPAD_TYPE, "") + "\n");
        builder.append (Param.PINPAD_CARDREADER + ": " + bkse_par.getString(Param.TERMINAL_ID, "") + "\n");
        builder.append (Param.PINPAD_PORT + ": " + bkse_par.getString(Param.PINPAD_CARDREADER, "") + "\n");
        builder.append (Param.PROXY_SERVER + ": " + bkse_par.getString(Param.PROXY_SERVER, "") + "\n");
        builder.append (Param.PROXY_ADDR + ": " + bkse_par.getString(Param.PROXY_ADDR, "") + "\n");
        builder.append (Param.PROXY_PORT + ": " + bkse_par.getString(Param.PROXY_PORT, "") + "\n");
        builder.append (Param.PROXY_USER + ": " + bkse_par.getString(Param.PROXY_USER, "") + "\n");
        builder.append (Param.PROXY_PWD + ": " + bkse_par.getString(Param.PROXY_PWD, "") + "\n");
        builder.append (Param.ECR_CONNECTION + ": " + bkse_par.getString(Param.ECR_CONNECTION, "") + "\n");
        builder.append (Param.ECR_PROTOCOL + ": " + bkse_par.getString(Param.ECR_PROTOCOL, "") + "\n");
        builder.append (Param.ECR_PORT + ": " + bkse_par.getString(Param.ECR_PORT, "") + "\n");
        builder.append (Param.ECR_LEN_INFO + ": " + bkse_par.getString(Param.ECR_LEN_INFO, "") + "\n");
        builder.append (Param.ECR_ENC + ": " + bkse_par.getString(Param.ECR_ENC, "") + "\n");
        builder.append (Param.ECR_RECEIPT + ": " + bkse_par.getString(Param.ECR_RECEIPT, "") + "\n");
        builder.append (Param.PAY_AT_TABLE_IP + ": " + bkse_par.getString(Param.PAY_AT_TABLE_IP, "") + "\n");
        builder.append (Param.PAY_AT_TABLE_PORT + ": " + bkse_par.getString(Param.PAY_AT_TABLE_PORT, "") + "\n");
        builder.append (Param.TRACERIP + ": " + bkse_par.getString(Param.TRACERIP, "") + "\n");
        builder.append (Param.TRACE_LEVEL + ": " + bkse_par.getString(Param.TRACE_LEVEL, "") + "\n");


//        // bkse.dat
//        editor.putString (Param.COMPANYNO, "00000000");
//        editor.putString (Param.STORENO, "00000000");
//        editor.putString (Param.POSNO, "00000000");
//        editor.putString (Param.CURRENCY, "0978");
//        editor.putString (Param.CURRENCY_EXP, "2");
//        editor.putString (Param.ECR_VERSION, "90000001");
//        editor.putString (Param.TRACE_NUM, "000000");
//        editor.putString (Param.RECEIPT_NUM, "0000");
//        editor.putString (Param.REQUEST_ID, "0000");
//        editor.putString (Param.OFFLINE_SYNC, "0");
//        editor.putString (Param.TA_DATE, "00000000");
//        editor.putString (Param.TA_TIME, "000000");
//        editor.putString (Param.T_SNR, "00000000");
//        editor.putString (Param.PP_VENDOR, "FD100");
//        editor.putString (Param.TIMER_VERSION, "00000000");
//        editor.putString (Param.CAPABILITIES, "00E00000");
//        editor.putString (Param.MERCHANT_PWD, DefaultPassword.MERCHANT);
//        editor.putString (Param.CASHIER_PWD,  DefaultPassword.CASHIER);
//        editor.putString (Param.SERVICE_PWD,  DefaultPassword.SERVICE);
//        editor.putString (Param.LOCAL_VERSION, "00000000");
//        editor.putString (Param.LAST_ERROR, "00000000");
//        editor.putString (Param.LAST_ERROR_TAG, "00000000");
//        editor.putString (Param.LAST_ERROR_TEXT, "");
//        editor.putString (Param.SIGNATURE_LEN, "192");
//        editor.putString (Param.RC_BEEP, "0");
//        editor.putString (Param.PARAM_CHANGED, "1");
//        editor.putString (Param.SPLIT_SIZE, "0");
//
        textView.setText(builder.toString());
    }
}
