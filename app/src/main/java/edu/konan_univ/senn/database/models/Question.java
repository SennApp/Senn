package edu.konan_univ.senn.database.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Question {

    public Long number;

    public String sentence;

    public Double answer;

    public Map<User, Double> precisions;

    public Question() {
    }

    public Question(Long number, String sentence, Double answer) {
        this.number = number;
        this.sentence = sentence;
        this.answer = answer;
        this.precisions = new HashMap<>();
    }
}
