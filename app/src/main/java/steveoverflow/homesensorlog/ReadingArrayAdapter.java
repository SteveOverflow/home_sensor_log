package steveoverflow.homesensorlog;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by stephentanton on 15-07-19.
 */
public class ReadingArrayAdapter extends ArrayAdapter<JSONObject> {
    private final Context context;
    private final List<JSONObject> values;
    private ImageLoader mImageLoader;
    Resources res;

    public ReadingArrayAdapter(Context context, List<JSONObject> values){
        super(context, R.layout.reading_list_item, values);
        this.context = context;
        this.values = values;
        this.mImageLoader = new ImageLoader(VolleyApplication.getInstance().getRequestQueue(), new LruBitmapCache(5));
        res = context.getResources();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.reading_list_item, parent, false);
        TextView dateText = (TextView) rowView.findViewById(R.id.readingDate);
        TextView readingLine1 = (TextView) rowView.findViewById(R.id.readingLine1);
        TextView readingLine2 = (TextView) rowView.findViewById(R.id.readingLine2);
        NetworkImageView imageView = (NetworkImageView) rowView.findViewById(R.id.readingImage);

        try{
            JSONObject reading = values.get(position);
            int lightVal = reading.getInt("lightVal");
            String img = ReadingsUtil.evaluateLightLevel(lightVal);
            imageView.setImageUrl(res.getString(R.string.baseURL) + "/images/" + img + ".png", mImageLoader);

            String dateString = reading.getString("date");
            Log.d("DATE",dateString);
            Date readingDate = DateFormatter.parseJavascriptDate(dateString);
            dateText.setText(DateFormatter.formatDisplayDate(readingDate));

            String line1 = "", line2 = "";

            line1 += "Temperature: " + reading.getString("tempVal") + (char) 0x00B0 + "C  Humidity: " + reading.getString("humidityVal") + "%";
            readingLine1.setText(line1);

            Long pressureVal = reading.getLong("pressureVal");
            line2 += "Pressure: "+(pressureVal / 10.0) +" kPa";
            readingLine2.setText(line2);
        }catch(JSONException je){
            je.printStackTrace();
        }

        return rowView;
    }
}
