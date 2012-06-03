package gpsExtractor.tools.trk;

import gpsExtractor.tools.Calculator;
import org.junit.Test;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 1/30/12
 * Time: 10:10 PM
 * To change this template use File | Settings | File Templates.
 */


public class AzimuthTest {
    @Test
    public void testAzimuth(){
        Waypoint wp1 = new Waypoint("test", 1, 1);
        Waypoint wp2 = new Waypoint("test", -2, -1);
        Waypoint wp3 = new Waypoint("test", 90, 0);
        Waypoint wp4 = new Waypoint("test", 0, 90);
        TrackPoint current = new TrackPoint(0.0, 0.0, new Date(), 0);
        Azimuth azimuth1 = Calculator.measureAzimuth(current, wp1);
        Azimuth azimuth2 = Calculator.measureAzimuth(current, wp2);
        Azimuth azimuth3 = Calculator.measureAzimuth(current, wp3);
        Azimuth azimuth4 = Calculator.measureAzimuth(current, wp4);
        System.out.println(azimuth1);
        System.out.println(azimuth2);
        System.out.println(azimuth3);
        System.out.println(azimuth4);
    }
}
