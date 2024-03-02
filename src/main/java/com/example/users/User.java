package com.example.users;

import com.example.auth.JwtTokenPayload;
import com.example.characters.Character;
import com.example.users.inventory.Inventory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Entity("users")
public class User {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private boolean isActive = true;
    private List<String> roles = new ArrayList<>();
//    @JsonIgnoreProperties("user")
    @JsonIgnore
    @Reference
    private Inventory inventory;

    private int maxCharactersPerUser = 4;

//    @JsonIgnoreProperties("user")
    @JsonIgnore
    @Reference(ignoreMissing = true, lazy = true)
    private HashSet<Character> characters;

    private long gold = 0L;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;

    public User() {}

    public User(CreateUserDTO data) {
       this.username = data.getUsername();
       this.password = data.getPassword();
       this.email = data.getEmail();
       this.lastLogin = LocalDateTime.now();
       this.characters = new HashSet<>();
       this.roles.add("USER");
       this.isActive = true;
       this.inventory = data.getInventory();
    }

    public ObjectId getId() { return id; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getGold() { return gold; }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void increaseGold(long value) {
        this.gold += value;
    }
    public void decreaseGold(long value) {
        this.gold -= value;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashSet<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(HashSet<Character> characters) {
        this.characters = characters;
    }

    public void addCharacter(Character character){
        if(this.characters == null) this.characters = new HashSet<>();
        if(characters.size() >= maxCharactersPerUser) return;

        this.characters.add(character);
    }
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public int getMaxCharactersPerUser() {
        return maxCharactersPerUser;
    }

    public void setMaxCharactersPerUser(int maxCharactersPerUser) {
        this.maxCharactersPerUser = maxCharactersPerUser;
    }



    public JwtTokenPayload getDetailsForToken(Date expirationTime){
        return new JwtTokenPayload(id.toString(), email, username, expirationTime);
    }
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
