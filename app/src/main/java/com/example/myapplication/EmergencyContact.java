package com.example.myapplication;

public class EmergencyContact {
    private int id;
    private String name;
    private String phoneNumber;
    private String type;
    private int priority;
    private int imageResourceId;

    public EmergencyContact(String name, String phoneNumber, int imageResourceId) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imageResourceId = imageResourceId;
        this.type = "personal";
        this.priority = 0;
    }

    public EmergencyContact(int id, String name, String phoneNumber, String type, int priority, int imageResourceId) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.priority = priority;
        this.imageResourceId = imageResourceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}