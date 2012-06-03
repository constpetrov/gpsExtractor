package gpsExtractor.tools.gpx;

import javax.xml.parsers.*;

import gpsExtractor.tools.AbstractGPSParser;
import gpsExtractor.tools.ParserCalc;
import gpsExtractor.tools.trk.Track;
import gpsExtractor.tools.trk.TrackID;
import gpsExtractor.tools.trk.TrackResult;
import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.trk.Waypoint;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.TimeZone;
import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 01.12.2008
 * Time: 18:44:47
 * To change this template use File | Settings | File Templates.
 */
public class GPXParser extends AbstractGPSParser {
    boolean wp_started;
    String wp_name="";
    /** Start element. */
    public void startElement(String namespaceURI, String localName,
                           String rawName, Attributes attrs)
    {
        if(rawName.equals("wpt")){

            dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

            wp_started = true;
            
            point=new GarminPoint();

            if (attrs != null)
            {
                int len = attrs.getLength();
                for (int i = 0; i < len; i++)
                {
                    if (attrs.getQName(i).equals("lat"))
                    {
                        point.lat=Double.parseDouble(attrs.getValue(i));
                    }

                    if (attrs.getQName(i).equals("lon"))
                    {
                        point.lon=Double.parseDouble(attrs.getValue(i));
                    }
                }
            }
        }


        if (rawName.equals("trk")){
            //calc=new ParserCalc();
            results.add(new TrackResult());
            currentTrack = new Track();

            need_ele=false;
            need_time=false;
            trk_started=true;

            dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            trkN++;

        }

        if (rawName.equals("trkseg")){
            currentTrack.addSegment();    
        }

        if (rawName.equals("trkpt"))
        {
            point=new GarminPoint();

            if (attrs != null)
            {
                int len = attrs.getLength();
                for (int i = 0; i < len; i++)
                {
                    if (attrs.getQName(i).equals("lat"))
                    {
                    point.lat=Double.parseDouble(attrs.getValue(i));
                    }

                    if (attrs.getQName(i).equals("lon"))
                    {
                    point.lon=Double.parseDouble(attrs.getValue(i));
                    }
                }
            }

        }

        if (rawName.equals("ele") && (trk_started || wp_started)){       //�������� <ele>, ���� ������ � ������� characters
            need_ele=true;
        }

        if (rawName.equals("time") && (trk_started || wp_started)){      //�������� <time>, ���� ����� � ������� characters
            need_time=true;
        }

        if (rawName.equals("name") && (trk_started || wp_started)){      //�������� <time>, ���� ����� � ������� characters
            need_name=true;
        }



        if (attrs != null)
        {
            int len = attrs.getLength();
            for (int i = 0; i < len; i++)
            {
            }
        }
    } // startElement(String,AttributeList)

    /** Characters. */
    public void characters(char ch[], int start, int length)
    {
        if (need_ele){
            point.elv=Double.parseDouble(new String(ch, start, length));
        }

        if(need_name){
            if(trk_started){
                currentTrack.setTrackName(new String(ch, start, length));
            } else if (wp_started){
                wp_name = new String(ch, start, length);
            }
        }

        if (need_time){


            try {
               point.time=dateFormatter.parse(new String(ch, start, length));
            } catch (ParseException e) {
                //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    } // characters(char[],int,int);
    /** End element. */
    public void endElement(String namespaceURI, String localName,
                         String rawName)
    {
        if (rawName.equals("name")){         //�������� <name>, �� ���� ��� ����� � ������� characters
            need_name=false;
        }

        if (rawName.equals("ele")){         //�������� <ele>, �� ���� ������ � ������� characters
            need_ele=false;
        }

        if (rawName.equals("time")){        //�������� <time>, �� ���� ����� � ������� characters
            need_time=false;
        }

        if (rawName.equals("trkpt")){        //�������� <trkpt>, ������ gpsExtractor.tools.trk.TrackPoint � gpsExtractor.tools.ParserCalc
            ParserCalc.addPoint(point, results.get(trkN-1));
            currentTrack.add(point);
        }

        if (rawName.equals("trk")){        //�������� <trk>, �� ���� ����� � ������
            trk_started=false;
            currentTrack.removeOnePartSegments();
            TrackStorage.put(new TrackID(currentFile.getName(), currentTrack.getTrackName(), currentTrack.getStartTime()), currentTrack);
            
        }

        if (rawName.equals("wpt")){
            wp_started = false;
            TrackStorage.addWaypoint(new Waypoint(wp_name, point));
        }
    } // endElement(String)

    @Override
    public void startDocument() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void endDocument() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
