package gpsExtractor.tools.trk;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 27.11.2008
 * Time: 22:02:07
 * To change this template use File | Settings | File Templates.
 */
public class TrackPoint {

    public double lat=0;
    public double lon=0;
    public Date time;
    public double elv=0;


    public TrackPoint(){
        lat=0;
        lon=0;
        time=new Date();
        elv=0;
    }

    public TrackPoint(double lat, double lon, Date time, double elv){
        this.lat=lat;
        this.lon=lon;
        this.time=time;
        this.elv=elv;
    }
    public TrackPoint(TrackPoint d){
        this.lat=d.lat;
        this.lon=d.lon;
        this.time=d.time;
        this.elv=d.elv;
    }

    public boolean equals(TrackPoint d){
        return (this.lat==d.lat&&this.lon==d.lon&&this.time==d.time&&this.elv==d.elv);
    }

    public String toString(){
        return "Lat "+this.lat + "; Lon "+this.lon + "; Elv "+this.elv;
    }

    public void print(){
        System.out.println("Lat "+this.lat);
        System.out.println("Lon "+this.lon);
        System.out.println("Elv "+this.elv);
    }


    public void set(TrackPoint d){
        this.lat=d.lat;
        this.lon=d.lon;
        this.elv=d.elv;
        this.time=d.time;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public Date getTime() {
        return time;
    }

    public double getElv() {
        return elv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrackPoint that = (TrackPoint) o;

        if (Double.compare(that.elv, elv) != 0) return false;
        if (Double.compare(that.lat, lat) != 0) return false;
        if (Double.compare(that.lon, lon) != 0) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = lat != +0.0d ? Double.doubleToLongBits(lat) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = lon != +0.0d ? Double.doubleToLongBits(lon) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (time != null ? time.hashCode() : 0);
        temp = elv != +0.0d ? Double.doubleToLongBits(elv) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

