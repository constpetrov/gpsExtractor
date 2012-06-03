package com.example;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.*;
import android.widget.Toast;
import gpsExtractor.tools.trk.TrackPoint;

public abstract class GpsActivity extends Activity implements MyConstants, GpsEngineListener, View.OnTouchListener, View.OnKeyListener{

    TrackPoint currentLocation;
    public static final int FIRST_MENU_ID = Menu.FIRST;
    public static final int REFRESH_MENU_ITEM = FIRST_MENU_ID + 0;
    public static final int EXIT_MENU_ITEM = FIRST_MENU_ID + 1;

    public TrackPoint getCurrentLocation() {
        return currentLocation;
    }

    private float x, y;


    @Override
    protected void onResume() {
        super.onResume();
        if (GpsEngine.getEngine() != null) {
            currentLocation = GpsEngine.getEngine().getCurrentLocation();
            GpsEngine.addGpsEngineListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GpsEngine.removeGpsEngineListener(this);
    }

    @Override
    public void onGpsUpdate() {
        currentLocation = GpsEngine.getEngine().getCurrentLocation();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                x = motionEvent.getX();
                y = motionEvent.getY();
                break;
            }

            case MotionEvent.ACTION_UP: {
                if ((motionEvent.getX() - x) > 200 && Math.abs(motionEvent.getY() - y) < 150) {
//                    Toast.makeText(this, "Right slide", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, this.getRightActivity()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                if ((x - motionEvent.getX()) > 200 && Math.abs(motionEvent.getY() - y) < 150) {
//                    Toast.makeText(this, "Left slide", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, this.getLeftActivity()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                break;
            }
        }


        return true;
    }

    protected abstract Class getLeftActivity();

    protected abstract Class getRightActivity();

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuItem m;
        
        if(this instanceof StartActivity){
            m = menu.add(0, EXIT_MENU_ITEM, 0, R.string.close);
            m.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        }
        
        m = menu.add(0, REFRESH_MENU_ITEM, 0, R.string.refresh);
        m.setIcon(android.R.drawable.ic_menu_compass);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case REFRESH_MENU_ITEM:
                startService(new Intent(this, GpsEngine.class));
                GpsEngine.addGpsEngineListener(this);
                return true;
            case EXIT_MENU_ITEM:
                GpsEngine.removeGpsEngineListener(this);
                stopService(new Intent(this, GpsEngine.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected static String formatTime(long millis){
        long hours = millis / 3600000;
        long minutes = (millis % 3600000) / 60000;
        long seconds = ((millis % 3600000) % 60000) / 1000;
        return ""+hours+":"+ (minutes<10 ? "0"+minutes : minutes) +":"+(seconds<10 ? "0"+seconds : seconds);
    }
}