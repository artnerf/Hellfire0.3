package com.clover.fda.hellfire03.parameter;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clover.fda.hellfire03.R;

/**
 * Created by veselskyr on 09/03/15.
 */
public class ParameterDemoActivity extends Activity {
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        Button btnPrefs = (Button) findViewById(R.id.btnPrefs);
        Button btnGetPrefs = (Button) findViewById(R.id.btnGetPreferences);
        Button btnDispNetwork = (Button) findViewById(R.id.btnDisplayNetworkSettings);

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

                    default:
                        break;
                }
            }
        };

        btnPrefs.setOnClickListener(listener);
        btnGetPrefs.setOnClickListener(listener);
        btnDispNetwork.setOnClickListener(listener);
    }

    private void init_bkse_parameter() {
        SharedPreferences bkse_ini = PreferenceManager.getDefaultSharedPreferences(ParameterDemoActivity.this);
        SharedPreferences.Editor editor = bkse_ini.edit();

        editor.putString("terminal_id", "54047754");
        editor.putString("language", "de");

        // server
        editor.putString("ip_encryption", "NONE"); // NONE, SSL, DATAWIRE
        editor.putString("server_ip", "217.86.142.9"); // Hamburg server
        editor.putInt("server_port", 9013);
        editor.putString("server_ip2", "217.86.142.9"); // Hamburg server
        editor.putInt("server_port2", 9013);

        // printer
        editor.putInt("delay_print", 0);
        editor.putInt("elv_text", 0);

        // PIN-Pad
        editor.putString("pinpad_ip", "192.168.1.3");
        editor.putString("pp_type", "APASPP"); // INTERN, APASPP
        editor.putString("pp_cardreader", "SEPERATE"); // SEPERATE, HYBRID
        editor.putInt   ("pp_port", 9050);

        // Proxy
        editor.putBoolean("proxy_server", false);
        editor.putInt    ("proxy_port", 8080);
        editor.putString ("proxy_addr", "");
        editor.putString ("proxy_user", "");
        editor.putString ("proxy_pwd", "");

        // ECR
        editor.putBoolean("ecr_conn", false);
        editor.putString ("ecr_prot", "RIA");
        editor.putInt    ("ecr_port", 9010);
        editor.putInt    ("ecr_len_info", 4);
        editor.putInt    ("ecr_enc", 0);
        editor.putInt    ("ecr_receipt", 1);


        // Pay@table
        editor.putString("paytable_ip", "");
        editor.putInt("paytable_port", 1234);


        // tracer
        editor.putString("tracer_ip", "192.168.1.20");
        editor.putInt   ("tracer_level", 1);

        //
        editor.putString("company_no", "00000000");
        editor.putString("store_no", "00000000");
        editor.putString("pos_no", "00000000");
        editor.putString("currency", "0978");
        editor.putInt   ("currency_exp", 2);


        // Commit the edits!
        editor.commit();
    }

    private void displayNetworkSettings() {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        //intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
        startActivity(intent);
    }

    private void displaySharedPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ParameterDemoActivity.this);

        String username = prefs.getString("username", "Default NickName");
        String passw = prefs.getString("password", "Default Password");
        boolean checkBox = prefs.getBoolean("checkBox", false);
        String listPrefs = prefs.getString("listpref", "Default list prefs");
        int x = prefs.getInt("xxx", 0);

        StringBuilder builder = new StringBuilder();
        builder.append("Username: " + username + "\n");
        builder.append("Password: " + passw + "\n");
        builder.append("Keep me logged in: " + String.valueOf(checkBox) + "\n");
        builder.append("List preference: " + listPrefs);

        textView.setText(builder.toString());
    }
}
