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
        Button btnBkseIni = (Button) findViewById(R.id.btnCreateParameterFile);

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
        btnBkseIni.setOnClickListener(listener);
    }

    private void init_bkse_parameter() {
        SharedPreferences bkse_par = PreferenceManager.getDefaultSharedPreferences(ParameterDemoActivity.this);
        SharedPreferences.Editor editor = bkse_par.edit();

        editor.putString("terminal_id", "54047754");
        editor.putString("language", "de");

        // server
        editor.putString("ip_encryption", "NONE"); // NONE, SSL, DATAWIRE
        editor.putString("server_ip", "217.86.142.9"); // Hamburg server
        editor.putInt   ("server_port", 9013);
        editor.putString("server_ip2", "217.86.142.9"); // Hamburg server
        editor.putInt   ("server_port2", 9013);
        editor.putString("server_ip2", "217.86.142.9"); // Hamburg server
        editor.putString("ping_host", "www.orf.at"); // Hamburg server

        // printer
        editor.putString("delay_print", "5");
        editor.putString("elv_text", "0");
        editor.putString("printer_width", "32");

        // PIN-Pad
        editor.putString ("pp_connection", "USB");
        editor.putString ("pinpad_ip", "192.168.1.3");
        editor.putString ("pp_type", "APASPP"); // INTERN, APASPP
        editor.putString ("pp_cardreader", "SEPERATE"); // SEPERATE, HYBRID
        editor.putInt    ("pp_port", 9050);

        // Proxy
        editor.putBoolean("proxy_server", false);
        editor.putString ("proxy_addr", "");
        editor.putInt    ("proxy_port", 8080);
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
        editor.putString ("paytable_ip", "");
        editor.putInt    ("paytable_port", 1234);


        // tracer
        editor.putString ("tracer_ip", "192.168.1.20");
        editor.putInt    ("tracer_level", 1);

        // bkse.dat
        editor.putString ("company_no", "00000000");
        editor.putString ("store_no", "00000000");
        editor.putString ("pos_no", "00000000");
        editor.putString ("currency", "0978");
        editor.putInt    ("currency_exp", 2);
        editor.putString ("ecr_version", "90000001");
        editor.putString ("trace_num", "000000");
        editor.putString ("receipt_num", "0000");
        editor.putString ("request_id", "0000");
        editor.putInt    ("offline_sync", 0);
        editor.putString ("ta_date", "00000000");
        editor.putString ("ta_time", "000000");
        editor.putString ("t_snr", "00000000");
        editor.putString ("pp_vendor", "FD100");
        editor.putString ("timer_version", "00000000");
        editor.putString ("capabilities", "00E00000");
        editor.putString ("merchant_pwd", "1234");
        editor.putString ("cashier_pwd", "1234");
        editor.putString ("service_pwd", "210888");
        editor.putString ("local_version", "00000000");
        editor.putString ("last_error", "00000000");
        editor.putString ("last_error_tag", "00000000");
        editor.putString ("last_error_text", "1234");
        editor.putInt    ("signature_len", 192);
        editor.putBoolean("readcard_beep", false);
        editor.putBoolean("param_changed", true);
        editor.putInt    ("split_size", 0);


        // Commit the edits!
        editor.commit();

        displaySharedPreferences();
    }

    private void displayNetworkSettings() {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        //intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
        startActivity(intent);
    }

    private void displaySharedPreferences() {
        SharedPreferences bkse_par = PreferenceManager.getDefaultSharedPreferences(ParameterDemoActivity.this);

        StringBuilder builder = new StringBuilder();
        builder.append("terminal_id: " + bkse_par.getString("terminal_id", "") + "\n");
        builder.append("language: " + bkse_par.getString("language", "") + "\n");
        builder.append("server_ip: " + bkse_par.getString("server_ip", "") + "\n");
        builder.append("server_port: " + bkse_par.getInt("server_port", 0) + "\n");
        builder.append("elv_text: " + bkse_par.getString("elv_text", "") + "\n");

        textView.setText(builder.toString());
    }
}
