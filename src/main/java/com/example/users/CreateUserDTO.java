package com.example.users;

import com.example.users.inventory.Inventory;

public class CreateUserDTO {
    private String username;
    private String password;
    private String email;

    private Inventory inventory;

    public CreateUserDTO(String email, String password, String username) {
        this.password = password;
        this.email = email;
        this.username = username;
    }
    public CreateUserDTO(String email, String password, String username, Inventory inventory) {
        this.password = password;
        this.email = email;
        this.inventory = inventory;
        this.username = username;
    }

    public String getUsername(){
            return username;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}

