package org.openjfx.Entities;

import java.text.*;
import java.util.Calendar;
import java.util.Date;

public class User {
    private String name;
    private String surname;
    private String username;
    private String password;
    private String category1;
    private String category2;
    private int age;
    private String role;
    private int registered;
    private String country;
    private Date lastLogin;
    private Date updated;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    //Da concludere per mongoDB

    public User() {
        this.name = null;
        this.surname = null;
        this.username = null;
        this.password = null;
        this.category1 = null;
        this.category2 = null;
        this.role = "normalUser";
        this.age = 18;
        this.registered = Calendar.getInstance().get(Calendar.YEAR);
        this.country = null;
        this.lastLogin = Calendar.getInstance().getTime();
        this.updated = Calendar.getInstance().getTime();
    }

    public User( String username, String password, String category1, String category2, int age, String role) {
        this();
        this.username = username;
        this.category1 = category1;
        this.category2 = category2;
        this.age = age;
        this.role = role;
    }

    //Da concluedere per mongoDB
    public User(String username, String name, String surname, int age, String country ) {
        this();
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.role = role;
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public String getName(){return name;}

    public String getSurname(){return surname;}
    
    public String getCategory1() {
        return category1;
    }

    public String getCategory2() {
        return category2;
    }

    public int getAge() {
        return age;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public int getRegistered(){return registered;}

    public String getLastLogin(){return dateFormat.format(lastLogin);}

    public String getCountry(){return country;}

    public String getUpdated(){return dateFormat.format(updated);}

}
