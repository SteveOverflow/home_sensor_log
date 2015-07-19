package steveoverflow.homesensorlog;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

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
        TextView textView = (TextView) rowView.findViewById(R.id.readingText);
        NetworkImageView imageView = (NetworkImageView) rowView.findViewById(R.id.readingImage);

        try{
            JSONObject reading = values.get(position);
            int lightVal = reading.getInt("lightVal");
            String img = ReadingsUtil.evaluateLightLevel(lightVal);
            imageView.setImageUrl(res.getString(R.string.baseURL) + "/images/" + img +".png", mImageLoader);
            textView.setText(reading.toString());
        }catch(JSONException je){
            je.printStackTrace();
        }

        return rowView;
    }
}
