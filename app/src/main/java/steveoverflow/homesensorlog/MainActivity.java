package steveoverflow.homesensorlog;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private TextView mTextView;
    private ImageLoader mImageLoader;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();
        mImageLoader = new ImageLoader(VolleyApplication.getInstance().getRequestQueue(), new LruBitmapCache(5));

        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        this.loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadData(){
        mTextView = (TextView) findViewById(R.id.text1);
        mTextView.setText("Loading...");

        final Button b = (Button) findViewById(R.id.refresh);
        b.setEnabled(false);

        JsonArrayRequest request = new JsonArrayRequest(res.getString(R.string.baseURL) + "/data/lastN/5",
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response){
                        mTextView.setText("Current Status");
                        try {
                            setCurrentStatus(response.getJSONObject(0));
                            populatePrevious(response);
                            Log.d("JSON", response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        b.setEnabled(true);
                    }
                },

                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        error.printStackTrace();
                        mTextView.setText(error.toString());
                    }
                }
        );

        VolleyApplication.getInstance().getRequestQueue().add(request);

    }

    private void setCurrentStatus(JSONObject curInfo){
        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.image1);
        String img = "";

        TextView dateTextView = (TextView) findViewById(R.id.dateVal);
        TextView tempTextView = (TextView) findViewById(R.id.tempVal);
        TextView humidityTextView = (TextView) findViewById(R.id.humidtyVal);
        TextView pressureTextView = (TextView) findViewById(R.id.pressureVal);

        try {
            int lightVal = curInfo.getInt("lightVal");
            img = ReadingsUtil.evaluateLightLevel(lightVal);

            String dateString = curInfo.getString("date");

            Date readingDate = DateFormatter.parseJavascriptDate(dateString);

            dateTextView.setText(DateFormatter.formatDisplayDate(readingDate));
            tempTextView.setText("Temperature: " + curInfo.getString("tempVal")+ " " + (char) 0x00B0+"C");
            humidityTextView.setText("Humidity: "+curInfo.getString("humidityVal")+"%");

            Long pressure = curInfo.getLong("pressureVal");
            pressureTextView.setText("Pressure: "+(pressure / 10.0)+" kPa");

        }catch(JSONException je){
            je.printStackTrace();;
        }

        if(!img.trim().equalsIgnoreCase("")) {
            imageView.setImageUrl(res.getString(R.string.baseURL) + "/images/"+ img +".png", mImageLoader);
        }
    }

    private void populatePrevious(JSONArray readings) {
        final List<JSONObject> list = new ArrayList<JSONObject>();

        if (readings == null || readings.length() < 2)
            return; //either no data or only the current reading passed.

        for (int i = 1; i < readings.length(); i++) {  //start at the second reading and loop over the rest of the list
            try {
                list.add(readings.getJSONObject(i));
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        final ListView listView = (ListView) findViewById(R.id.listView);
        final ReadingArrayAdapter adapter = new ReadingArrayAdapter(this, list);
        listView.setAdapter(adapter);
    }
}
