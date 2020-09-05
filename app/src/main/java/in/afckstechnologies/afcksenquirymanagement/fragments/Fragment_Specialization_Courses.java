package in.afckstechnologies.afcksenquirymanagement.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.adapter.NewCoursesListAdpter;
import in.afckstechnologies.afcksenquirymanagement.models.NewCoursesDAO;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentContract;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;
import in.afckstechnologies.afcksenquirymanagement.utils.Config;
import in.afckstechnologies.afcksenquirymanagement.utils.FeesListener;
import in.afckstechnologies.afcksenquirymanagement.utils.JsonHelper;
import in.afckstechnologies.afcksenquirymanagement.utils.WebClient;


public class Fragment_Specialization_Courses extends Fragment {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String center_id = "";

    private JSONObject jsonLeadObj;
    JSONArray jsonArray;
    String courseListResponse = "";
    ProgressDialog mProgressDialog;


    private RecyclerView mleadList;
    //
    List<NewCoursesDAO> data;
    NewCoursesListAdpter coursesListAdpter;
    ArrayList<String> centerIdArrayList;
    Button btnSubmit;
    //database helper object
    private StudentProvider.AFCKSDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment__specialization__courses, container, false);
        preferences = getActivity().getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        mleadList = (RecyclerView) v.findViewById(R.id.coursesList);
        db = new StudentProvider.AFCKSDatabase(getActivity());
        new getCoursesList().execute();

        NewCoursesListAdpter.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                new getCoursesList().execute();
            }
        });

        return v;
    }

    //
    private class getCoursesList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog

        }

        @Override
        protected Void doInBackground(Void... params) {
            data = new ArrayList<>();
            Cursor cursor=null;
            if(!preferences.getString("user_id", "").equals("")) {
                cursor = db.getCourses(Integer.parseInt(preferences.getString("user_id", "")), 2);
                assert cursor != null;
                Log.d("total count", "" + cursor.getCount());

                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getString(cursor.getColumnIndex(StudentContract.Courses.COLUMN_NAME_COURSE_TYPE_ID)).equals("2")) {
                            data.add(new NewCoursesDAO(cursor.getString(cursor.getColumnIndex(StudentContract.Courses.COLUMN_NAME_ENTRY_ID)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.Courses.COLUMN_NAME_COURSE_TYPE_ID)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.Courses.COLUMN_NAME_COURSE_CODE)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.Courses.COLUMN_NAME_COURSE_NAME)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.Courses.COLUMN_NAME_TIME_DURATION)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.Courses.COLUMN_NAME_PREREQUISITE)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.Courses.COLUMN_NAME_RECOMMONDED)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.Courses.COLUMN_NAME_FEES)),
                                    cursor.getString(cursor.getColumnIndex("isselected")), false));
                        }

                    } while (cursor.moveToNext());
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (data.size() > 0) {
                coursesListAdpter = new NewCoursesListAdpter(getActivity(), data);
                mleadList.setAdapter(coursesListAdpter);
                mleadList.setLayoutManager(new LinearLayoutManager(getActivity()));
                // Close the progressdialog

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
