package it.unipi.dii.lsmdb.project.group5.bean;

public class AgeBean {
    private int age;
    private int numUser;

    public int getAge() {
        return age;
    }

    public int getNumUser() {
        return numUser;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setNumUser(int numUser) {
        this.numUser = numUser;
    }

    @Override
    public String toString() {
        return age + ", " + numUser + ", " + numUser;
    }
}
