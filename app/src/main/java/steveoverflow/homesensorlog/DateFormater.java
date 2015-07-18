package steveoverflow.homesensorlog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by stephentanton on 15-07-18.
 */
public class DateFormater {
    private static SimpleDateFormat displayFmt = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss");
    private static SimpleDateFormat javascriptFmt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.SSS", Locale.ENGLISH);

    public static String formatDisplayDate(Date d){

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

        try {
            d = javascriptFmt.parse(s);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return d;
    }
}
