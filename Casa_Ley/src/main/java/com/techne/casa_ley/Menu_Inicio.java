package com.techne.casa_ley;

import android.graphics.Bitmap;

import java.sql.Time;
import java.util.Calendar;

public class Menu_Inicio {
    private String name;
    private int image;
    private long fecha;
    private  String desc = "";

    public Menu_Inicio(int img, String n, String d)
    {
       super();
        this.image = img;
        this.name = n;
        this.fecha = 0;
        this.desc = d;
    }

    public Menu_Inicio(String name, long fecha)
    {
        super();
        this.name = name;
        this.fecha = fecha;
        this.image = -1;
    }

    public Menu_Inicio(String name)
    {
        super();
        this.name = name;
        this.fecha = 0;
        this.image = -1;
    }

    public Menu_Inicio(String name, int image) {
        super();
        this.name = name;
        this.image = image;
        this.fecha = 0;
    }

    public String getDesc()
    {
        return this.desc;
    }

    public String getFecha()
    {
        if(fecha != 0)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(fecha);

            Time tiempo = new Time(fecha);
            int hora = tiempo.getHours();
            int minuto = tiempo.getMinutes();
            String tipo = "AM";
            if(hora > 12)
            {
                hora -= 12;
                tipo = "PM";
            }

            return cal.get(Calendar.DATE) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " +
                    hora + ":" + minuto + " " + tipo;
        }else
            return "";
    }
    public void setFecha(long f)
    {
        this.fecha = f;
    }

    public String getName() {
        return name;
    }
    public void setName(String nameText) {
        name = nameText;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
}
