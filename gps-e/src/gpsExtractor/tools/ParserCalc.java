package gpsExtractor.tools;

import gpsExtractor.tools.gpx.*;
import gpsExtractor.tools.trk.TrackResult;


/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 02.12.2008
 * Time: 16:18:37
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public abstract class ParserCalc {
    static private GarminPoint bufPt = new GarminPoint();
    static private GarminDelta d1 =new GarminDelta();

    final static double r=6366197.7236758134307553505349006;

    static public void addPoint (GarminPoint p, TrackResult res){
        if(bufPt.lat!=0){
            d1=measure(bufPt,p);

            for(int i=0; i<res.getNum();i++){
                if(res.distList[i].testGrad(d1)){
                    res.distList[i].addDelta(d1,res.getMinBikeSpeed(),res.getStopSpeed());
                }
            }

        }


        bufPt.set(p);
    }
    static public GarminDelta measure(GarminPoint d1, GarminPoint d2){
        GarminDelta delta=new GarminDelta();
        long t1,t2;
        double x1,x2,y1,y2,z1,z2;

        x1=r*Math.cos(d2.lat*Math.PI/180.0)*Math.cos(d2.lon*Math.PI/180.0);
        y1=r*Math.cos(d2.lat*Math.PI/180.0)*Math.sin(d2.lon*Math.PI/180.0);
        z1=r*Math.sin(d2.lat*Math.PI/180.0);

        x2=r*Math.cos(d1.lat*Math.PI/180.0)*Math.cos(d1.lon*Math.PI/180.0);
        y2=r*Math.cos(d1.lat*Math.PI/180.0)*Math.sin(d1.lon*Math.PI/180.0);
        z2=r*Math.sin(d1.lat*Math.PI/180.0);

        delta.pathDist=Math.pow((Math.pow(x2-x1,2.0)+Math.pow(y2-y1,2.0)+Math.pow(z2-z1,2.0)),0.5);
        delta.elevDist=(d1.elv-d2.elv);
        t1=d1.time.getTime();
        t2=d2.time.getTime();
        delta.time=Math.abs(t2-t1);
        return delta;
    }
}
