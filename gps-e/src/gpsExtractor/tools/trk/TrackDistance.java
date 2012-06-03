package gpsExtractor.tools.trk;

import gpsExtractor.tools.gpx.*;

/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 08.12.2008
 * Time: 19:23:45
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class TrackDistance {
    TrackPart all;
    TrackPart onBike;
    TrackPart onFoot;
    TrackPart stop;

    final double gradMin;
    final double gradMax;

    private TrackDistance(){
        this.gradMin = 0;
        this.gradMax = 0;
        all = new TrackPart();
        onBike = new TrackPart();
        onFoot = new TrackPart();
        stop = new TrackPart();
    }

    public TrackDistance(double gMin, double gMax){
        this.gradMin = gMin;
        this.gradMax = gMax;
        all = new TrackPart();
        onBike = new TrackPart();
        onFoot = new TrackPart();
        stop = new TrackPart();
    }

    public boolean testGrad (GarminDelta d){

        return (d.elevDist/d.pathDist>gradMin/100.0&&d.elevDist/d.pathDist<=gradMax/100.0);
    }

    public void addDelta(GarminDelta d, double minBikeSpeed, double stopSpeed){
        all.addPart(d);
        if (d.pathDist/((double)d.time/1000)>minBikeSpeed/3.6){
            onBike.addPart(d);
        }
        else
        if(d.pathDist/((double)d.time/1000)>stopSpeed/3.6){
            onFoot.addPart(d);
        }
        else {
            stop.addPart(d);
        }
    }
}
