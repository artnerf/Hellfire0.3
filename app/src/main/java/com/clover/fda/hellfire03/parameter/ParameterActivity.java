package com.clover.fda.hellfire03.parameter;

import android.app.Activity;
import android.os.Bundle;

import com.clover.fda.hellfire03.R;

/**
 * Created by ver on 09.03.2015.
 */
public class ParameterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MyTheme);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new ParamsFragment()).commit();

    }
}

