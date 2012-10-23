package com.example;

import java.text.DecimalFormat;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by IntelliJ IDEA. User: kpetrov Date: 2/12/12 Time: 3:15 PM To change
 * this template use File | Settings | File Templates.
 */
@TargetApi(3)
public class RogainActivity extends GpsActivity {
    RogainLayout layout;

    TextView testText;

    private Class rightActivity = TrainingActivity.class;

    private Class leftActivity = StartActivity.class;

    DecimalFormat format = new DecimalFormat("#.0");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rogain);
        layout = (RogainLayout) findViewById(R.id.rog_lay);
        layout.invalidate();
        layout.setOnTouchListener(this);

        layout.setWindowParameters(0,
            getResources().getDisplayMetrics().heightPixels
                - getStatusBarHeight() - getTitleBarHeight(), 0, getResources()
                .getDisplayMetrics().widthPixels);

        testText = (TextView) findViewById(R.id.testText);
        testText.setText("Width = "
            + getResources().getDisplayMetrics().widthPixels + ", height = "
            + getResources().getDisplayMetrics().heightPixels
            + ", status bar height = " + getStatusBarHeight()
            + ", window title height = " + getTitleBarHeight());
        Rect r = new Rect();
        Window w = getWindow();
        w.getDecorView().getWindowVisibleDisplayFrame(r);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        testText.setText("Width = "
            + getResources().getDisplayMetrics().widthPixels + ", height = "
            + getResources().getDisplayMetrics().heightPixels
            + ", status bar height = " + getStatusBarHeight()
            + ", window title height = " + getTitleBarHeight());
        layout.setWindowParameters(0,
            getResources().getDisplayMetrics().heightPixels
                - getStatusBarHeight() - getTitleBarHeight(), 0, getResources()
                .getDisplayMetrics().widthPixels);
        layout.invalidate();
        testText.setText(createMovementParametersList());
        super.onTouch(view, motionEvent);
        return true;
    }

    @Override
    protected Class getRightActivity() {
        return rightActivity;
    }

    @Override
    protected Class getLeftActivity() {
        return leftActivity;
    }

    @Override
    public void onGpsUpdate() {
        super.onGpsUpdate();
        testText.setText(createMovementParametersList());
    }

    private String createMovementParametersList() {
        StringBuilder builder = new StringBuilder();
        if (GpsEngine.getEngine() != null) {
            builder.append("Speed ")
                .append(format.format(GpsEngine.getEngine().getCurrentSpeed()))
                .append(" km/h").append("\n");
            builder.append("Distance ")
                .append((int) GpsEngine.getEngine().getTripDistance())
                .append(" meters").append("\n");
            builder.append("Time spent ")
                .append(formatTime(GpsEngine.getEngine().getTripTime()))
                .append("\n");
            builder.append("Average speed ")
                .append(format.format(GpsEngine.getEngine().getAverageSpeed()))
                .append(" km/h").append("\n");
        }
        return builder.toString();
    }

    public int getStatusBarHeight() {
        Rect r = new Rect();
        Window w = getWindow();
        w.getDecorView().getWindowVisibleDisplayFrame(r);
        return r.top;
    }

    public int getTitleBarHeight() {
        int viewTop =
            getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return (viewTop - getStatusBarHeight());
    }

}