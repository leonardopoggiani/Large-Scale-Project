package it.unipi.dii.LSMDB.project.group5.bean;

import java.sql.Timestamp;
import java.util.Calendar;

public class UserBean extends User{

    private String username;
    private String password;
    private String category1;
    private String category2;
    private int age;
    private String role;
    private int registered;
    private String country;
    private Timestamp lastLogin;
    private Timestamp updated;
    private String name;
    private String surname;
    //Da concludere per mongoDB

    public UserBean() {
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
        this.lastLogin = new Timestamp(System.currentTimeMillis());
        this.updated = new Timestamp(System.currentTimeMillis());
    }

    public UserBean(String username, String password, String category1, String category2, int age, String role) {
        this();
        this.username = username;
        this.category1 = category1;
        this.category2 = category2;
        this.age = age;
        this.role = role;
        this.password = password;
    }

    //Da concluedere per mongoDB
    public UserBean(String username, String name, String surname, int age, String country ) {
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

    public Timestamp getLastLogin(){return lastLogin;}

    public String getCountry(){return country;}

    public Timestamp getUpdated(){return updated;}

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegistered(int registered) {
        this.registered = registered;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
