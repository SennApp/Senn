package edu.konan_univ.senn;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.konan_univ.senn.database.DatabaseHelper;
import edu.konan_univ.senn.database.models.Precision;
import edu.konan_univ.senn.database.models.User;
import edu.konan_univ.senn.database.models.UserPrecision;

public class LeaderboardActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private int questionId = 0;

    public List<UserPrecision> data;

    private Precision precision = null;

    @Override
    protected void onStart() {
        super.onStart();
        data = new ArrayList<>();
        DatabaseHelper.getInstance().getPrecisionRef(questionId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                precision = task.getResult().getValue(Precision.class);
                if (precision != null) {
                    //Log.d("LeaderboardActivity", String.valueOf(precision.precisions.get("user1")));
                    FirebaseUser fbUser = auth.getCurrentUser();
                    if (fbUser == null) {
                        return;
                    }
                    // Create user if not exists
                    DatabaseHelper.getInstance().getUsersRef().get().addOnCompleteListener(dataSnapshotTask -> {
                        if (!dataSnapshotTask.isSuccessful()) {
                            return;
                        }
                        DataSnapshot snapshot = dataSnapshotTask.getResult();
                        snapshot.getChildren().forEach(action -> {
                            DatabaseHelper.getInstance().getUserRef(action.getKey()).get().addOnCompleteListener(retrievalTask -> {
                                if (!retrievalTask.isSuccessful()) {
                                    return;
                                }
                                User fetchedUser = retrievalTask.getResult().getValue(User.class);
                                if (fetchedUser != null) {
                                    UserPrecision userPrecision = new UserPrecision(fetchedUser.displayName, precision.precisions.get(action.getKey()));
                                    data.add(userPrecision);
                                    Log.d("userPrecision", String.valueOf(userPrecision));
                                }
                            });

                        });
                        data.sort((u0, u1) -> u0.precision() > u1.precision() ? 1 : u0.precision().equals(u1.precision()) ? 0 : -1);
                    });
                }

            }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
