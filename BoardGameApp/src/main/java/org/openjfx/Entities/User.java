package org.openjfx.Entities;

public class User {
    private String name;
    private String surname;
    private String username;
    private String password;
    private String category1;
    private String category2;
    private int age;
    private String role;
    //Da concludere per mongoDB

    public User() {
    }

    public User( String username, String password, String category1, String category2, int age, String role) {
        this.username = username;
        this.category1 = category1;
        this.category2 = category2;
        this.age = age;
        this.role = role;
    }

    //Da concluedere per mongoDB
    public User(String name, String surname, String username, String category1, String category2,  int age, String role) {

        this.username = username;
        this.category1 = category1;
        this.category2 = category2;
        this.age = age;
        this.role = role;
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

    public String getPassword() {
        return password;
    }
}
