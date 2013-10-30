package com.techne.casa_ley;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Victor on 25/10/13.
 */
public class PrivacidadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacidad);
    }

    public void btnPrivacidad(View v)
    {
        this.onBackPressed();
    }
}
