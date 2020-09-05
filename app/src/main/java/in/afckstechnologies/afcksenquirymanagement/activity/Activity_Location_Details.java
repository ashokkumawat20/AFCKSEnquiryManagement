package in.afckstechnologies.afcksenquirymanagement.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import in.afckstechnologies.afcksenquirymanagement.adapter.CenterListAdpter;
import in.afckstechnologies.afcksenquirymanagement.fragments.TabsFragmentActivity;
import in.afckstechnologies.afcksenquirymanagement.models.CenterDAO;
import in.afckstechnologies.afcksenquirymanagement.models.TemplatesContactDAO;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentContract;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;
import in.afckstechnologies.afcksenquirymanagement.utils.Config;
import in.afckstechnologies.afcksenquirymanagement.utils.FeesListener;
import in.afckstechnologies.afcksenquirymanagement.utils.JsonHelper;
import in.afckstechnologies.afcksenquirymanagement.utils.WebClient;
import in.afckstechnologies.afcksenquirymanagement.view.CommentsDetailsView;
import in.afckstechnologies.afcksenquirymanagement.view.MultipleCommentAddView;

public class Activity_Location_Details extends AppCompatActivity {
    LinearLayout takeChangeHome, takeChangeCourses, takeChangedayprefrence, takeTemplate, takeCreateComment, takeBookSeat, takeEditUser, takeDeleteUser;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    Button btnSearch;

    JSONObject jsonCenterObj, jsonobject, jsonLeadObj;
    JSONArray jsonarray;
    String centerResponse = "";

    String centerListResponse = "";
    ProgressDialog mProgressDialog;
    CenterDAO centerDAO;
    private RecyclerView mleadList;
    //
    List<CenterDAO> data;
    CenterListAdpter centerListAdpter;
    ArrayList<String> centerIdArrayList;
    //database helper object
    private StudentProvider.AFCKSDatabase db;
    Boolean status;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__location__details);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        db = new StudentProvider.AFCKSDatabase(this);
        getCenterList();
        centerIdArrayList = new ArrayList<>();
        mleadList = (RecyclerView) findViewById(R.id.centerList);
        takeCreateComment = (LinearLayout) findViewById(R.id.takeCreateComment);
        takeChangeHome = (LinearLayout) findViewById(R.id.takeChangeHome);
        takeChangedayprefrence = (LinearLayout) findViewById(R.id.takeChangedayprefrence);
        takeChangeCourses = (LinearLayout) findViewById(R.id.takeChangeCourses);
        takeTemplate = (LinearLayout) findViewById(R.id.takeTemplate);
        takeBookSeat = (LinearLayout) findViewById(R.id.takeBookSeat);
        takeBookSeat = (LinearLayout) findViewById(R.id.takeBookSeat);

        takeBookSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("user_id", "").equals("")) {
                    //BookSeatView bookSeatView = new BookSeatView();
                    //bookSeatView.show(getSupportFragmentManager(), "registrationView");
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
                    finish();
                    Intent intent = new Intent(Activity_Location_Details.this, TabsFragmentActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }


            }
        });


        takeChangedayprefrence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("user_id", "").equals("")) {
                    finish();
                    Intent intent = new Intent(Activity_Location_Details.this, Activity_DayPrefrence_Display.class);
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
                    finish();
                    Intent intent = new Intent(Activity_Location_Details.this, TemplateDisplayActivity.class);
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
                    Intent intent = new Intent(Activity_Location_Details.this, DisplayStudentEditPreActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }


            }
        });

        CenterListAdpter.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                getCenterList();
            }
        });


    }

    public void getCenterList() {
        Thread objectThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub


                centerDAO = new CenterDAO();
                data = new ArrayList<>();

                Cursor cursor = db.getBranches(Integer.parseInt(preferences.getString("user_id", "")));
                assert cursor != null;
                Log.d("total count", "" + cursor.getCount());

                if (cursor.moveToFirst()) {
                    do {
                        data.add(new CenterDAO(cursor.getString(cursor.getColumnIndex(StudentContract.Branches.COLUMN_NAME_ENTRY_ID)),
                                cursor.getString(cursor.getColumnIndex(StudentContract.Branches.COLUMN_NAME_BRANCH_NAME)),
                                cursor.getString(cursor.getColumnIndex(StudentContract.Branches.COLUMN_NAME_LATITUDE)),
                                cursor.getString(cursor.getColumnIndex(StudentContract.Branches.COLUMN_NAME_LONGITUDE)),
                                cursor.getString(cursor.getColumnIndex(StudentContract.Branches.COLUMN_NAME_BRANCH_SHORT)),
                                cursor.getString(cursor.getColumnIndex(StudentContract.Branches.COLUMN_NAME_M_TIMESTAMP)),
                                cursor.getString(cursor.getColumnIndex(StudentContract.Branches.COLUMN_NAME_ADDRESS)),
                                cursor.getString(cursor.getColumnIndex("isselected")), false));
                    } while (cursor.moveToNext());
                }
                if (data.size() > 0) {
                    centerListAdpter = new CenterListAdpter(Activity_Location_Details.this, data);
                    mleadList.setAdapter(centerListAdpter);
                    mleadList.setLayoutManager(new LinearLayoutManager(Activity_Location_Details.this));
                }
            }
        });

        objectThread.start();
    }


}
