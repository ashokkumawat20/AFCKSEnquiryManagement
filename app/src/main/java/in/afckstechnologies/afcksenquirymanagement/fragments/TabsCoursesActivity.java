package in.afckstechnologies.afcksenquirymanagement.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.activity.Activity_User_Profile;
import in.afckstechnologies.afcksenquirymanagement.adapter.NewCoursesListAdpter;
import in.afckstechnologies.afcksenquirymanagement.models.NewCoursesDAO;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentContract;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;


public class TabsCoursesActivity extends Fragment {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    private FragmentTabHost mTabHost;
    TextView stName;
    private RecyclerView mleadList;
    //
    List<NewCoursesDAO> data;
    NewCoursesListAdpter coursesListAdpter;
    //database helper object
    private StudentProvider.AFCKSDatabase db;
    Button entry_level,spli_level;
    int flag=1;
    String cflag="1";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v = inflater.inflate(R.layout.courses_new_tabs, container, false);
        // Setting ViewPager for each Tabs
        db = new StudentProvider.AFCKSDatabase(getActivity());
        preferences = getActivity().getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        stName=(TextView)v.findViewById(R.id.stName);
        mleadList = (RecyclerView) v.findViewById(R.id.coursesList);
        entry_level=(Button)v.findViewById(R.id.entry_level);
        spli_level=(Button)v.findViewById(R.id.spli_level);
        data = new ArrayList<>();
        stName.setText(preferences.getString("user_id", "")+"-"+preferences.getString("st_first_name", "").toUpperCase() + " " + preferences.getString("st_last_name", "").toUpperCase());
        new getCoursesList().execute();
        entry_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                cflag="1";
                entry_level.setBackgroundColor(Color.parseColor("#809E9E9E"));
                spli_level.setBackgroundColor(Color.parseColor("#a8a8a8"));
                new getCoursesList().execute();
            }
        });
        spli_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=2;
                cflag="2";
                entry_level.setBackgroundColor(Color.parseColor("#a8a8a8"));
                spli_level.setBackgroundColor(Color.parseColor("#809E9E9E"));
                new getCoursesList().execute();
            }
        });
        stName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("user_id", "").equals("")) {
                    prefEditor.putString("edit_u_p_flag", "dfu");
                    prefEditor.commit();
                    Intent intent = new Intent(getActivity(), Activity_User_Profile.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getActivity(), "Please select student from list", Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        // Here we call the data setup methods again, to reflect
        // the changes which took place then the Fragment was paused
        stName.setText(preferences.getString("user_id", "")+"-"+preferences.getString("st_first_name", "").toUpperCase() + " " + preferences.getString("st_last_name", "").toUpperCase());
        new getCoursesList().execute();

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
            data.clear();
            Cursor cursor=null;
            if(!preferences.getString("user_id", "").equals("")) {
                cursor = db.getCourses(Integer.parseInt(preferences.getString("user_id", "")), flag);

                assert cursor != null;
                Log.d("total count", "" + cursor.getCount());

                if (cursor.moveToFirst()) {
                    do {
                        if(cursor.getString(cursor.getColumnIndex(StudentContract.Courses.COLUMN_NAME_COURSE_TYPE_ID)).equals(cflag)) {
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
            if (data.size()>0) {

                coursesListAdpter = new NewCoursesListAdpter(getActivity(), data);
                mleadList.setAdapter(coursesListAdpter);
                mleadList.setLayoutManager(new LinearLayoutManager(getActivity()));


            }
        }
    }
}
