package ir.khabarefori.helper.DateTime;

/**
 * Created by Hani on 5/30/14.
 */
public class HDateTime {

    public static GeneralFormat convertDatabaseFormatToGeneralFormat(String datetime)
    {
        String[] strDate = datetime.split(" ")[0].split("-");
        int[] intDate = new int[3];
        for (int i = 0; i < strDate.length; i++)
            intDate[i] = Integer.parseInt(strDate[i]);

        return new GeneralFormat(intDate[0] , intDate[1] , intDate[2]);
    }

}
