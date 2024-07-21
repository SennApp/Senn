package edu.konan_univ.senn;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.Nullable;

import edu.konan_univ.senn.database.DatabaseHelper;
import edu.konan_univ.senn.database.models.Precision;
import edu.konan_univ.senn.database.models.Question;

public class PlayActivity extends AppCompatActivity {

    private static final String TAG = "PlayActivity";

    @Nullable
    private TextView questionNumber = null;

    @Nullable
    private TextView sentenceView = null;

    @Nullable
    private CustomView drawingArea = null;

    @Nullable
    private TextView drawingHint = null;

    @Nullable
    private Dialog dialogResult = null;

    @Nullable
    private TextView dialogAnswer = null;

    @Nullable
    private TextView dialogSegmentLength = null;

    @Nullable
    private Button buttonContinue = null;

    private int questionId = 0;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container), (v, insets) -> {
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
        auth = FirebaseAuth.getInstance();

        // Initialize views
        questionNumber = findViewById(R.id.questionNumber);
        sentenceView = findViewById(R.id.sentence);
        drawingArea = findViewById(R.id.customView);
        drawingHint = findViewById(R.id.drawingHint);
        dialogResult = new Dialog(PlayActivity.this);
        dialogResult.setContentView(R.layout.dialog_result);
        dialogAnswer = dialogResult.findViewById(R.id.answer);
        dialogSegmentLength = dialogResult.findViewById(R.id.segmentLength);
        buttonContinue = dialogResult.findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(view -> {
            if (isMissingWidget()) {
                return;
            }
            questionId++; // Go to next question
            drawingArea.reset();
            dialogResult.dismiss(); // Close dialog
            initQuestionViews();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        questionId = 0;
        initQuestionViews();
    }

    public void onBackClicked(View view) {
        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void initQuestionViews() {
        if (isMissingWidget()) {
            return;
        }
        DatabaseHelper.getInstance().getQuestionRef(questionId).get().addOnSuccessListener(task -> {
            Question question = task.getValue(Question.class);
            boolean noMoreQuestions = question == null;
            if (noMoreQuestions) {
                // FIXME: 時間がないためによる緊急処置なので後でちゃんとした画面を作ってください
                dialogAnswer.setText("すべての問題を解き終えました！");
                dialogSegmentLength.setText("またプレイしてくださいね！");
                buttonContinue.setText("メインメニューに戻る");
                buttonContinue.setOnClickListener((v1) -> finish());
                dialogResult.show(); // Open result dialog
                return;
            }
            questionNumber.setText("問題" + (questionId + 1));
            sentenceView.setText(question.sentence);
            drawingArea.setOnDrawStartListener(() -> {
                drawingHint.setVisibility(View.INVISIBLE); // Make drawing hint invisible
            });
            drawingArea.setOnDrawFinishListener(distance -> {
                // TODO: Save data to the database
//                double accuracy = distance / question.answer;
//                double diff = Math.abs(1.0F - accuracy);
//                // TODO: Save data if the score is the highest accuracy
//                DatabaseHelper.getInstance().getPrecisionRef(questionId).get().addOnCompleteListener(taskPrecision -> {
//                    if (taskPrecision.isSuccessful()) {
//                        Precision precision = taskPrecision.getResult().getValue(Precision.class);
//                        FirebaseUser user = auth.getCurrentUser();
//                        String uid = user != null ? user.getUid() : null;
//                        if (uid == null || precision == null || precision.precisions == null) {
//                            return;
//                        }
//                        double diffRecord = Math.abs(1.0 - precision.precisions.getOrDefault(uid, 0.0));
//                        if (diff < diffRecord) {
//                            Precision newPrecision = new Precision(uid, accuracy);
//                            DatabaseHelper.getInstance().getPrecisionRef(questionId).setValue(newPrecision);
//                        }
//                    }
//                });

                // Show dialog
                float length = Math.round(distance * 10.0F) / 10.0F;
                dialogAnswer.setText("正解は" + question.answer + "でした！");
                dialogSegmentLength.setText("あなたが描いた線は" + length + "cmでした！");
                dialogResult.show(); // Open result dialog
            });
        });
    }

    private boolean isMissingWidget() {
        return questionNumber == null || sentenceView == null || drawingArea == null || drawingHint == null ||
                dialogResult == null || dialogAnswer == null || dialogSegmentLength == null || buttonContinue == null;
    }
}