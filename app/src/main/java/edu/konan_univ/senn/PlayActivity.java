package edu.konan_univ.senn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

import edu.konan_univ.senn.database.DatabaseHelper;
import edu.konan_univ.senn.database.models.Question;

public class PlayActivity extends AppCompatActivity {

    private static final String TAG = "PlayActivity";

    private TextView sentenceView = null;

    private int questionId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(), (view, windowInsets) -> {
            if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())) {
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
            }
            return ViewCompat.onApplyWindowInsets(view, windowInsets);
        });
        sentenceView = findViewById(R.id.sentence);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CustomView view = findViewById(R.id.customView);
        DatabaseHelper.getInstance().getQuestionRef(questionId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Question question = task.getResult().getValue(Question.class);
                if (sentenceView == null || question == null) {
                    return;
                }
                sentenceView.setText(question.sentence);
                view.addOnAnswerListener(distance -> {
                    float accuracy = (distance / question.answer) * 100.0F;
                    Log.d(TAG, "Accuracy: " + accuracy + "%");
                });
            }
        });
    }

    public void onBackClicked(View view) {
        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent);
    }
}