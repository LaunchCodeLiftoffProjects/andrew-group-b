package org.launchcode.ouchy.models.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class User {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue
    private int Id;

    @NotNull
    private String username;

    @NotNull
    private String pwHash;

    private Boolean isProvider;

    private int providerId = -1;

    public User() {
    }

    public User(String username, String password, Boolean isProvider) {
        this.username = username;
        this.pwHash = encoder.encode(password);
        this.isProvider = isProvider;
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }

    public int getId() {
        return Id;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getProvider() {
        return isProvider;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }
}