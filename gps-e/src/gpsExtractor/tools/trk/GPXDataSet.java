package gpsExtractor.tools.trk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Kostya
 * Date: 19.05.12
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class GPXDataSet {
    private final List<Waypoint> waypoints = new ArrayList<Waypoint>();
    private final List<Route> routes = new ArrayList<Route>();
    private final List<Track> tracks = new ArrayList<Track>();

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Track> getTracks() {
        return tracks;
    }
    
    public int addWaypoint(Waypoint wp){
        this.waypoints.add(wp);
        return this.waypoints.size()-1;
    }

    public int addTrack(Track tr){
        this.tracks.add(tr);
        return this.tracks.size()-1;
    }

    public int addRoute(Route rt){
        this.routes.add(rt);
        return this.routes.size()-1;
    }
}
