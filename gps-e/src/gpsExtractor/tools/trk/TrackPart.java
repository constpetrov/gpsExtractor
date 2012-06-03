package gpsExtractor.tools.trk;

import gpsExtractor.tools.gpx.GarminDelta;

/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 03.12.2008
 * Time: 1:26:37
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class TrackPart {
    public double dist;
    public double elev;
    public long time;

    public TrackPart(){
        dist=0;
        elev=0;
        time=0;
    }

    public TrackPart(double d,double e,long t){
        dist=d;
        elev=e;
        time=t;
    }

    public TrackPart(TrackPart t){
        dist=t.dist;
        elev=t.elev;
        time=t.time;
    }

    public void addPart(GarminDelta d){
        dist+=d.pathDist;
        elev+=d.elevDist;

        if(Math.abs(d.time)<3600000)time+=d.time;
    }

    public void addPart(TrackPart d){
        dist+=d.dist;
        elev+=d.elev;
        time+=d.time;
    }

    public String timeToString(){
        String res="";

        res+= time/3600000+" ���";
        if ((time/3600000)%100!=12&&(time/3600000)%100!=13&&(time/3600000)%100!=14&&((time/3600000)%10==2||(time/3600000)%10==3||(time/3600000)%10==4)){
            res+="a ";
        }
        else if((time/3600000)%100!=11&&(time/3600000)%10==1){
            res+=" ";
        }
        else res+="�� ";

        if (time/3600000<1){
            res="";
        } 

        res+= (time%3600000)/60000+" �����";
        if (((time%3600000)/60000)%100!=12&&((time%3600000)/60000)%100!=13&&((time%3600000)/60000)%100!=14&&(((time%3600000)/60000)%10==2||((time%3600000)/60000)%10==3||((time%3600000)/60000)%10==4)){
            res+="� ";
        }
        else if(((time%3600000)/60000)%100!=11&&((time%3600000)/60000)%10==1){
            res+="� ";
        }
        else res+=" ";

        if ((time%3600000)/60000<1){
            res="����� ������";
        }
        return res;
    }
}
