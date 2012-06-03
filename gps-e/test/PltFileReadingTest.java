import gpsExtractor.tools.ozi.DateToTDateTimeConverter;
import gpsExtractor.tools.ozi.TDateTimeToDateConverter;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 18.10.2010
 * Time: 11:41:37
 * To change this template use File | Settings | File Templates.
 */
public class PltFileReadingTest {
    @Test
    public void testTDateTimeToDateConverter(){
        Assert.assertEquals(new Date(0), TDateTimeToDateConverter.convert(25569.16666));
        Assert.assertEquals(new Date(86400000), TDateTimeToDateConverter.convert(25570.16666));
    }

    @Test
    public void testDateToTDateTimeConverter(){
        Assert.assertEquals(25569.16666, DateToTDateTimeConverter.convert(new Date(0)));
        Assert.assertEquals(25570.16666, DateToTDateTimeConverter.convert(new Date(86400000)));
    }

    @Test
    public void testPltFileReading(){
        
    }
}
