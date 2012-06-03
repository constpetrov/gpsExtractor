import org.junit.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: kpetrov
 * Date: 12.10.2010
 * Time: 11:20:49
 * To change this template use File | Settings | File Templates.
 */
public class DecimalFormatTest {
    @Test
    public void testDecimalFormats(){
        DecimalFormat decFormat = new DecimalFormat("##0.000000");
        decFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
        System.out.println(decFormat.format(-35.75634));
    }
}
