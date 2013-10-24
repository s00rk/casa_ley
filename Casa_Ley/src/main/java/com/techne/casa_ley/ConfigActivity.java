package com.techne.casa_ley;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Victor on 23/10/13.
 */
public class ConfigActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Typeface font = Typeface.createFromAsset(getAssets(), "pulsarjs.ttf");
        TextView privacidad = (TextView)findViewById(R.id.txt_privacidad);
        privacidad.setTypeface(font);

    }


    public void btnConfig(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_backconfig:
                this.onBackPressed();
                break;

        }
    }

}
