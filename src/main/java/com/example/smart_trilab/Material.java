package com.example.smart_trilab;

import javafx.scene.image.ImageView;

public class Material {
    int id,quantity;
    String name,color,type,reference,supplier;
    ImageView image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Material(int id, int quantity, String supplier, String name, String color, ImageView image, String type) {
        this.id = id;
        this.quantity = quantity;
        this.supplier = supplier;
        this.name = name;
        this.color = color;
        this.image = image;
        this.type = type;
        this.reference = String.format("%s%04d",type,id);
    }
}
