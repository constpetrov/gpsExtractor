package gpsExtractor.tools.trk;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 11.10.2010
 * Time: 17:15:01
 * To change this template use File | Settings | File Templates.
 */
public class TrackSegment {
    private LinkedList<TrackSegmentPart> segmentParts = new LinkedList<TrackSegmentPart>();
    public void add (TrackSegmentPart segmentPart){
        segmentParts.add(segmentPart);
    }

    public LinkedList<TrackSegmentPart> getSegmentParts() {
        return segmentParts;
    }
}
