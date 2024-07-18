package edu.konan_univ.senn.database.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String displayName;

    public User() {
    }

    public User(String displayName) {
        this.displayName = displayName;
    }
}
