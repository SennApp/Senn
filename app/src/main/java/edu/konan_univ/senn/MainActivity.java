package edu.konan_univ.senn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.annotations.Nullable;

import edu.konan_univ.senn.database.DatabaseHelper;
import edu.konan_univ.senn.database.models.User;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.signInAnonymously().addOnCompleteListener(this, (signInTask -> {
            if (signInTask.isSuccessful()) {
                FirebaseUser fbUser = auth.getCurrentUser();
                if (fbUser == null) {
                    Log.e(TAG, "Firebase user not found!");
                    return;
                }
                Log.d(TAG, "Retrieved firebase user: " + fbUser.getUid());
                // Create user if not exists
                String uid = fbUser.getUid();
                DatabaseHelper.getInstance().getUserRef(uid).get().addOnCompleteListener(retrievalTask -> {
                    if (!retrievalTask.isSuccessful()) {
                        return;
                    }
                    User fetchedUser = retrievalTask.getResult().getValue(User.class);
                    if (fetchedUser == null) {
                        User newUser = new User("Guest");
                        DatabaseHelper.getInstance().getUserRef(uid).setValue(newUser);
                        Log.d(TAG, "New user with uid " + uid + " created on the database!");
                    } else {
                        Log.d(TAG, "User with uid " + uid + " already exists on the database!");
                    }
                });
            }
        }));
    }

    public void onPlayClicked(View view) {
        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        startActivity(intent);
    }

    public void onLeaderClicked(View view) {
        Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
        startActivity(intent);
    }

    public void onGuideClicked(View view) {
        Intent intent = new Intent(MainActivity.this, GuideActivity.class);
        startActivity(intent);
    }

//    public void onCreditClicked(View view) {
//        Intent intent = new Intent(MainActivity.this, CreditActivity.class);
//        startActivity(intent);
//    }
//
//    public void onQuitClicked(View view) {
//        super.onDestroy();
//    }
}