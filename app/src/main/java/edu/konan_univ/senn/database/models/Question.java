package edu.konan_univ.senn.database.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Question {

    public String sentence;

    public Double answer;

    public Question() {
    }

    public Question(String sentence, Double answer) {
        this.sentence = sentence;
        this.answer = answer;
    }
}
