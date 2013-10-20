package com.techne.casa_ley;

import java.util.Calendar;

public class Menu_Inicio {
    private String name;
    private int image;
    private long fecha;

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

    public String getFecha()
    {
        if(fecha != 0)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(fecha);
            return cal.get(Calendar.DATE) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " " +
                    cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE);
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
