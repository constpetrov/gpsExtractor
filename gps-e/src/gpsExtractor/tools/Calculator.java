package gpsExtractor.tools;

import gpsExtractor.storage.PreferencesHolder;
import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.trk.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 22.08.2010
 * Time: 16:18:13
 * To change this template use File | Settings | File Templates.
 */
public final class Calculator {
    final private static double r=6366197.7236758134307553505349006;
    private static double minBikeSpeed;
    private static double stopSpeed;
    private Calculator(){

    }

    public static ShortTrackInfo getShortTrackInfo(Track track){
        Double distance = 0d;
        long timeTotal = 0;
        long timeMove = 0;
        Double avgTotal = 0d;
        Double avgMove = 0d;
        Double uphillTotal = 0d;
        Double downhillTotal = 0d;
        Double maxSpeed = 0d;
        LinkedList<TrackSegment> segments = track.getSegments();
        Double minHeight = 100000d;
        Double maxHeight = -100000d;
        for (TrackSegment seg: segments){
            for (TrackSegmentPart s: seg.getSegmentParts()){
                distance += s.getPathDelta();

                if(s.getStartPoint().getElv() < minHeight){
                    minHeight = s.getStartPoint().getElv();
                }

                if(s.getStartPoint().getElv() > maxHeight){
                    maxHeight = s.getStartPoint().getElv();
                }

                if(s.getElevDelta() > 0){
                    uphillTotal += s.getElevDelta();
                }else{
                    downhillTotal += s.getElevDelta();
                }

                if(s.getSpeed() > 0.1){
                    timeTotal += s.getTimeDelta();
                    timeMove += s.getTimeDelta();
                }else{
                    timeTotal += s.getTimeDelta();
                }
                if(s.getSpeed() > maxSpeed){
                    maxSpeed = s.getSpeed();
                }
            }
        }
        if(segments != null && segments.size() != 0 && segments.getLast().getSegmentParts() != null && segments.getLast().getSegmentParts().size() != 0){
            TrackPoint totalEndPoint = segments.getLast().getSegmentParts().getLast().getEndPoint();
            if(totalEndPoint.getElv() > maxHeight){
                maxHeight = totalEndPoint.getElv();
            }
            if(totalEndPoint.getElv() < minHeight){
                minHeight = totalEndPoint.getElv();
            }

            if (timeTotal != 0){
                avgTotal = (distance/timeTotal) * 3600;
            }
            if (timeMove != 0){
                avgMove = (distance/timeMove) * 3600;
            }
        }
        return new ShortTrackInfo(distance, timeTotal, timeMove, avgTotal, avgMove, uphillTotal, downhillTotal, maxHeight, minHeight, maxSpeed);
    }

    public static ShortTrackInfo getShortTrackInfo(TrackID tid){
        return getShortTrackInfo(TrackStorage.getTrackByID(tid));
    }

    public static ArrayList<ShortTrackInfo> getShortTotalInfo(Collection<TrackID> tracks){
        Double distance = 0d;
        long timeTotal = 0;
        long timeMove = 0;
        Double avgTotal = 0d;
        Double avgMove = 0d;
        Double uphillTotal = 0d;
        Double downhillTotal = 0d;
        Double minHeight = 100000d;
        Double maxHeight = -100000d;
        Double maxSpeed = -100000d;
        ShortTrackInfo info;
        ArrayList<ShortTrackInfo> result = new ArrayList<ShortTrackInfo>(0);
        for(TrackID tid: tracks){
            info = Calculator.getShortTrackInfo(tid);
            result.add(info);
            distance += info.getDistance();
            timeTotal += info.getTimeTotal();
            timeMove += info.getTimeMove();
            avgTotal = (distance/timeTotal) * 3600;
            avgMove = (distance/timeMove) * 3600;
            uphillTotal += info.getUphillTotal();
            downhillTotal += info.getDownhillTotal();
            if(info.getMaxHeight() > maxHeight){
                maxHeight = info.getMaxHeight();
            }

            if(info.getMinHeight() < minHeight){
                minHeight = info.getMinHeight();
            }
            if(info.getMaxSpeed() > maxSpeed){
                maxSpeed = info.getMaxSpeed();
            }
        }
        if(tracks.size() > 1){
            result.add( new ShortTrackInfo(distance, timeTotal, timeMove, avgTotal, avgMove, uphillTotal, downhillTotal, maxHeight, minHeight, maxSpeed));
        }
        return result;
    }

