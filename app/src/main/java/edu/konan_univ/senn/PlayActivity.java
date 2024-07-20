package edu.konan_univ.senn;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import edu.konan_univ.senn.database.DatabaseHelper;
import edu.konan_univ.senn.database.models.Question;

public class PlayActivity extends AppCompatActivity {
    int questionId = 0;
    float dist=0.0F;
    double accuracy=0.0;
    private TextView sentenceView = null;

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
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        // Add a listener to update the behavior of the toggle fullscreen button when
        // the system bars are hidden or revealed.
        ViewCompat.setOnApplyWindowInsetsListener(
                getWindow().getDecorView(),
                (view, windowInsets) -> {
                    // You can hide the caption bar even when the other system bars are visible.
                    // To account for this, explicitly check the visibility of navigationBars()
                    // and statusBars() rather than checking the visibility of systemBars().
                    if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                            || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())) {
                        // Hide both the status bar and the navigation bar.
                        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
                    }
                    return ViewCompat.onApplyWindowInsets(view, windowInsets);
                });

        sentenceView = findViewById(R.id.sentence);

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseHelper.getInstance().getQuestionRef(questionId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Question question = task.getResult().getValue(Question.class);
                if (sentenceView == null || question == null) {
                    return;
                }
                sentenceView.setText(question.sentence);
            }
        });
    }

    public void onBackClicked(View view) {
        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private float getDist(){
        CustomView view=findViewById(R.id.customView);
        return view.getDistance();
    }

}