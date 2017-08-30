package com.example.ravi.mychat;

/**
 * Created by Ravi on 21-07-2017.
 */

public class UserInfo {

    private String name;
    private String phone;
    private String email;

    public UserInfo(String name, String phone, String email)
    {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }


    //getter

    public String getName(){ return name; }
    public String getPhone(){ return phone; }
    public String getEmail(){ return email; }

    //setter

    public void setName(String name){ this.name = name; }
    public void setPhone(String phone){ this.phone = phone; }
    public void setEmail(String email){ this.email = email; }

}