    private static void recreateSegments(TrackID tid){
        Track oldTrack = TrackStorage.getTrackByID(tid);
        Track newTrack = new Track();
        boolean onBikeNow = false;
        for(TrackSegment seg: oldTrack.getSegments()){
            for(TrackSegmentPart segP: seg.getSegmentParts()){
                if (segP.getSpeed() < PreferencesHolder.getStopSpeed()){
                    continue;
                }
                if (PreferencesHolder.getStopSpeed() < segP.getSpeed() &&
                        segP.getSpeed() < PreferencesHolder.getMinBikeSpeed()){
                    if(onBikeNow){
                        newTrack.addSegment();
                        newTrack.add(segP.getStartPoint());
                        newTrack.add(segP.getEndPoint());
                        onBikeNow = false;
                    }else{
                        newTrack.add(segP.getEndPoint());
                    }
                }
                if (segP.getSpeed() > PreferencesHolder.getMinBikeSpeed()){
                    if(onBikeNow){
                        newTrack.add(segP.getEndPoint());
                    }else{
                        newTrack.addSegment();
                        newTrack.add(segP.getStartPoint());
                        newTrack.add(segP.getEndPoint());
                        onBikeNow = true;
                    }
                }
            }
        }
        TrackStorage.remove(tid);
        TrackID newID = new TrackID(tid.getFileName(),tid.getTrackName(),newTrack.getStartTime());
        TrackStorage.put(newID, newTrack);
    }

    public static void recreateSegments(Collection<TrackID> tids){
        for(TrackID tid: tids){
            recreateSegments(tid);
        }
    }

    static public Double measureElev(TrackPoint d1, TrackPoint d2){
        Double delta;

        delta=(d2.elv-d1.elv);    //because of feets in gps

        return delta;
    }

    static public long measureTime(TrackPoint d1, TrackPoint d2){
        long delta;
        long t1,t2;

        t1=d1.time.getTime();
        t2=d2.time.getTime();
        delta=Math.abs(t2-t1);
        return delta;
    }

    static public Azimuth measureAzimuth(TrackPoint start, TrackPoint end){
        final double pi = Math.PI;
        
        
        double lat1 = start.getLat()*pi/180.0;
        double lat2 = end.getLat()*pi/180.0;
        double long1 = start.getLon()*pi/180.0;
        double long2 = end.getLon()*pi/180.0;

        double cl1 = Math.cos(lat1);
        double cl2 = Math.cos(lat2);
        double sl1 = Math.sin(lat1);
        double sl2 = Math.sin(lat2);
        double delta = long2 - long1;
        double cdelta = Math.cos(delta);
        double sdelta = Math.sin(delta);

        double p1 = Math.pow((cl2 * sdelta), 2);
        double p2 = Math.pow(((cl1*sl2) - (sl1*cl2*cdelta)),2);
        double p3 = Math.pow((p1 + p2),0.5);
        double p4 = sl1*sl2;
        double p5 = cl1*cl2*cdelta;
        double p6 = p4 + p5;
        double p7 = p3/p6;
        double anglerad = Math.atan(p7);
        double dist = anglerad*r;

        double x = (cl1*sl2) - (sl1*cl2*cdelta);
        double y = sdelta*cl2;
        double z = (Math.atan(-y/x)) * 180.0 / pi;

        if (x < 0){
            z = z + 180.0;
        }

        double z2 = (z+180.) % 360. - 180.;
        z2 = - (z2)*pi/180.;
        double anglerad2 = z2 - ((2*pi)*Math.floor((z2/(2*pi))));
        double angledeg = (anglerad2*180.)/pi;

        return new Azimuth(angledeg, dist);

        
        /*double delta, angle;
        double x1,x2,y1,y2,z1,z2;

        x1=r*Math.cos(start.lat*Math.PI/180.0)*Math.cos(start.lon*Math.PI/180.0);
        y1=r*Math.cos(start.lat*Math.PI/180.0)*Math.sin(start.lon*Math.PI/180.0);
        z1=r*Math.sin(start.lat*Math.PI/180.0);

        x2=r*Math.cos(end.lat*Math.PI/180.0)*Math.cos(end.lon*Math.PI/180.0);
        y2=r*Math.cos(end.lat*Math.PI/180.0)*Math.sin(end.lon*Math.PI/180.0);
        z2=r*Math.sin(end.lat*Math.PI/180.0);

        delta=Math.pow((Math.pow(x2-x1,2.0)+Math.pow(y2-y1,2.0)+Math.pow(z2-z1,2.0)),0.5);
        angle = Math.atan((x2-x1)/(y2-y1));

        return new Azimuth(angle, delta);*/
    }

    static public void setGradientCategories(TrackID tid){
        for(TrackSegment segment: TrackStorage.getTrackByID(tid).getSegments()){
            for(TrackSegmentPart part: segment.getSegmentParts()){
                int i=0;
                while(i < PreferencesHolder.getGradientThresholds().size() && Math.abs(part.getGradient()) > PreferencesHolder.getGradientThresholds().get(i)){
                    i++;
                }
                part.setGradientCategory(PreferencesHolder.getGradientThresholds().size()-i);
            }
        }
    }

    static public void setSpeedCategories(TrackID tid){
        for(TrackSegment segment: TrackStorage.getTrackByID(tid).getSegments()){
            for(TrackSegmentPart part: segment.getSegmentParts()){
                int i=0;
                while(i < PreferencesHolder.getSpeedThresholds().size() && part.getSpeed()>PreferencesHolder.getSpeedThresholds().get(i)){
                    i++;
                }
                part.setSpeedCategory(i);
            }
        }
    }
}
