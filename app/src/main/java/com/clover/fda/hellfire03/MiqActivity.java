package com.clover.fda.hellfire03;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.clover.fda.hellfire03.menu.ScMenuActivity;
import com.clover.fda.hellfire03.parameter.ParameterDemoActivity;


public class MiqActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_miq);
	}

    public void doScMenu(View view) {
        Intent intend = new Intent(this, ScMenuActivity.class);
        startActivity(intend);
    }

    public void doParameter(View view){
        Intent intend = new Intent(this, ParameterDemoActivity.class);
        startActivity(intend);

    }
}
