package gpsExtractor.tools.ozi;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 18.10.2010
 * Time: 11:44:33
 * To change this template use File | Settings | File Templates.
 */
public class TDateTimeToDateConverter {
    public static Date convert (double tDateTime){
        long time = (long)((tDateTime - 25569.16666) * 86400d)*1000;
        return new Date(time);
    }
}
