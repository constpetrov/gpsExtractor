package com.example;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.trk.Waypoint;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Kostya
 * Date: 12.02.12
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public class RogainLayout extends View {
    private int wTop, wBottom, wLeft, wRight;
    public RogainLayout(Context context) {
        super(context);
    }

    public RogainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RogainLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    public void onDraw(Canvas c){
        Set<Waypoint> waypoints = TrackStorage.getWaypoints();
        Paint mPaint = new Paint();
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        c.drawRect(wLeft, wTop, wRight-1, wBottom-1, mPaint);

//        c.drawCircle((float)Math.random()*200, (float)Math.random()*200, 150f, mPaint);
    }
    
    public void setWindowParameters(int wTop, int wBottom, int wLeft, int wRight){
        this.wTop = wTop;
        this.wBottom = wBottom;
        this.wLeft = wLeft;
        this.wRight = wRight;
    }
}
