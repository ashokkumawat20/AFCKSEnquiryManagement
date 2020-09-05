package in.afckstechnologies.afcksenquirymanagement.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.activity.Activity_User_Profile;
import in.afckstechnologies.afcksenquirymanagement.activity.DisplayStudentEditPreActivity;
import in.afckstechnologies.afcksenquirymanagement.adapter.CenterListAdpter;
import in.afckstechnologies.afcksenquirymanagement.models.CenterDAO;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentContract;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;


/**
 * A simple {@link Fragment} subclass.
 */
public class Activity_Location_Details extends Fragment {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    //database helper object
    private StudentProvider.AFCKSDatabase db;
    CenterDAO centerDAO;
    private RecyclerView mleadList;
    //
    List<CenterDAO> data;
    CenterListAdpter centerListAdpter;
    ArrayList<String> centerIdArrayList;
    TextView stName;
    View v;
    public Activity_Location_Details() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.activity__location__details, container, false);
        preferences = getActivity().getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        db = new StudentProvider.AFCKSDatabase(getActivity());
        centerIdArrayList = new ArrayList<>();
        mleadList = (RecyclerView) v.findViewById(R.id.centerList);
        stName=(TextView)v.findViewById(R.id.stName);
        data = new ArrayList<>();
        if(!preferences.getString("user_id", "").equals("")) {
            stName.setText(preferences.getString("user_id", "")+"-"+preferences.getString("st_first_name", "").toUpperCase() + " " + preferences.getString("st_last_name", "").toUpperCase());
            getCenterList();
        }
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
        if(!preferences.getString("user_id", "").equals("")) {
            stName.setText(preferences.getString("user_id", "")+"-"+preferences.getString("st_first_name", "").toUpperCase() + " " + preferences.getString("st_last_name", "").toUpperCase());
            getCenterList();
        }
    }


    public void getCenterList() {

        data.clear();

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
            centerListAdpter = new CenterListAdpter(getActivity(), data);
            mleadList.setAdapter(centerListAdpter);
            mleadList.setLayoutManager(new LinearLayoutManager(getActivity()));
        }


    }
}
