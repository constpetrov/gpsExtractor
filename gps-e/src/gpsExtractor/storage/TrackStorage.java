package gpsExtractor.storage;

import gpsExtractor.tools.AbstractGPSParser;
import gpsExtractor.tools.gpx.GPXParser;
import gpsExtractor.tools.kml.KMLParser;
import gpsExtractor.tools.ozi.PLTReader;
import gpsExtractor.tools.trk.Track;
import gpsExtractor.tools.trk.TrackID;
import gpsExtractor.tools.trk.TrackPoint;
import gpsExtractor.tools.trk.Waypoint;

import java.io.File;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 07.04.2010
 * Time: 18:53:36
 * To change this template use File | Settings | File Templates.
 */
public final class TrackStorage {

    private TrackStorage(){}
    private final static Map<TrackID,Track> trackMap = new TreeMap<TrackID,Track>();
    private final static Set<Waypoint> waypoints = new HashSet<Waypoint>();

    public static void add(File f){
        if(f!=null && f.exists()){
            AbstractGPSParser parser;
            if(f.getName().toLowerCase().endsWith(".gpx")){
                parser = new GPXParser();
                parser.parseFile(f);
            }
            if(f.getName().toLowerCase().endsWith(".kml")){
                parser = new KMLParser();
                parser.parseFile(f);
            }
            if(f.getName().toLowerCase().endsWith(".plt")){
                PLTReader.readPLT(f);
            }

            LinkedList<TrackID> toRemove = new LinkedList<TrackID>();
            for(TrackID tid: trackMap.keySet()){
                if(trackMap.get(tid).getPoints().size() < 5){
                    toRemove.add(tid);
                }
            }
            for(TrackID tid: toRemove){
                trackMap.remove(tid);
            }
            toRemove.clear();
        }
    }
    public static void put(TrackID tid, Track t){
        trackMap.put(tid,t);
    }
    public static void remove(TrackID tid){
        trackMap.remove(tid);
    }

    @Deprecated
    public static Map<TrackID,Track> getTrackMap() {
        return trackMap;
    }

    public static Track getTrackByID(TrackID tid){
        if(tid != null && trackMap.containsKey(tid)){
            return trackMap.get(tid);    
        }
        return new Track();
    }

    public static Set<TrackID> getTrackIDs(){
        return trackMap.keySet();
    }

    public static Date getTrackStartTime(TrackID tid){
        return trackMap.get(tid).getStartTime();
    }

    public static int getTrackPointsSize(TrackID tid){
        return trackMap.get(tid).getSize();
    }

    public static TrackID joinTracks(Collection<TrackID> tids){
        Track t = new Track();
        Track buf;
        t.setTrackName("Join of "+tids.size()+" tracks");
        for(TrackID tid: tids){
            buf = getTrackByID(tid);
            for(TrackPoint p: buf.getPoints()){
                t.add(p);
            }
        }
        TrackID resID = new TrackID("combined", t.getTrackName(), t.getStartTime());
        put(resID, t);

        return resID;
    }

    public static void addWaypoint(Waypoint wp){
        if(waypoints.contains(wp)){
            throw new IllegalArgumentException("this waypoint already exist in the track!");
        } else {
            waypoints.add(wp);
        }
    }

    public static void removeWaypoint(Waypoint wp){
        waypoints.remove(wp);
    }

    public static void clearWaypoints(){
        waypoints.clear();
    }

    public static Set<Waypoint> getWaypoints(){
        return new HashSet<Waypoint>(waypoints);
    }
}
