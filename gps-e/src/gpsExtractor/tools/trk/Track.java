package gpsExtractor.tools.trk;

import gpsExtractor.storage.PreferencesHolder;
import gpsExtractor.tools.Calculator;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 10.04.2010
 * Time: 0:11:25
 * To change this template use File | Settings | File Templates.
 */
public class Track implements Comparable{
    private ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();
    private LinkedList<TrackSegment> segments = new LinkedList<TrackSegment>();
    private TrackSegment currentSegment;
//    private ArrayList<TrackSegmentPart> segmentParts = new ArrayList<TrackSegmentPart>();
    private String trackName;
    private TrackPoint mostEastPoint;
    private TrackPoint mostWestPoint;
    private TrackPoint mostNorthPoint;
    private TrackPoint mostSouthPoint;
    

    private TrackPoint bufPoint;

    private List gradientThresholds = new ArrayList<Double>();
    private Map<String, Double> categories = new HashMap<String, Double>();
    private TrackPoint startTrPoint;
    private long startTrTime;
    private long lastTrTime;
    private int lastTrPointIndex;

    public Track(){
        this.addSegment();
    }

    public void add(TrackPoint p){
        if(mostSouthPoint == null || p.lat < mostSouthPoint.lat){
            mostSouthPoint = new TrackPoint(p);
        }
        if(mostNorthPoint == null || p.lat > mostNorthPoint.lat){
            mostNorthPoint = new TrackPoint(p);
        }
        if(mostWestPoint == null || p.lon < mostWestPoint.lon){
            mostWestPoint = new TrackPoint(p);
        }
        if(mostEastPoint == null || p.lon > mostEastPoint.lon){
            mostEastPoint = new TrackPoint(p);
        }

        if (bufPoint != null){
            TrackSegmentPart segPart = new TrackSegmentPart(bufPoint, p);
            if(segPart.getSpeed() < PreferencesHolder.getMaxDistanceTwoPoints() &&
                    segPart.getTimeDelta() > PreferencesHolder.getMinTimeTwoPoints() &&
                    Math.abs(segPart.getElevDelta()) < PreferencesHolder.getMaxElevTwoPoints()){
                currentSegment.add(segPart);
                points.add(p);
                bufPoint = p;
            }else{
                addSegment();
                points.add(p);
                bufPoint = p;
            }
        }else{
            points.add(p);
            bufPoint = p;
        }

    }

    public void addSegment(){
        segments.add(new TrackSegment());
        currentSegment = segments.getLast();
    }
    


    public void removeOnePartSegments(){
        LinkedList<TrackSegment> toRemove = new LinkedList<TrackSegment>();
        for(TrackSegment seg: segments){
            if(seg.getSegmentParts().size() <=2){
                toRemove.add(seg);
            }
        }
        segments.removeAll(toRemove);
    }

    public void setUpGradients(List<Double> thresholds){
        if(thresholds != null && thresholds.size() != 0){
            this.gradientThresholds = thresholds;
        }else{
            this.gradientThresholds = Arrays.asList(-18d, -12d, -6d, -3d, 0d, 3d, 6d, 12d, 18d); //default thresholds
        }
    }

    public void setUpCategories(Map<String, Double> categories){
        if(categories != null && categories.size() != 0){
            this.categories = categories;
        }else{                                 //default categories
            this.categories.put("Stop",0.1);
            this.categories.put("Walk",8.0);
            this.categories.put("Bike",100d);
        }
    }

    public void clear(){
        points.clear();
        segments.clear();
    }

    public int getSize(){
        return points == null ? 0 : points.size();
    }

    public Date getStartTime(){
        return points.get(0).time;
    }
    public Date getEndTime(){
        return points.get(points.size()-1).time;
    }
    public TrackPoint getStartLocation(){
        return points.get(0);
    }
    public TrackPoint getEndLocation(){
        return points.get(points.size()-1);
    }

    public TrackPoint getMostEastPoint() {
        return mostEastPoint == null ? new TrackPoint() : mostEastPoint;
    }

    public TrackPoint getMostWestPoint() {
        return mostWestPoint == null ? new TrackPoint() : mostWestPoint;
    }

    public TrackPoint getMostNorthPoint() {
        return mostNorthPoint == null ? new TrackPoint() : mostNorthPoint;
    }

    public TrackPoint getMostSouthPoint() {
        return mostSouthPoint == null ? new TrackPoint() : mostSouthPoint;
    }

    public LinkedList<TrackSegment> getSegments() {
        return segments;
    }

    public TrackSegment getCurrentSegment() {
        return currentSegment;
    }

    public int compareTo(Object t){
        if (this==t){
            return 0;
        }
        if (this.equals(t)){
            return 0;
        }
        return this.points.size()>((Track)t).points.size()?1:-1;
    }

    public ArrayList<TrackPoint> getPoints() {
        return points;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackName() {
        return trackName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        if (points != null ? !points.equals(track.points) : track.points != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return points != null ? points.hashCode() : 0;
    }

    public long getTimeForPosition(TrackPoint point){
        long pSize = points.size();
        for (int i = lastTrPointIndex; i < pSize - 1; i++){
            TrackPoint point1 = points.get(i);
            TrackPoint point2 = points.get(i + 1);
            double distance = Calculator.measureAzimuth(point1, point2).getDistance();
            if(Calculator.measureAzimuth(point1, point).getDistance() < distance &&
                    Calculator.measureAzimuth(point2, point).getDistance() < distance){
                this.lastTrTime = point1.time.getTime();
                this.lastTrPointIndex = i;
                return this.lastTrTime;
            }
        }
        return this.lastTrTime;
    }

    public TrackPoint getPositionForTime(long time){
        long pSize = points.size();
        for (int i = lastTrPointIndex; i < pSize - 1; i++){
            long time0 = points.get(0).getTime().getTime();
            TrackPoint point1 = points.get(i);
            long time1 = point1.getTime().getTime();
            long time2 = points.get(i + 1).getTime().getTime();
            if(time >= time1 - time0 && time < time2 - time0){
                this.lastTrTime = time1;
                this.lastTrPointIndex = i;
                return point1;
            }
        }
        return new TrackPoint();
    }

    public void setStartTrPoint(TrackPoint point){
        this.startTrPoint = point;
        this.startTrTime = getTimeForPosition(point);
    }

}
