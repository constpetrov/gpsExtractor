package gpsExtractor.tools.kml;

import org.junit.*;

/**
 * Created by IntelliJ IDEA.
 * User: KPETROV
 * Date: 15.12.2010
 * Time: 17:12:49
 * To change this template use File | Settings | File Templates.
 */
public class KMLColorTest {
    @Test
    public void testColorGeneration(){
        for(Integer color: KMLWriter.createColors(255)){
            System.out.println(Integer.toHexString(color));
        }
    }
}
