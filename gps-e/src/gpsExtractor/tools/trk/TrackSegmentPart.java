package gpsExtractor.tools.trk;

import gpsExtractor.tools.Calculator;

/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 22.08.2010
 * Time: 17:34:49
 * To change this template use File | Settings | File Templates.
 */
public class TrackSegmentPart {
    private final TrackPoint startPoint;//in lat lon elev time
    private final TrackPoint endPoint;  //in lat lon elev time
    private final Double pathDelta;     //meters
    private final Double elevDelta;     //meters
    private final long timeDelta;       //milliseconds
    private final Double gradient;      //tan
    private final Double speed;         //km/h
    private int gradientCategory;       //number of category
    private int speedCategory;          //number of category


    public TrackSegmentPart(TrackPoint start, TrackPoint end){
        this.startPoint = start;
        this.endPoint = end;

        this.pathDelta = Calculator.measureAzimuth(startPoint, endPoint).getDistance();
        this.elevDelta = Calculator.measureElev(startPoint, endPoint);
        this.timeDelta = Calculator.measureTime(startPoint, endPoint);
        this.gradient = (elevDelta / pathDelta) * 100;
        this.speed = (pathDelta / timeDelta) * 3600;//because timeDelta is in milliseconds
    }

    public TrackPoint getStartPoint() {
        return startPoint;
    }

    public TrackPoint getEndPoint() {
        return endPoint;
    }

    public Double getPathDelta() {
        return pathDelta;
    }

    public Double getElevDelta() {
        return elevDelta;
    }

    public long getTimeDelta() {
        return timeDelta;
    }

    public Double getGradient() {
        return gradient;
    }

    public Double getSpeed() {
        return speed;
    }

    public int getGradientCategory() {
        return gradientCategory;
    }

    public void setGradientCategory(int gradientCategory) {
        this.gradientCategory = gradientCategory;
    }

    public int getSpeedCategory() {
        return speedCategory;
    }

    public void setSpeedCategory(int speedCategory) {
        this.speedCategory = speedCategory;
    }

    @Override
    public String toString() {
        return "TrackSegmentPart{" +
                "startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", pathDelta=" + pathDelta +
                ", elevDelta=" + elevDelta +
                ", timeDelta=" + timeDelta +
                '}';
    }
}
