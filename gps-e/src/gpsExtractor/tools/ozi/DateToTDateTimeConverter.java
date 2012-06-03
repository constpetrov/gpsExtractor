package gpsExtractor.tools.ozi;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: kostya
 * Date: 10/23/10
 * Time: 6:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateToTDateTimeConverter {
    public static double convert (Date date){
        return ((double)date.getTime()/86400000d)+25569.16666;
    }
}
