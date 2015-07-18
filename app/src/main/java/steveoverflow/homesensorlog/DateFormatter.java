package steveoverflow.homesensorlog;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by stephentanton on 15-07-18.
 */
public class DateFormatter {
    private static SimpleDateFormat displayFmt = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
    private static SimpleDateFormat javascriptFmt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS");

    public static String formatDisplayDate(Date d){
        //not sure why, but the node.js server is sending date/time in the US/Arizona timezone.
        displayFmt.setTimeZone(TimeZone.getTimeZone("US/Arizona"));
        if(d!=null) {
            return displayFmt.format(d);
        }else{
            return "";
        }
    }

    public static Date parseJavascriptDate(String s){
        Date d = null;

        if(s==null||s.trim().equalsIgnoreCase("")){
            return null;
        }

        //remove the T and Z characters sent from the server since they are unrecognized chars
        //in the formatter.
        s = s.replace("T", " ");
        s = s.replace("Z", "");
        Log.d("DateFormatter", s);
        try {
            javascriptFmt.setTimeZone(TimeZone.getDefault());
            d = javascriptFmt.parse(s);

        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return d;
    }
}
