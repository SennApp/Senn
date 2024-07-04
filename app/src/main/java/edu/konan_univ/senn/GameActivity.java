package edu.konan_univ.senn;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.nio.file.Path;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }



    }

}