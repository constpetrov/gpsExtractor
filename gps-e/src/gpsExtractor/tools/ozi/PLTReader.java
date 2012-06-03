package gpsExtractor.tools.ozi;

import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.AbstractGPSParser;
import gpsExtractor.tools.trk.Track;
import gpsExtractor.tools.trk.TrackID;
import gpsExtractor.tools.trk.TrackPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 18.10.2010
 * Time: 11:56:59
 * To change this template use File | Settings | File Templates.
 */
public class PLTReader {
    private static List<String> strings;
    private static String fileName;
    private PLTReader(){}

    public static void readPLT(File inFile){
        strings = new ArrayList<String>();
        fileName = inFile.getName();
        try{

            BufferedReader reader = new BufferedReader(
                    new FileReader(inFile));
            String line;
            int numRead;
            while ((line = reader.readLine()) != null) {
                if (true/*line.contains("\n")*/) {
                     strings.add(line);
                  }
            }

            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        parsePLT();
    }
    private static void parsePLT(){
        Track currentTrack = new Track();
        for(String str : strings){
            String regex = "\\G(\\s*)(\\d+\\.\\d+),(\\s*\\d+\\.\\d+),(\\d),(\\s*\\d+\\.\\d+),(\\s*\\d+\\.\\d+)";
            Matcher mMain = Pattern.compile(regex, Pattern.COMMENTS).matcher("");
            mMain.reset(str);
            while(mMain.find()){
                if(Integer.parseInt(mMain.group(4))==1){
                    currentTrack.addSegment();
                }
                TrackPoint point = new TrackPoint(Double.parseDouble(mMain.group(2)), Double.parseDouble(mMain.group(3)), TDateTimeToDateConverter.convert(Double.parseDouble(mMain.group(6))), Double.parseDouble(mMain.group(5))*0.305);
                currentTrack.add(point);
//                System.out.println(point);
            }
        }
        StringTokenizer commaTokenizer = new StringTokenizer(strings.get(4),",");
        commaTokenizer.nextToken();
        commaTokenizer.nextToken();
        commaTokenizer.nextToken();
        String trackName = commaTokenizer.nextToken();
        currentTrack.removeOnePartSegments();
        TrackStorage.put(new TrackID(fileName, trackName,currentTrack.getStartTime()),currentTrack);
    }
}
