package ir.khabarefori;

import android.test.suitebuilder.annotation.MediumTest;
import ir.khabarefori.lib.DateTime.GeneralFormat;
import ir.khabarefori.lib.DateTime.HDateTime;
import ir.khabarefori.lib.datetime.Roozh;
import junit.framework.TestCase;

/**
 * Created by Hani on 5/30/14.
 */
public class RoozhTest extends TestCase{

    @MediumTest
    public void testPersianToGregorian() {
        String datetime = "1393-03-09 00:00";

        GeneralFormat generalFormat = HDateTime.convertDatabaseFormatToGeneralFormat(datetime);

        Roozh roozh = new Roozh();
        roozh.PersianToGregorian(generalFormat.Year, generalFormat.Month, generalFormat.Day);

        assertEquals("2014-05-30" , roozh.toString());
    }

    @MediumTest
    public void testGregorianToPersian() {
        String datetime = "2014-05-30 00:00";

        GeneralFormat generalFormat = HDateTime.convertDatabaseFormatToGeneralFormat(datetime);

        Roozh roozh = new Roozh();
        roozh.GregorianToPersian(generalFormat.Year, generalFormat.Month, generalFormat.Day);

        assertEquals("1393-03-09" , roozh.toString());
    }
}
