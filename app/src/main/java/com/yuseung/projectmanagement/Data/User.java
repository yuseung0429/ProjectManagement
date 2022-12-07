package com.yuseung.projectmanagement.Data;

import com.yuseung.projectmanagement.Tool.Tool;

public class User {
    private String id;
    private String password;
    private String name;
    private String phone;
    private String email;
    public User(){};
    public User(String id, String password, String name, String phone, String email)
    {
        this.id = id;
        this.password = Tool.hashConverter(password);
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    public String getId()
    {
        return id;
    }
    public String getPassword()
    {
        return password;
    }
    public String getName()
    {
        return name;
    }
    public String getPhone()
    {
        return phone;
    }
    public String getEmail()
    {
        return email;
    }

    public void setId(String id)
    {
        this.id = id;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
}
