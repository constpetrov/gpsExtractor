package gpsExtractor.tools.trk;

import gpsExtractor.tools.Calculator;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Kostya
 * Date: 19.05.12
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class Route {
    private String name;
    private List<Waypoint> waypoints;

    public Route(String name, List<Waypoint> waypoints) {
        this.name = name;
        this.waypoints = waypoints;
    }

    public Route(List<Waypoint> waypoints) {
        this("Empty name", waypoints);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public Waypoint getNearestWaypoint(TrackPoint target){
        double distance = Double.MAX_VALUE;
        double distanceCandidate = distance;
        Waypoint result = null;
        for(Waypoint point: waypoints){
            distanceCandidate = Calculator.measureAzimuth(target, point).getDistance();
            if(distanceCandidate < distance){
                result = point;
                distance = distanceCandidate;
            }
        }
        return result;
    }
}
