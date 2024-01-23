package com.example.users;

import com.example.characters.Character;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity("users")
public class User {
    @Id
    private ObjectId id;
    private String username;
    private String password;
    private String email;
    private int maxCharactersPerUser = 4;

    @Reference(ignoreMissing = true, lazy = true)
    @JsonIgnoreProperties("character")
    private HashSet<Character> characters;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;

    public User() {    }

    public User(CreateUserDTO data) {
       this.username = data.getUsername();
       this.password = data.getPassword();
       this.email = data.getEmail();
       this.lastLogin = LocalDateTime.now();
       this.characters = new HashSet<>();
    }

    public ObjectId getId() { return id; }

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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUsername(String username) {
        this.username = username;
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
