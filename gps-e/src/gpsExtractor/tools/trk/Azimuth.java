package gpsExtractor.tools.trk;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 1/30/12
 * Time: 9:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class Azimuth {
    private final double angle;
    private final double distance;
    private final static DecimalFormat tenKmFormat = new DecimalFormat("0.00");
    private final static DecimalFormat hundredKmFormat = new DecimalFormat("00.0");
    private final static DecimalFormat thousandKmFormat = new DecimalFormat("000");

    static {
        tenKmFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
        hundredKmFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
        thousandKmFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
    }

    public Azimuth(double angle, double distance) {
        this.angle = angle % 360;
        this.distance = distance;
    }

    public double getAngle() {
        return angle;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString(){
        return this.printDirection() + ", " + this.printDistance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Azimuth azimuth = (Azimuth) o;

        if (Double.compare(azimuth.angle, angle) != 0) return false;
        if (Double.compare(azimuth.distance, distance) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = angle != +0.0d ? Double.doubleToLongBits(angle) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = distance != +0.0d ? Double.doubleToLongBits(distance) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    private String printDirection(){
        int i = (int)((angle+12.25)/22.5);
        String[] direction = {"N",
                "NNE",
                "NE",
                "NEE",
                "E",
                "SEE",
                "SE",
                "SSE",
                "S",
                "SSW",
                "SW",
                "SWW",
                "W",
                "NWW",
                "NW",
                "NNW"};
        return direction[i];
    }

    private String printDistance(){
        if (distance < 1000){
            return ""+(long)distance + "m";
        }

        if (distance < 10000){
            return tenKmFormat.format(distance/1000.0) + "km";
        }

        if (distance < 100000){
            return hundredKmFormat.format(distance/1000.0) + "km";
        }

        return "" + (int)(distance/1000.0) + "km";
    }
}
