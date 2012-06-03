package gpsExtractor.tools;

import gpsExtractor.tools.gpx.GarminPoint;
import gpsExtractor.tools.trk.Track;
import gpsExtractor.tools.trk.TrackID;
import gpsExtractor.tools.trk.TrackResult;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 01.10.2010
 * Time: 15:29:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGPSParser extends DefaultHandler {
    protected GarminPoint point;
    protected ArrayList<TrackResult> results;
    protected File currentFile;
    protected Track currentTrack;
    protected TrackID currentTrackID;
    protected boolean need_coordinates;
    protected boolean need_name;
    protected boolean need_ele;
    protected boolean need_time;
    protected boolean trk_started=false;
    protected boolean trk_has_time=false;
    protected SimpleDateFormat dateFormatter;
    protected int trkN=0;
    public ArrayList<TrackResult> parseURI(String uri)
    {
        results= new ArrayList<TrackResult>();
        try
        {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            sp.parse(uri, this);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
        return results;
    }
    public  ArrayList<TrackResult> parseFile(File f)
    {
        results= new ArrayList<TrackResult>();
        try
        {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            currentFile = f;
            sp.parse(f, this);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
        return results;
    }
    /** Start document. */
    public abstract void startDocument();// startDocument()





    /** Ignorable whitespace. */
    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        characters(ch, start, length);
    } // ignorableWhitespace(char[],int,int);

    /** End element. */


    /** End document. */
    public abstract void endDocument();  // endDocument()

    /** Processing instruction. */
    public void processingInstruction(String target, String data)
    {
        //System.out.print("<?");
        //System.out.print(target);
        if (data != null && data.length() > 0)
        {
            //System.out.print(' ');
            //System.out.print(data);
        }
        //System.out.print("?>");

    } // processingInstruction(String,String)

    //
    // ErrorHandler methods
    //

    /** Warning. */
    public void warning(SAXParseException ex)
    {
        //System.err.println("[Warning] "+
         //              getLocationString(ex)+": "+
          //             ex.getMessage());
    }

    /** Error. */
    public void error(SAXParseException ex)
    {
       // System.err.println("[Error] "+
         //              getLocationString(ex)+": "+
         //              ex.getMessage());
    }

    /** Fatal error. */
    public void fatalError(SAXParseException ex)
        throws SAXException
    {
       // System.err.println("[Fatal Error] "+
           //            getLocationString(ex)+": "+
            //           ex.getMessage());
        throw ex;
    }

    /** Returns a string of the location. */
    private String getLocationString(SAXParseException ex)
    {
        StringBuffer str = new StringBuffer();

        String systemId = ex.getSystemId();
        if (systemId != null)
        {
            int index = systemId.lastIndexOf('/');
            if (index != -1)
            systemId = systemId.substring(index + 1);
            str.append(systemId);
        }
        str.append(':');
        str.append(ex.getLineNumber());
        str.append(':');
        str.append(ex.getColumnNumber());

        return str.toString();
    } // getLocationString(SAXParseException):String
}
