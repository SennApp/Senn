package edu.konan_univ.senn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomView extends View {
    List<PointF> points = new ArrayList<>();
    Paint paint=new Paint();
    boolean drawing=true;
    double dist=0;

    public CustomView(Context context,@Nullable AttributeSet attrs) {
        super(context,attrs);
    }
    @Override
    protected void onDraw(@NonNull Canvas canvas){
        if(points.size()<2){
            return;
        }
        paint.setStrokeWidth(5);
        for(int i=0;i<points.size()-1;i++) {
                canvas.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, paint);
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF point=new PointF(event.getX(),event.getY());
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                performClick();
                drawing=false;
                Log.d("kyori",String.valueOf(dist));
                break;
            case MotionEvent.ACTION_MOVE:
                if (drawing) {
                    points.add(point);//点を配列に追加
                    if(points.size()>=2) {
                        dist += calcDist(points.get(points.size() - 1), points.get(points.size() - 2));
                    }
                    invalidate();
                }
                break;
        }
        return true;
    }

    private double calcDist(PointF a,PointF b){
        double ans=0;
        Log.d("zahyouX",String.valueOf(a.x));
        Log.d("zahyouY",String.valueOf(a.y));
        ans=Math.sqrt(Math.pow((a.x-b.x)*getScaleX(),2)+Math.pow((a.y-b.y)*getScaleY(),2));
        ans*=0.0264583333;
        return ans;
    }

}
