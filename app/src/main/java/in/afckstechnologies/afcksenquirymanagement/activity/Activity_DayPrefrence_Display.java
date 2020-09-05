package in.afckstechnologies.afcksenquirymanagement.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.adapter.DayPrefrenceListAdpter;
import in.afckstechnologies.afcksenquirymanagement.fragments.TabsFragmentActivity;
import in.afckstechnologies.afcksenquirymanagement.models.DayPrefrenceDAO;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentContract;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;
import in.afckstechnologies.afcksenquirymanagement.utils.Config;
import in.afckstechnologies.afcksenquirymanagement.utils.JsonHelper;
import in.afckstechnologies.afcksenquirymanagement.utils.WebClient;
import in.afckstechnologies.afcksenquirymanagement.view.CommentsDetailsView;
import in.afckstechnologies.afcksenquirymanagement.view.MultipleCommentAddView;

public class Activity_DayPrefrence_Display extends AppCompatActivity {
    LinearLayout takeChangeHome, takeChangeCourses, takeChangeLocation, takeTemplate, takeCreateComment, takeBookSeat, takeEditUser, takeDeleteUser;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    Button btnSearch;

    JSONObject jsonCenterObj, jsonobject, jsonLeadObj;
    JSONArray jsonarray;
    String centerResponse = "";
    JSONArray jsonArray;
    String dayprefrenceResponse = "";
    ProgressDialog mProgressDialog;
    DayPrefrenceDAO dayPrefrenceDAO;
    private RecyclerView mleadList;
    //
    List<DayPrefrenceDAO> data;
    DayPrefrenceListAdpter dayPrefrenceListAdpter;
    ArrayList<String> centerIdArrayList;
    Boolean status;
    String msg;
    //database helper object
    private StudentProvider.AFCKSDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__day_prefrence__display);
        preferences = getSharedPreferences("Prefrence", MODE_PRIVATE);
        prefEditor = preferences.edit();
        db = new StudentProvider.AFCKSDatabase(this);
        mleadList = (RecyclerView) findViewById(R.id.dayprefrenceList);
        takeCreateComment = (LinearLayout) findViewById(R.id.takeCreateComment);
        takeChangeHome = (LinearLayout) findViewById(R.id.takeChangeHome);
        takeChangeLocation = (LinearLayout) findViewById(R.id.takeChangeLocation);
        takeChangeCourses = (LinearLayout) findViewById(R.id.takeChangeCourses);
        takeTemplate = (LinearLayout) findViewById(R.id.takeTemplate);
        takeBookSeat = (LinearLayout) findViewById(R.id.takeBookSeat);
        takeBookSeat = (LinearLayout) findViewById(R.id.takeBookSeat);
        takeBookSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("user_id", "").equals("")) {
                    // BookSeatView bookSeatView = new BookSeatView();
                    // bookSeatView.show(getSupportFragmentManager(), "registrationView");
                } else {

                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }

            }
        });
        takeCreateComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MultipleCommentAddView commentAddView = new MultipleCommentAddView();
                commentAddView.show(getSupportFragmentManager(), "commentAddView");

            }
        });

        takeCreateComment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                prefEditor.putString("da", preferences.getString("st_first_name", "") + " " + preferences.getString("st_last_name", ""));
                prefEditor.commit();
                CommentsDetailsView commentAddView = new CommentsDetailsView();
                commentAddView.show(getSupportFragmentManager(), "commentAddView");
                return true;
            }
        });
        takeChangeCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("user_id", "").equals("")) {
                    Intent intent = new Intent(Activity_DayPrefrence_Display.this, TabsFragmentActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }


            }
        });


        takeChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("user_id", "").equals("")) {
                    Intent intent = new Intent(Activity_DayPrefrence_Display.this, Activity_Location_Details.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }


            }
        });

        takeTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!preferences.getString("user_id", "").equals("")) {
                    Intent intent = new Intent(Activity_DayPrefrence_Display.this, TemplateDisplayActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }
            }
        });

        takeChangeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("user_id", "").equals("")) {
                    finish();
                    Intent intent = new Intent(Activity_DayPrefrence_Display.this, DisplayStudentEditPreActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }


            }
        });

        new getDayPrefrenceList().execute();
    }

    //
    private class getDayPrefrenceList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(Activity_DayPrefrence_Display.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {


            data = new ArrayList<>();
            Cursor cursor = db.getUserDayPre(Integer.parseInt(preferences.getString("user_id", "")));
            assert cursor != null;
            Log.d("total count", "" + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    data.add(new DayPrefrenceDAO(cursor.getString(cursor.getColumnIndex(StudentContract.UserDayPrefrence.COLUMN_NAME_ENTRY_ID)),
                            cursor.getString(cursor.getColumnIndex("Prefrence")),
                            cursor.getString(cursor.getColumnIndex(StudentContract.Branches.COLUMN_NAME_M_TIMESTAMP)),
                            cursor.getString(cursor.getColumnIndex("isselected")), false));
                } while (cursor.moveToNext());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (data.size() > 0) {

                dayPrefrenceListAdpter = new DayPrefrenceListAdpter(Activity_DayPrefrence_Display.this, data);
                mleadList.setAdapter(dayPrefrenceListAdpter);
                mleadList.setLayoutManager(new LinearLayoutManager(Activity_DayPrefrence_Display.this));
                // Close the progressdialog
                mProgressDialog.dismiss();
            } else {

                // Close the progressdialog
                mProgressDialog.dismiss();
            }
        }
    }


    protected boolean isJSONValid(String callReoprtResponse2) {
        // TODO Auto-generated method stub
        try {
            new JSONObject(callReoprtResponse2);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(callReoprtResponse2);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}


