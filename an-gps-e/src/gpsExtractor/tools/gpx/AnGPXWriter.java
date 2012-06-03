package gpsExtractor.tools.gpx;

import android.util.Log;
import android.util.Xml;
import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.trk.*;
import org.xmlpull.v1.XmlSerializer;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: KPETROV
 * Date: 24.09.2010
 * Time: 16:57:30
 * To change this template use File | Settings | File Templates.
 */
public class AnGPXWriter implements Runnable {
    Collection<Track> tracks;
    String fileName;
    boolean writeWP;
    //Logger log;

    public AnGPXWriter(Collection<Track> tracks, String fileName, boolean writeWP) {
        this.tracks = tracks;
        this.fileName = fileName;
        this.writeWP = writeWP;
    }

    public AnGPXWriter(Track currentTrack, String fileName, boolean writeWP) {
        tracks = new LinkedList<Track>();
        tracks.add(currentTrack);
        this.fileName = fileName;
        this.writeWP = writeWP;
    }

    /*public AnGPXWriter(Collection<TrackID> tracks, String fileName, boolean writeWP) {
        for (TrackID tid : tracks) {
            this.tracks.add(TrackStorage.getTrackByID(tid));

        }
        this.fileName = fileName;
        this.writeWP = writeWP;
    }*/

    private void writeTracksToGPX() throws IOException {

        final FileWriter str = new FileWriter(fileName);
        final XmlSerializer serializer = Xml.newSerializer();
        final StringWriter writer = new StringWriter();
        final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DecimalFormat decFormat = new DecimalFormat("##0.000000");
        decFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        serializer.setOutput(writer);
        serializer.startDocument("UTF-8", true);
        serializer.text("\n");

        serializer.startTag("", "gpx");
        serializer.text("\n");

        if (writeWP){
            for(Waypoint wp: TrackStorage.getWaypoints()){
                serializer.startTag("", "wpt");
                serializer.attribute("", "lat", decFormat.format(wp.getLat()));
                serializer.attribute("", "lon", decFormat.format(wp.getLon()));
                serializer.text("\n");

                serializer.startTag("", "ele");
                serializer.text(decFormat.format(wp.getElv()));
                serializer.endTag("", "ele"); //ele
                serializer.text("\n");

                serializer.startTag("", "time");
                serializer.text(dateFormatter.format(wp.getTime()));
                serializer.endTag("", "time"); //time
                serializer.text("\n");

                serializer.startTag("", "name");
                serializer.text(wp.getName());
                serializer.endTag("", "name"); //name
                serializer.text("\n");

                serializer.startTag("", "type");
                serializer.text(Waypoint.TYPE);
                serializer.endTag("", "type"); //name
                serializer.text("\n");

                serializer.endTag("","wpt");
                serializer.text("\n");
            }
        }

        for (Track t : tracks) {
            serializer.startTag("", "trk");
            serializer.text("\n");

            serializer.startTag("", "name");
            serializer.text(t.getTrackName());
            serializer.endTag("", "name"); //name
            serializer.text("\n");

            serializer.startTag("", "trkseg");
            serializer.text("\n");
            for (TrackPoint point : t.getPoints()) {
                serializer.startTag("", "trkpt");
                serializer.attribute("", "lat", decFormat.format(point.getLat()));
                serializer.attribute("", "lon", decFormat.format(point.getLon()));
                serializer.text("\n");

                serializer.startTag("", "ele");
                serializer.text(decFormat.format(point.getElv()));
                serializer.endTag("", "ele"); //ele
                serializer.text("\n");

                serializer.startTag("", "time");
                serializer.text(dateFormatter.format(point.getTime()));
                serializer.endTag("", "time"); //time
                serializer.text("\n");

                serializer.endTag("", "trkpt"); //trkpt
                serializer.text("\n");
            }
            serializer.endTag("", "trkseg"); //trkseg
            serializer.text("\n");

            serializer.endTag("", "trk"); //trk
            serializer.text("\n");
        }
        serializer.endTag("", "gpx"); //gpx
        serializer.text("\n");

        serializer.endDocument();
        serializer.flush();
        str.write(writer.toString());
        str.flush();
        str.close();
    }

    @Override
    public void run() {
        try {
            this.writeTracksToGPX();
        } catch (FileNotFoundException e) {
            Log.e("","",e);
            //log.warning(e.getMessage());
        } catch (IOException e) {
            Log.e("","",e);
            //log.warning(e.getMessage());
        }
    }
}
