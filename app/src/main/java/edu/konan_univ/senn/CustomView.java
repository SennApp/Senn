package edu.konan_univ.senn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomView extends View {

    private final List<PointF> points = new ArrayList<>();
    private final Paint paint = new Paint();
    private boolean drawing = true;
    private float dist = 0.0F;

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (!isSegmentDrawable()) {
            return;
        }
        paint.setStrokeWidth(5);
        for (int i = 0; i < points.size() - 1; i++) {
            PointF current = points.get(i);
            PointF next = points.get(i + 1);
            canvas.drawLine(current.x, current.y, next.x, next.y, paint);
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        double accuracy=0;
        PointF point = new PointF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                invalidate(); // 画面を更新
                break;
            }
            case MotionEvent.ACTION_UP: {
                performClick();
                drawing = false;
                Log.d("CustomView", dist + "cm");
//                accuracy=dist/6*100;
//                Log.d("result", accuracy + "%");
                invalidate(); // 画面を更新
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (!drawing) {
                    break;
                }
                points.add(point); // 点を配列に追加
                updateDist();

                invalidate(); // 画面を更新
                break;
            }
        }
        return true;
    }

    private void updateDist() {
        if (!isSegmentDrawable()) {
            return;
        }
        PointF current = points.get(points.size() - 1);
        PointF prev = points.get(points.size() - 2);
        dist += calcDist(current, prev);
    }

    private float calcDist(PointF a, PointF b) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float cmPerInches = 2.54F;
        float inchesDistX = Math.abs(a.x - b.x) / metrics.xdpi;
        float inchesDistY = Math.abs(a.y - b.y) / metrics.ydpi;
        float cmDistX = inchesDistX * cmPerInches;
        float cmDistY = inchesDistY * cmPerInches;
        return (float) Math.sqrt(Math.pow(cmDistX, 2) + Math.pow(cmDistY, 2));
    }

    private boolean isSegmentDrawable() {
        return points.size() >= 2;
    }

    public float getDistance(){
        return dist;
    }

}
