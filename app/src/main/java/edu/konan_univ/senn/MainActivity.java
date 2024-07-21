package edu.konan_univ.senn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container), (v, insets) -> {
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
                    return;
                }
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
                    }
                });
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onPlayClicked(View view) {
        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        startActivity(intent);
    }

    public void onLeaderClicked(View view) {
        Toast.makeText(this, "実装予定です！", Toast.LENGTH_SHORT).show();
    }

    public void onGuideClicked(View view) {
        Toast.makeText(this, "実装予定です！", Toast.LENGTH_SHORT).show();
    }

    public void onCreditClicked(View view) {
        Toast.makeText(this, "実装予定です！", Toast.LENGTH_SHORT).show();
    }

    public void onQuitClicked(View view) {
        finish();
    }
}