package com.techne.casa_ley;

/**
 * Created by Victor on 23/10/13.
 */
public class Config {
    private int _id;
    private int local;
    private int push;
    private int tiendas;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getLocal() {
        return local;
    }

    public void setLocal(int local) {
        this.local = local;
    }

    public int getPush() {
        return push;
    }

    public void setPush(int push) {
        this.push = push;
    }

    public int getTiendas() {
        return tiendas;
    }

    public void setTiendas(int tiendas) {
        this.tiendas = tiendas;
    }
}
