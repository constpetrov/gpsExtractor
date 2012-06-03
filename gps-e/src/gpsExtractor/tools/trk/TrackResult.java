package gpsExtractor.tools.trk;

//import org.apache.commons.configuration.ConfigurationException;
//import org.apache.commons.configuration.XMLConfiguration;
import gpsExtractor.storage.PreferencesHolder;


import java.util.List;

import java.util.prefs.BackingStoreException;


/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 03.12.2008
 * Time: 1:25:29
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class TrackResult {
    public TrackDistance[] distList;
    private int distNum=0;
    private List<Double> threshold;
    private double minBikeSpeed;
    private double stopSpeed;

    void setConfig(/*String file*/) throws /*IOException, SAXException, ParserConfigurationException,*/ BackingStoreException {
        minBikeSpeed = PreferencesHolder.getMinBikeSpeed();
        stopSpeed = PreferencesHolder.getStopSpeed();
        threshold = PreferencesHolder.getGradientThresholds();
    }
    public TrackResult(){
        try {
            setConfig();
        } catch (BackingStoreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        TrackDistance dist;
        distList= new TrackDistance[threshold.size()+1];
        dist = new TrackDistance(Double.parseDouble(threshold.get(0).toString()),Double.POSITIVE_INFINITY);
        distList[0]=dist;
        for(int i=0;i<threshold.size()-1;i++){
            dist = new TrackDistance(Double.parseDouble(threshold.get(i+1).toString()),Double.parseDouble(threshold.get(i).toString()));
            distList[i+1]=dist;
        }
        dist = new TrackDistance(Double.NEGATIVE_INFINITY,Double.parseDouble(threshold.get(threshold.size()-1).toString()));
        distList[threshold.size()]=dist;
        distNum=threshold.size()+1;
    }

    public TrackDistance getDist(int i){
        return distList[i];
    }
    public int getNum(){
        return distNum;
    }
    public double getMinBikeSpeed(){
        return minBikeSpeed;
    }
    public double getStopSpeed(){
            return stopSpeed;
    }
}

