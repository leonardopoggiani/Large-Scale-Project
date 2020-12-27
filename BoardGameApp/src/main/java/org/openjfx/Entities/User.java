package org.openjfx.Entities;

public class User {
    private String _id;
    private String name;
    private String surname;
    private String username;
    private String category1;
    private String category2;
    private int age;
    private String role;
    //Da concludere per mongoDB

    public User() {
    }

    public User( String username, String category1, String category2, int age, String role) {
        //this._id = _id;
        this.username = username;
        this.category1 = category1;
        this.category2 = category2;
        this.age = age;
        this.role = role;
    }

    //Da concluedere per mongoDB
    public User(String _id, String name, String surname, String username, String category1, String category2,  int age, String role) {
        this._id = _id;
        this.username = username;
        this.category1 = category1;
        this.category2 = category2;
        this.age = age;
        this.role = role;
    }

    public String get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

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
}
