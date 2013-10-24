package com.techne.casa_ley;

/**
 * Created by Victor on 19/10/13.
 */
public class Producto {

    private int _id;
    private String nombre;
    private String descripcion;

    public Producto(){}
    public Producto(String n, String d)
    {
        nombre = n;
        descripcion = d;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
