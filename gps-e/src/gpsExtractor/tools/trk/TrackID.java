package gpsExtractor.tools.trk;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: KPETROV
 * Date: 22.09.2010
 * Time: 11:56:06
 * To change this template use File | Settings | File Templates.
 */
public class TrackID implements Comparable<TrackID>{
    private final String fileName;
    private final String trackName;
    private final Date startDateTime;
    

    public TrackID(String filename, String trackName, Date startDateTime) {
        this.fileName = filename;
        this.trackName = trackName;
        this.startDateTime = startDateTime;
    }

    public String getFileName() {
        return fileName;
    }

    public String getTrackName() {
        return trackName;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrackID trackID = (TrackID) o;

        if (fileName != null ? !fileName.equals(trackID.fileName) : trackID.fileName != null) return false;
        if (startDateTime != null ? !startDateTime.equals(trackID.startDateTime) : trackID.startDateTime != null)
            return false;
        if (trackName != null ? !trackName.equals(trackID.trackName) : trackID.trackName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fileName != null ? fileName.hashCode() : 0;
        result = 31 * result + (trackName != null ? trackName.hashCode() : 0);
        result = 31 * result + (startDateTime != null ? startDateTime.hashCode() : 0);
        return result;
    }

    public int compareTo(TrackID t){
        if (this==t){
            return 0;
        }
        if (this.equals(t)){
            return 0;
        }
        if (fileName.compareTo(t.getFileName())!=0){
            return fileName.compareTo(t.getFileName());
        }

        if (trackName.compareTo(t.getTrackName())!=0){
            return trackName.compareTo(t.getTrackName());    
        }

        return this.startDateTime.compareTo(t.getStartDateTime());
    }
}
