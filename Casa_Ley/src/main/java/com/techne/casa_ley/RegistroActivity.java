package com.techne.casa_ley;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class RegistroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
        //super.onBackPressed();
    }

    public void btnBack(View v)
    {
        this.onBackPressed();
    }
}
