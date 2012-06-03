package gpsExtractor.storage;

import gpsExtractor.tools.Calculator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 11.10.2010
 * Time: 17:44:31
 * To change this template use File | Settings | File Templates.
 */
public final class PreferencesHolder {
    private static Preferences prefs;

    private static double minBikeSpeed;
    private static double stopSpeed;

    private static ArrayList<Double> gradientThresholds;
    private static ArrayList<Double> speedThresholds;

    private static boolean drawGridLines;
    private static boolean drawSpeedLegend;
    private static boolean drawTitle;
    private static boolean jpeg;
    private static boolean png;

    private static boolean speed;
    private static boolean gradient;
    private static boolean noColor;

    private static double maxDistanceTwoPoints;
    private static long minTimeTwoPoints;
    private static double maxElevTwoPoints;

    private static DecimalFormat decFormat;
    static{
        prefs = Preferences.userNodeForPackage(Calculator.class);
        decFormat = new DecimalFormat("##0.0");
        decFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
        getConfig();
    }

    private PreferencesHolder() {}

    private static void getConfig(){
        minBikeSpeed = prefs.getDouble("minBikeSpeed", 8d);
        stopSpeed = prefs.getDouble("stopSpeed", 0.1d);
        gradientThresholds = new ArrayList<Double>();
        StringTokenizer thresholdString = new StringTokenizer(prefs.get("gradientThresholds", "0.1;3;6;12;18"),";");
        while (thresholdString.hasMoreTokens()){
            gradientThresholds.add(Double.parseDouble(thresholdString.nextToken()));
        }

        speedThresholds = new ArrayList<Double>();
        thresholdString = new StringTokenizer(prefs.get("speedThresholds", "5;10;15;20;25"),";");
        while (thresholdString.hasMoreTokens()){
            speedThresholds.add(Double.parseDouble(thresholdString.nextToken()));
        }

        drawGridLines = prefs.getBoolean("drawGridLines", true);
        drawTitle = prefs.getBoolean("drawTitle", true);
        drawSpeedLegend = prefs.getBoolean("drawSpeedLegend", true);
        jpeg = prefs.getBoolean("jpeg", false);
        png = prefs.getBoolean("png", true);

        speed = prefs.getBoolean("speed",true);
        gradient = prefs.getBoolean("gradient",false);
        noColor = prefs.getBoolean("noColor",false);

        maxDistanceTwoPoints = prefs.getDouble("maxDistanceTwoPoints", 10000);
        minTimeTwoPoints = prefs.getLong("minTimeTwoPoints", 0);
        maxElevTwoPoints = prefs.getDouble("maxElevTwoPoints", 50);

    }

    public static void setMinBikeSpeed(double minBikeSpeed) {
        PreferencesHolder.minBikeSpeed = minBikeSpeed;
        prefs.putDouble("minBikeSpeed",minBikeSpeed);
    }

    public static void setStopSpeed(double stopSpeed) {
        PreferencesHolder.stopSpeed = stopSpeed;
        prefs.putDouble("stopSpeed",stopSpeed);
    }

    public static void setGradientThresholds(ArrayList<Double> gradientThresholds) {
        PreferencesHolder.gradientThresholds = gradientThresholds;
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i<gradientThresholds.size(); i++){
            builder.append(decFormat.format(gradientThresholds.get(i)));
            if(i == gradientThresholds.size()-1){
                break;
            }
            builder.append(";");
        }
        prefs.put("gradientThresholds",builder.toString());
    }

    public static void setSpeedThresholds(ArrayList<Double> gradientThresholds) {
        PreferencesHolder.speedThresholds = speedThresholds;
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i<speedThresholds.size(); i++){
            builder.append(decFormat.format(speedThresholds.get(i)));
            if(i == speedThresholds.size()-1){
                break;
            }
            builder.append(";");
        }
        prefs.put("speedThresholds",builder.toString());
    }

    public static void setDrawGridLines(boolean drawGridLines) {
        PreferencesHolder.drawGridLines = drawGridLines;
        prefs.putBoolean("drawGridLines",drawGridLines);
    }

    public static void setDrawSpeedLegend(boolean drawSpeedLegend) {
        PreferencesHolder.drawSpeedLegend = drawSpeedLegend;
        prefs.putBoolean("drawSpeedLegend",drawSpeedLegend);
    }

    public static void setDrawTitle(boolean drawTitle) {
        PreferencesHolder.drawTitle = drawTitle;
        prefs.putBoolean("drawTitle",drawTitle);
    }

    public static void setJpeg(boolean jpeg) {
        PreferencesHolder.jpeg = jpeg;
        prefs.putBoolean("jpeg",jpeg);
    }

    public static void setPng(boolean png) {
        PreferencesHolder.png = png;
        prefs.putBoolean("png",png);
    }

    public static void setSpeed(boolean speed) {
        PreferencesHolder.speed = speed;
        prefs.putBoolean("speed",speed);
    }

    public static void setGradient(boolean gradient) {
        PreferencesHolder.gradient = gradient;
        prefs.putBoolean("gradient",gradient);
    }

    public static void setNoColor(boolean noColor) {
        PreferencesHolder.noColor = noColor;
        prefs.putBoolean("noColor",noColor);
    }

    public static void setMaxDistanceTwoPoints(double maxDistanceTwoPoints) {
        PreferencesHolder.maxDistanceTwoPoints = maxDistanceTwoPoints;
        prefs.putDouble("maxDistanceTwoPoints",maxDistanceTwoPoints);
    }

    public static void setMinTimeTwoPoints(long minTimeTwoPoints) {
        PreferencesHolder.minTimeTwoPoints = minTimeTwoPoints;
        prefs.putLong("minTimeTwoPoints",minTimeTwoPoints);
    }

    public static void setMaxElevTwoPoints(double maxElevTwoPoints) {
        PreferencesHolder.maxElevTwoPoints = maxElevTwoPoints;
        prefs.putDouble("maxElevTwoPoints",maxElevTwoPoints);
    }

    public static double getMinBikeSpeed() {
        return minBikeSpeed;
    }

    public static double getStopSpeed() {
        return stopSpeed;
    }

    public static ArrayList<Double> getGradientThresholds() {
        return gradientThresholds;
    }

    public static ArrayList<Double> getSpeedThresholds() {
        return speedThresholds;
    }

    public static boolean isDrawGridLines() {
        return drawGridLines;
    }

    public static boolean isDrawSpeedLegend() {
        return drawSpeedLegend;
    }

    public static boolean isDrawTitle() {
        return drawTitle;
    }

    public static boolean isJpeg() {
        return jpeg;
    }

    public static boolean isPng() {
        return png;
    }

    public static boolean isSpeed() {
        return speed;
    }

    public static boolean isGradient() {
        return gradient;
    }

    public static boolean isNoColor() {
        return noColor;
    }

    public static double getMaxDistanceTwoPoints() {
        return maxDistanceTwoPoints;
    }

    public static long getMinTimeTwoPoints() {
        return minTimeTwoPoints;
    }

    public static double getMaxElevTwoPoints() {
        return maxElevTwoPoints;
    }
}
