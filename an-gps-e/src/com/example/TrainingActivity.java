package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import gpsExtractor.tools.trk.Azimuth;

import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: kpetrov
 * Date: 07.05.12
 * Time: 18:36
 * To change this template use File | Settings | File Templates.
 */
public class TrainingActivity extends GpsActivity{
    private static final int START_RECORD_ITEM = FIRST_MENU_ID + 2;
    private static final int STOP_RECORD_ITEM = FIRST_MENU_ID + 3;
    private static final int CLEAR_RECORD_ITEM = FIRST_MENU_ID + 4;
    private static final int SAVE_RECORD_ITEM = FIRST_MENU_ID + 5;
    private Class rightActivity = StartActivity.class;
    private Class leftActivity = RogainActivity.class;
    private TextView speed;
    private TextView time;
    private TextView distance;
    private TextView avgSpeed;
    private TextView recTime;
    private TextView recDistance;
    DecimalFormat format = new DecimalFormat("#.0");

    @Override
    protected Class getLeftActivity() {
        return leftActivity;
    }

    @Override
    protected Class getRightActivity() {
        return rightActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training);
        speed       = (TextView)findViewById(R.id.trainingSpeed);
        time        = (TextView)findViewById(R.id.trainingTime);
        distance    = (TextView)findViewById(R.id.trainingDistance);
        avgSpeed    = (TextView)findViewById(R.id.trainingAvgSpeed);
        recTime     = (TextView)findViewById(R.id.trainingRecTime);
        recDistance = (TextView)findViewById(R.id.trainingRecDistance);

    }

    @Override
    public void onGpsUpdate() {
        super.onGpsUpdate();

        speed.setText(format.format(GpsEngine.getEngine().getCurrentSpeed()) + " km/h");
        time.setText(formatTime(GpsEngine.getEngine().getTripTime()));
        distance.setText(format.format(GpsEngine.getEngine().getTripDistance()) + " m");
        avgSpeed.setText(format.format(GpsEngine.getEngine().getAverageSpeed()) + " km/h");
        recTime.setText(formatTime(GpsEngine.getEngine().getRecordDistance().getTimeTotal()));
        recDistance.setText(format.format(GpsEngine.getEngine().getRecordDistance().getDistance()) + " m");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuItem m;

        m = menu.add(0, START_RECORD_ITEM, 0, R.string.startRec);
        m.setIcon(android.R.drawable.ic_menu_add);
        m.setEnabled(GpsEngine.canStart());

        m = menu.add(0, STOP_RECORD_ITEM, 0, R.string.stopRec);
        m.setIcon(android.R.drawable.ic_menu_crop);
        m.setEnabled(GpsEngine.canStop());

        m = menu.add(0, CLEAR_RECORD_ITEM, 0, R.string.clearRec);
        m.setIcon(android.R.drawable.ic_menu_delete);
        m.setEnabled(GpsEngine.canClear());

        m = menu.add(0, SAVE_RECORD_ITEM, 0, R.string.saveRec);
        m.setIcon(android.R.drawable.ic_menu_save);
        m.setEnabled(GpsEngine.canSave());

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        return super.onPrepareOptionsMenu(menu);
        menu.findItem(START_RECORD_ITEM).setEnabled(GpsEngine.canStart());
        menu.findItem(STOP_RECORD_ITEM).setEnabled(GpsEngine.canStop());
        menu.findItem(CLEAR_RECORD_ITEM).setEnabled(GpsEngine.canClear());
        menu.findItem(SAVE_RECORD_ITEM).setEnabled(GpsEngine.canSave());

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case START_RECORD_ITEM:{
                if(GpsEngine.isEngineExists()) GpsEngine.getEngine().startRecording();
                return true;
            }
            case STOP_RECORD_ITEM:{
                if(GpsEngine.isEngineExists()) GpsEngine.getEngine().stopRecording();
                return true;
            }
            case CLEAR_RECORD_ITEM:{
                if(GpsEngine.isEngineExists()) GpsEngine.getEngine().clearRecord();
                return true;
            }
            case SAVE_RECORD_ITEM:{
                if(GpsEngine.isEngineExists()) {
                    String result = GpsEngine.getEngine().saveRecordToFile();
                    Toast.makeText(this, "Track saved to " + result, Toast.LENGTH_LONG);
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
