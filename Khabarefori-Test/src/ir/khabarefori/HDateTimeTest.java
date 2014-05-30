package ir.khabarefori;

import android.test.suitebuilder.annotation.MediumTest;
import ir.khabarefori.lib.DateTime.GeneralFormat;
import ir.khabarefori.lib.DateTime.HDateTime;
import junit.framework.TestCase;

/**
 * Created by Hani on 5/30/14.
 */
public class HDateTimeTest extends TestCase{

    @MediumTest
    public void testConvertDatabaseFormatToGeneralFormat()
    {
        String datetime = "1393-03-09 00:00";

        GeneralFormat gFormat = HDateTime.convertDatabaseFormatToGeneralFormat(datetime);

        assertEquals(1393 , gFormat.Year);
        assertEquals(3 , gFormat.Month);
        assertEquals(9 , gFormat.Day);
    }
}
