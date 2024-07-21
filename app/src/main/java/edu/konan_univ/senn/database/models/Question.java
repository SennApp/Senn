package edu.konan_univ.senn.database.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Question {

    public String sentence;

    public Float answer;

    public Question() {
    }

    public Question(String sentence, Float answer) {
        this.sentence = sentence;
        this.answer = answer;
    }
}
