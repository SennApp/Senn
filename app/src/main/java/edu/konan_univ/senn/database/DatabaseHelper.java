package edu.konan_univ.senn.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import edu.konan_univ.senn.BuildConfig;

public class DatabaseHelper {

    private static DatabaseHelper instance;

    private final DatabaseReference reference;

    private DatabaseHelper() {
        reference = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DATABASE_URL).getReference();
    }

    public DatabaseReference getUsersRef() {
        return reference.child("users");
    }

    public DatabaseReference getUserRef(@NotNull String uid) {
        return getUsersRef().child(uid);
    }

    public DatabaseReference getQuestionsRef() {
        return reference.child("questions");
    }
  
    public DatabaseReference getQuestionRef(@NotNull int questionId) {
        return getQuestionsRef().child(String.valueOf(questionId));
    }

    public DatabaseReference getStatisticsRef() {
        return reference.child("statistics");
    }

    public DatabaseReference getPrecisionRef(int question) {
        return getStatisticsRef().child(String.valueOf(question));    
    }
  
    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }
}
