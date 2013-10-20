package com.techne.casa_ley;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddRecordatorioActivity extends Activity {

    DatabaseHelper db;
    private Boolean isUpdate;
    private int id;
    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrecordatorio);

        isUpdate = false;
        db = new DatabaseHelper(getApplicationContext());
        DatePicker date = (DatePicker)findViewById(R.id.datePicker);
        EditText recordar = (EditText)findViewById(R.id.txt_recordar);
        TimePicker time = (TimePicker)findViewById(R.id.timePicker);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 11)
            date.setCalendarViewShown(false);


        Bundle extras = getIntent().getExtras();
        boton = (Button)findViewById(R.id.update_recordatorio);
        boton.setText("Agregar");
        if(extras != null && extras.containsKey("id"))
        {
            Log.e("casa_ley", extras.getInt("id")+"");
            Recordatorio r = db.getRecordatorio(extras.getInt("id"));
            if(r == null){
                setResult(RESULT_CANCELED);
                finish();
            }

            isUpdate = true;
            boton.setText("Guardar");
            id = r.get_id();

            recordar.setText( r.getTitulo() );

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(r.getFecha());
            date.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)-1, cal.get(Calendar.DATE));
            time.setCurrentHour(cal.get(Calendar.HOUR));
            time.setCurrentMinute(cal.get(Calendar.MINUTE));
        }

    }

    public void btnAgregarRecordatorio(View v)
    {
        EditText recordar = (EditText)findViewById(R.id.txt_recordar);
        DatePicker date = (DatePicker)findViewById(R.id.datePicker);
        TimePicker time = (TimePicker)findViewById(R.id.timePicker);
        if(recordar.getText().toString().length() == 0)
        {
            Util.mensaje(this, "Ingrese recordatorio");
            return;
        }
        Calendar cal = Calendar.getInstance();
        Calendar actual = Calendar.getInstance();
        actual.add(Calendar.MONTH, 1);

        cal.set(date.getYear(), date.getMonth()+1, date.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute(), 0);
        if(actual.getTimeInMillis() >= cal.getTimeInMillis())
        {
            Util.mensaje(this, "La fecha Indicada es menor o igual al dia de hoy");
            return;
        }

        Recordatorio r = new Recordatorio();
        r.setTitulo(recordar.getText().toString());
        r.setFecha(cal.getTimeInMillis());

        if(isUpdate)
        {
            r.set_id(id);
            db.updateRecordatorio(r);
            Util.mensaje(this, "Recordatorio Actualizado");
        }else{
            db.createRecordatorio(r);
            Util.mensaje(this, "Recordatorio Agregado!");
        }
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        db.closeDB();
        finish();
    }

    public void btnBackR(View v)
    {
        this.onBackPressed();
    }
    
}
