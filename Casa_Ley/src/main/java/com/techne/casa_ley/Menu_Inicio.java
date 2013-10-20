package com.techne.casa_ley;

public class Menu_Inicio {
    private String name;
    private int image;

    public Menu_Inicio(String name)
    {
        super();
        this.name = name;
        this.image = -1;
    }

    public Menu_Inicio(String name, int image) {
        super();
        this.name = name;
        this.image = image;
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
