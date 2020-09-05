package in.afckstechnologies.afcksenquirymanagement.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.utils.Config;
import in.afckstechnologies.afcksenquirymanagement.utils.VolleySingleton;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        pullDataFromServer();
    }

    private void pullDataFromServer() {

        JSONObject json = new JSONObject();
        try {
            json.put("device_id", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_GETALLVWLOCATIONSDEMANDEDUSERWISE, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("status")) {
                                //updating the status in sqlite
                                //  db.updateNameStatus(id, MainActivity.NAME_SYNCED_WITH_SERVER);
                                if (!response.isNull("dataList")) {
                                    JSONArray jsonArray = response.getJSONArray("dataList");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);


                                    }
                                }
                                //sending the broadcast to refresh the list
                                // context.sendBroadcast(new Intent(MainActivity.DATA_SAVED_BROADCAST));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(HomePageActivity.this).addToRequestQueue(jsonObjectRequest);
    }
}
