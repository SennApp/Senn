package edu.konan_univ.senn.database.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.annotations.Nullable;

@IgnoreExtraProperties
public class User {

    public String displayName;

    public User() {
    }

    public User(@Nullable String displayName) {
        this.displayName = displayName;
    }
}
