package com.techne.casa_ley;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class TiendasActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendas);
    }

    public void btnRegresarP(View v)
    {
        this.onBackPressed();
    }
    
}
