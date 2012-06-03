package gpsExtractor.tools.gpx;

import gpsExtractor.storage.TrackStorage;
import gpsExtractor.tools.trk.*;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by IntelliJ IDEA.
 * User: KPETROV
 * Date: 24.09.2010
 * Time: 16:57:30
 * To change this template use File | Settings | File Templates.
 */
public class GPXWriter implements Runnable{
    Collection<TrackID> tids;
    String fileName;
    Logger log;

    public GPXWriter(Collection<TrackID> tids, String fileName) {
        this.tids = tids;
        this.fileName = fileName;

        try {
            boolean append = true;
            FileHandler fh = new FileHandler("KMLParser.log", append);
            fh.setFormatter(new SimpleFormatter());
            log = Logger.getLogger("tools.kml.KMLParser");
            log.addHandler(fh);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTracksToGPX() throws XMLStreamException, FileNotFoundException {

        final FileOutputStream str = new FileOutputStream(fileName);
        final XMLOutputFactory output = XMLOutputFactory.newInstance();
        final XMLStreamWriter writer = output.createXMLStreamWriter(str,"UTF-8");
        final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DecimalFormat decFormat = new DecimalFormat("##0.000000");
        decFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));


        writer.writeStartDocument("UTF-8","1.0");
        writer.writeCharacters("\n");

        writer.setDefaultNamespace("gpx");

        writer.writeStartElement("gpx");
        writer.writeCharacters("\n");

        for(TrackID tid: tids){
            Track t = TrackStorage.getTrackByID(tid);
            writer.writeStartElement("trk");
            writer.writeCharacters("\n");

            writer.writeStartElement("name");
            writer.writeCharacters(t.getTrackName());
            writer.writeEndElement(); //name
            writer.writeCharacters("\n");

            writer.writeStartElement("trkseg");
            writer.writeCharacters("\n");
            for(TrackPoint point: t.getPoints()){
                writer.writeStartElement("trkpt");
                writer.writeAttribute("lat",decFormat.format(point.getLat()));
                writer.writeAttribute("lon",decFormat.format(point.getLon()));
                writer.writeCharacters("\n");

                writer.writeStartElement("ele");
                writer.writeCharacters(decFormat.format(point.getElv()));
                writer.writeEndElement(); //ele
                writer.writeCharacters("\n");

                writer.writeStartElement("time");
                writer.writeCharacters(dateFormatter.format(point.getTime()));
                writer.writeEndElement(); //time
                writer.writeCharacters("\n");

                writer.writeEndElement(); //trkpt
                writer.writeCharacters("\n");
            }
            writer.writeEndElement(); //trkseg
            writer.writeCharacters("\n");

            writer.writeEndElement(); //trk
            writer.writeCharacters("\n");
        }
        writer.writeEndElement(); //gpx
        writer.writeCharacters("\n");

        writer.writeEndDocument();
        writer.flush();
        writer.close();
        }

    @Override
    public void run() {
        try {
            this.writeTracksToGPX();
        } catch (XMLStreamException e) {
            e.printStackTrace();
            log.warning(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.warning(e.getMessage());
        }
    }
}
