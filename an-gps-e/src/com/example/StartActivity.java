package com.example;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.Calculator;
import gpsExtractor.tools.gpx.AnGPXWriter;
import gpsExtractor.tools.trk.Track;
import gpsExtractor.tools.trk.TrackID;
import gpsExtractor.tools.trk.TrackPoint;
import gpsExtractor.tools.trk.Waypoint;

import java.io.File;
import java.util.*;

public class StartActivity extends GpsActivity implements View.OnClickListener
{

    TextView wpList;
    Button addWp;
    Button saveWp;
    Button clearWp;
    Button startServiceButton;
    Button stopServiceButton;
    private Class rightActivity = RogainActivity.class;
    private Class leftActivity = TrainingActivity.class;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        wpList = (TextView)findViewById(R.id.wpList);
        addWp  = (Button)findViewById(R.id.addWpButton);
        addWp.setOnClickListener(this);
        saveWp  = (Button)findViewById(R.id.saveWpButton);
        saveWp.setOnClickListener(this);
        clearWp  = (Button)findViewById(R.id.clearWpButton);
        clearWp.setOnClickListener(this);

        View layout = findViewById(R.id.main_layout);
        layout.setOnTouchListener(this);

//        testTrackSave();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void createWpList() {
        StringBuilder builder = new StringBuilder("Coordinates: ");
        if (getCurrentLocation() != null){
            builder.append(getCurrentLocation().getLat()).append(", ").append(getCurrentLocation().getLon()).append("\n");
        } else{
            builder.append("\n");
        }
        if(GpsEngine.getEngine() != null){
            LinkedList<Waypoint> pList = GpsEngine.getEngine().getSomeWaypoints(6);
            if(getCurrentLocation() != null){
                while(pList.size() > 0){
                    Waypoint wp = pList.remove();
                    builder.append(Calculator.measureAzimuth(getCurrentLocation(), wp))
                            .append(" ")
                            .append(wp.getName())
                            .append(": ")
                            .append(wp.getLat())
                            .append(", ")
                            .append(wp.getLon())
                            .append("\n");
                }
            } else {
                while(pList.size() > 0){
                    Waypoint wp = pList.remove();
                    builder.append(" ")
                            .append(wp.getName())
                            .append(": ")
                            .append(wp.getLat())
                            .append(", ")
                            .append(wp.getLon())
                            .append("\n");
                }
            }
        }
        wpList.setText(builder.toString());
    }

    private void testTrackSave(){
        Track track = new Track();

        track.add(new TrackPoint(50.0, 37.0, new Date(), 100));
        track.add(new TrackPoint(50.1, 37.0, new Date(), 100));
        track.add(new TrackPoint(50.2, 37.0, new Date(), 100));
        track.add(new TrackPoint(50.3, 37.0, new Date(), 100));
        track.add(new TrackPoint(50.4, 37.0, new Date(), 100));
        TrackStorage.addWaypoint(new Waypoint("waypoint1", new TrackPoint(50.0, 37.0, new Date(), 100)));
        TrackStorage.addWaypoint(new Waypoint("waypoint2", new TrackPoint(50.2, 37.0, new Date(), 100)));
        track.setTrackName("Testing track");

        TrackID trackID = new TrackID(FILENAME, track.getTrackName(), track.getStartTime());
        TrackStorage.put(trackID, track);
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//" + GpsActivity.PROGRAM_MAIN_FOLDER);
        f.mkdir();
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "//" + GpsActivity.PROGRAM_MAIN_FOLDER + "//" + FILENAME + EXTENSION;
        AnGPXWriter writer = new AnGPXWriter((Collection<Track>) track, filePath, true);
        writer.run();
        
        TrackStorage.remove(trackID);
        TrackStorage.clearWaypoints();
        TrackStorage.add(new File(filePath));
        createWpList();

    }

    private void saveTrack(){
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//" + GpsActivity.PROGRAM_MAIN_FOLDER);

        if(!f.exists()) f.mkdir();

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "//" + GpsActivity.PROGRAM_MAIN_FOLDER + "//" + FILENAME;
        if(new File(filePath + EXTENSION).exists()){
            int i=2;
            while (new File(filePath + "_" + i + EXTENSION).exists()){
                i++;
            }
            filePath = filePath + "_" + i  + EXTENSION;
        }
//        AnGPXWriter writer = new AnGPXWriter(, filePath, true);
//        writer.run();
    }

    @Override
    public void onClick(View view) {
        Log.i("StartActivity/onClick","started");
        switch (view.getId()){
            case R.id.addWpButton:{
                if(getCurrentLocation() != null){
                    String name = "wp-" + (TrackStorage.getWaypoints().size() +1);
                    TrackStorage.addWaypoint(new Waypoint(name,
                            getCurrentLocation().getLat(),
                            getCurrentLocation().getLon()));
                }
                createWpList();
                break;
            }
            case R.id.saveWpButton:{
                saveTrack();
                break;
            }
            case R.id.clearWpButton:{
                TrackStorage.clearWaypoints();
                createWpList();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onGpsUpdate() {
        super.onGpsUpdate();
        createWpList();
    }

    public Class getRightActivity() {
        return rightActivity;
    }

    public Class getLeftActivity() {
        return leftActivity;
    }
}
