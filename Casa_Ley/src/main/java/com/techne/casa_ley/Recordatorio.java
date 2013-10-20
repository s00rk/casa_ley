package com.techne.casa_ley;

/**
 * Created by Victor on 19/10/13.
 */
public class Recordatorio {
    private int _id;
    private String titulo;
    private String fecha;

    public Recordatorio(){}
    public Recordatorio(String titulo, String fecha)
    {
        this.titulo = titulo;
        this.fecha = fecha;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
