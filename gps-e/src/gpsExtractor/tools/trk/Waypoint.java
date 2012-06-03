package gpsExtractor.tools.trk;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 1/29/12
 * Time: 7:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class Waypoint extends TrackPoint{
    private final String name;
    public static final String TYPE="gpsExtractorWaypoint";
    
    public Waypoint(String name, TrackPoint point){
        super(point);
        this.name = name;
    }
    
    public Waypoint(String name, double lat, double lon){
        this(name, new TrackPoint(lat, lon, new Date(), 0));
    }

    public Waypoint(String name, double lat, double lon, double elv){
        this(name, new TrackPoint(lat, lon, new Date(), elv));
    }

    public Waypoint(String name, double lat, double lon, Date date){
        this(name, new TrackPoint(lat, lon, date, 0));
    }

    public Waypoint(String name, double lat, double lon, Date date, double elv){
        this(name, new TrackPoint(lat, lon, date, elv));
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Waypoint waypoint = (Waypoint) o;

        if (name != null ? !name.equals(waypoint.name) : waypoint.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
