package gpsExtractor.tools.kml;

import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.AbstractGPSParser;
import gpsExtractor.tools.trk.Track;
import gpsExtractor.tools.trk.TrackID;
import gpsExtractor.tools.trk.TrackPoint;
import org.xml.sax.Attributes;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 01.10.2010
 * Time: 14:51:42
 * To change this template use File | Settings | File Templates.
 */
public class KMLParser extends AbstractGPSParser {
    private static Logger log;
    private String buffer = "";
    private boolean need_point = false;
    Queue<String> points = new LinkedList<String>();
    Queue<Date> timeStamps = new LinkedList<Date>();
    Date parseStartTime = new Date();
    static {
        try {
            boolean append = true;
            FileHandler fh = new FileHandler("KMLParser.log", append);
            //fh.setFormatter(new XMLFormatter());
            fh.setFormatter(new SimpleFormatter());
            log = Logger.getLogger("tools.kml.KMLParser");
            log.addHandler(fh);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Start element. */
    public void startElement(String namespaceURI, String localName,
                           String rawName, Attributes attrs)
    {
        if (rawName.equals("Placemark")){
            currentTrack = new Track();
            trk_started=true;
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            trkN++;

        }

        if (rawName.equals("name") &&trk_started){       //�������� <name>, ���� ��� � ������� characters
            need_name=true;
        }

        if (rawName.equals("when") &&trk_started){       //�������� <name>, ���� ��� � ������� characters
            need_time=true;
            trk_has_time = true;
        }

        if (rawName.equals("gx:coord") &&trk_started){       //�������� <name>, ���� ��� � ������� characters
            need_point=true;
        }

        if (rawName.equals("coordinates") &&trk_started){       //�������� <coordinates>, ���� coordinates � ������� characters
            need_coordinates=true;
            log.info("coordinates");
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

        if(need_name){
            currentTrack.setTrackName(new String(ch, start, length));
        }

        if(need_coordinates){
            String coordinates = buffer + new String(ch, start, length);
            buffer = coordinates.substring(coordinates.lastIndexOf(" "));
            String regex = "\\G(?:^|\\s+)(\\d+\\.\\d+),(\\d+\\.\\d+),(\\d+(?:\\.\\d+)?)";
            Matcher mMain = Pattern.compile(regex, Pattern.COMMENTS).matcher("");
            mMain.reset(coordinates);
            while(mMain.find()){
                TrackPoint point = new TrackPoint(Double.parseDouble(mMain.group(2)), Double.parseDouble(mMain.group(1)), parseStartTime, Double.parseDouble(mMain.group(3)));
                currentTrack.add(point);
//                System.out.println(point);
            }
        }

        if (need_time){
            try {
                timeStamps.offer(dateFormatter.parse(new String(ch, start, length)));
            } catch (ParseException e) {
                log.warning(e.getMessage());
            }
        }

        if (need_point){
            points.offer(new String(ch, start, length));
        }
    } // characters(char[],int,int);



    /** End element. */
    public void endElement(String namespaceURI, String localName,
                         String rawName)
    {
        if (rawName.equals("name")){         //�������� <name>, �� ���� ��� ����� � ������� characters
            need_name=false;
        }

        if (rawName.equals("coordinates")){         //�������� <coordinates>, �� ���� coordinates � ������� characters
            need_coordinates=false;
        }

        if (rawName.equals("when")){       //�������� <name>, ���� ��� � ������� characters
            need_time=false;
        }

        if (rawName.equals("gx:coord")){       //�������� <name>, ���� ��� � ������� characters
            need_point=false;
        }

        if (rawName.equals("Placemark")){        //�������� <Placemark>, �� ���� ��� � ����������
            trk_started=false;
            if((points.size()!=0) && (points.size()==timeStamps.size())){
                String pointBuffer;
                StringTokenizer spaceTokenizer;
                while(points.size() != 0){
                    pointBuffer = points.poll();
                    spaceTokenizer = new StringTokenizer(pointBuffer," ");
                    String lon = spaceTokenizer.nextToken();
                    String lat = spaceTokenizer.nextToken();
                    String elv = spaceTokenizer.nextToken();
                    currentTrack.add(new TrackPoint(Double.parseDouble(lat), Double.parseDouble(lon), timeStamps.poll(), Double.parseDouble(elv)));
                }
            }else if(!trk_has_time){
                String pointBuffer;
                StringTokenizer spaceTokenizer;
                Date now = new Date();
                while(points.size() != 0){
                    pointBuffer = points.poll();
                    spaceTokenizer = new StringTokenizer(pointBuffer," ");
                    String lon = spaceTokenizer.nextToken();
                    String lat = spaceTokenizer.nextToken();
                    String elv = spaceTokenizer.nextToken();
                    currentTrack.add(new TrackPoint(Double.parseDouble(lat), Double.parseDouble(lon), now, Double.parseDouble(elv)));
                }
            }
            currentTrack.removeOnePartSegments();
            TrackStorage.put(new TrackID(currentFile.getName(), currentTrack.getTrackName(), currentTrack.getStartTime()), currentTrack);
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
