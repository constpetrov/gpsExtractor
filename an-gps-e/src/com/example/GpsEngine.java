package com.example;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;
import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.Calculator;
import gpsExtractor.tools.gpx.AnGPXWriter;
import gpsExtractor.tools.trk.*;

import java.io.File;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: KPETROV
 * Date: 14.03.12
 * Time: 10:39
 * To change this template use File | Settings | File Templates.
 */
public class GpsEngine extends Service implements LocationListener, MyConstants {
    private TrackPoint currentLocation;
    private TrackPoint previousLocation;
    private static Track currentTrack;
    private LocationManager locationManager;
    private final Object locationModLock = new Object();
    private static GpsEngine engine;
    private static final Set<GpsEngineListener> listeners = new HashSet<GpsEngineListener> ();
    private double tripDistance;
    private long tripTime;
    private double averageSpeed;
    private static boolean RECORD_STARTED;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 5, this);
        engine = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    public double getTripDistance() {
        return tripDistance;
    }

    public long getTripTime() {
        return tripTime;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    @Override
    public void onLocationChanged(Location location) {
        synchronized (locationModLock){
            this.previousLocation = this.currentLocation;
            this.currentLocation = new TrackPoint(location.getLatitude(), location.getLongitude(), new Date(location.getTime()), location.getAltitude());
            if(previousLocation != null){
                tripDistance += Calculator.measureAzimuth(previousLocation, currentLocation).getDistance();
                tripTime += Calculator.measureTime(previousLocation, currentLocation);
                averageSpeed = (tripDistance / tripTime) * 3600;
            }
            if (RECORD_STARTED){
                this.currentTrack.add(currentLocation);
            }
        }
        notifyListeners();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(getApplicationContext(), "GPS ON",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(getApplicationContext(), "GPS OFF",
                Toast.LENGTH_SHORT).show();
    }

    public TrackPoint getCurrentLocation() {
        synchronized (locationModLock){
            return currentLocation == null ? null : new TrackPoint(currentLocation);
        }
    }
    
    public Azimuth getCurrentAzimuth(){
        synchronized (locationModLock){
            return Calculator.measureAzimuth(previousLocation, currentLocation);
        }
    }

    public double getCurrentSpeed(){
        synchronized (locationModLock){
            if(previousLocation != null && currentLocation != null){
                return new TrackSegmentPart(previousLocation, currentLocation).getSpeed();
            } else return 0;
        }
    }

    public static GpsEngine getEngine() {
        return engine;
    }

    public static void addGpsEngineListener(GpsEngineListener listener){
        listeners.add(listener);
    }

    public static void removeGpsEngineListener(GpsEngineListener listener){
        listeners.remove(listener);
    }

    private static void notifyListeners(){
        for(GpsEngineListener listener: listeners){
            if(listener != null){
                listener.onGpsUpdate();
            }
        }
    }
    
    public LinkedList<Waypoint> getSomeWaypoints(int number){
        LinkedList<Waypoint> pList = new LinkedList<Waypoint>();
        LinkedList<Waypoint> points = new LinkedList<Waypoint>();
        points.addAll(TrackStorage.getWaypoints());
        int initialSize = points.size();
        if(getCurrentLocation() != null && points.size() != 0){
            Waypoint pointToAdd = points.getFirst();
            double minimumDistance = Calculator.measureAzimuth(getCurrentLocation(), points.getFirst()).getDistance();
            for(int i = 0; i < number && i < initialSize; i++){
                for(Waypoint p: points){
                    if(Calculator.measureAzimuth(getCurrentLocation(), p).getDistance() < minimumDistance){
                        minimumDistance = Calculator.measureAzimuth(getCurrentLocation(), p).getDistance();
                        pointToAdd = p;
                    }
                }
                pList.add(pointToAdd);
            }
        } else {
            Collections.sort(points, new Comparator<Waypoint>() {
                @Override
                public int compare(Waypoint waypoint, Waypoint waypoint1) {
                    return waypoint.getName().compareTo(waypoint1.getName());
                }
            });
            for(int i = 0; i < number && i < initialSize; i++){
                pList.add(points.remove());
            }
        }
        return pList;
    }

    public void startRecording(){
        if(currentTrack == null){
            currentTrack = new Track();
            currentTrack.setTrackName("an-gps-e testing track");
        }
        RECORD_STARTED = true;
    }

    public void stopRecording(){
        RECORD_STARTED = false;
    }

    public void clearRecord(){
        if(currentTrack != null){
            currentTrack = null;
        }
    }

    public ShortTrackInfo getRecordDistance(){
        if(currentTrack == null) return new ShortTrackInfo(0d,0,0,0d,0d,0d,0d,0d,0d,0d);
        return Calculator.getShortTrackInfo(currentTrack);
    }

    public String saveRecordToFile(){
        if(currentTrack == null) return "";
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//" + PROGRAM_MAIN_FOLDER);
        if(!f.exists()) f.mkdir();


        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "//" + PROGRAM_MAIN_FOLDER + "//" + FILENAME;
        if(new File(filePath + EXTENSION).exists()){
            int i=2;
            while (new File(filePath + "_" + i + EXTENSION).exists()){
                i++;
            }
            filePath = filePath + "_" + i  + EXTENSION;
        }
        AnGPXWriter writer = new AnGPXWriter(currentTrack, filePath, true);
        writer.run();
        return filePath;
    }

    public static boolean isEngineExists(){
        return engine != null;
    }

    public static boolean canStart(){
        return !RECORD_STARTED;
    }

    public static boolean canStop(){
        return RECORD_STARTED;
    }

    public static boolean canClear(){
        return currentTrack!=null && !RECORD_STARTED;
    }

    public static boolean canSave(){
        return currentTrack!= null;
    }
}
