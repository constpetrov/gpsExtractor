package gpsExtractor.tools.trk;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 23.08.2010
 * Time: 17:48:43
 * To change this template use File | Settings | File Templates.
 */
public class ShortTrackInfo {
    private final Double distance;
    private final long timeTotal;
    private final long timeMove;
    private final Double avgTotal;
    private final Double avdMove;
    private final Double uphillTotal;
    private final Double downhillTotal;
    private final Double minHeight;
    private final Double maxHeight;
    private final Double maxSpeed;
    private final SimpleDateFormat formatForDifference = new SimpleDateFormat("HH:mm:ss");

    public ShortTrackInfo(Double distance, long timeTotal, long timeMove, Double avgTotal, Double avdMove, Double uphillTotal, Double downhillTotal, Double maxHeight, Double minHeight, Double maxSpeed) {
        this.distance = distance;
        this.timeTotal = timeTotal;
        this.timeMove = timeMove;
        this.avgTotal = avgTotal;
        this.avdMove = avdMove;
        this.uphillTotal = uphillTotal;
        this.downhillTotal = downhillTotal;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.maxSpeed = maxSpeed;
    }

    public Double getDistance() {
        return distance;
    }

    public long getTimeTotal() {
        return timeTotal;
    }

    public long getTimeMove() {
        return timeMove;
    }

    public Double getAvgTotal() {
        return avgTotal;
    }

    public Double getAvgMove() {
        return avdMove;
    }

    public Double getUphillTotal() {
        return uphillTotal;
    }

    public Double getDownhillTotal() {
        return downhillTotal;
    }

    public Double getMinHeight() {
        return minHeight;
    }

    public Double getMaxHeight() {
        return maxHeight;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    @Override
    public String toString() {
        return "ShortTrackInfo{" +
                "distance=" + distance +
                ", timeTotal=" + formatForDifference.format(new Date(timeTotal)) +
                ", timeMove=" + formatForDifference.format(new Date(timeMove)) +
                ", avgTotal=" + avgTotal +
                ", avgMove=" + avdMove +
                ", uphillTotal=" + uphillTotal +
                ", downhillTotal=" + downhillTotal +
                '}';
    }
    public List<String> getInfoList(){
        return Arrays.asList("distance= " + String.format("%5.3f", distance/1000d) + "km",
                "timeTotal= " + timeDifference(timeTotal),
                "timeMove= " + timeDifference(timeMove),
                "avgTotal= " + String.format("%5.1f", avgTotal) +"km/h",
                "avgMove= " + String.format("%5.1f", avdMove) +"km/h",
                "uphillTotal= " + String.format("%5.0f", uphillTotal) + "m",
                "minHeight= " + String.format("%5.0f", minHeight) + "m",
                "maxHeight= " + String.format("%5.0f", maxHeight) + "m",
                "maxSpeed= " + String.format("%5.1f", maxSpeed) + "km/h");
    }
    private static String timeDifference(Long time){
        StringBuilder strBuild = new StringBuilder();
        Long days = time / (24L*60L*60L*1000L);
        Long hours = (time % (24L*60L*60L*1000L)) / (60L*60L*1000L);
        Long minutes = (time % (60L*60L*1000L)) / (60L*1000L);
        Long seconds = (time % (60L*1000L)) / (1000L);

        if(days != 0){
            strBuild.append(days + "d ");
            strBuild.append((hours > 9 ? hours : "0"+ hours) + ":");
            strBuild.append((minutes > 9 ? minutes: "0" + minutes) + ":");
            strBuild.append(seconds > 9 ? seconds : "0" + seconds);
        }else
        if(hours != 0){
            strBuild.append((hours > 9 ? hours : "0"+ hours) + ":");
            strBuild.append((minutes > 9 ? minutes: "0" + minutes) + ":");
            strBuild.append(seconds > 9 ? seconds : "0" + seconds);
        }else
        if(minutes != 0){
            strBuild.append((minutes > 9 ? minutes: "0" + minutes) + ":");
            strBuild.append(seconds > 9 ? seconds : "0" + seconds);
        }else{
            strBuild.append((seconds > 9 ? seconds : "0" + seconds) + "seconds");
        }
        return strBuild.toString();
    }
}
