package in.afckstechnologies.afcksenquirymanagement.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.activity.Activity_User_Profile;
import in.afckstechnologies.afcksenquirymanagement.activity.AddTemplateContactsActivity;
import in.afckstechnologies.afcksenquirymanagement.adapter.TemplateListAdpter;
import in.afckstechnologies.afcksenquirymanagement.models.TemplatesContactDAO;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentContract;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;
import in.afckstechnologies.afcksenquirymanagement.utils.ConnectivityReceiver;


/**
 * A simple {@link Fragment} subclass.
 */
public class TemplateDisplayActivity extends Fragment {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    List<TemplatesContactDAO> data;
    TemplateListAdpter templateListAdpter;
    private FloatingActionButton fab;
    public EditText search;
    String flag = "0";
    ImageView serach_hide, clear;
    private RecyclerView mleadList;
    TextView stName;
    //database helper object
    private StudentProvider.AFCKSDatabase db;
    View v;

    public TemplateDisplayActivity() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  @Nullable ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.floating_main_contact_layout, container, false);
        db = new StudentProvider.AFCKSDatabase(getActivity());
        getActivity().registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        preferences = getActivity().getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        mleadList = (RecyclerView) v.findViewById(R.id.templateList);
        search = (EditText) v.findViewById(R.id.search);
        serach_hide = (ImageView) v.findViewById(R.id.serach_hide);
        clear = (ImageView) v.findViewById(R.id.clear);
        stName=(TextView)v.findViewById(R.id.stName);
        stName.setText(preferences.getString("user_id", "")+"-"+preferences.getString("st_first_name", "").toUpperCase() + " " + preferences.getString("st_last_name", "").toUpperCase());
        data = new ArrayList<>();
        getTemplatesList();
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
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTemplateContactsActivity.class);
                startActivity(intent);

            }
        });
        serach_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                clear.setVisibility(View.VISIBLE);
                serach_hide.setVisibility(View.GONE);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
                serach_hide.setVisibility(View.VISIBLE);
                clear.setVisibility(View.GONE);

            }
        });
        addTextListener();
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Here we call the data setup methods again, to reflect
        // the changes which took place then the Fragment was paused
        stName.setText(preferences.getString("user_id", "")+"-"+preferences.getString("st_first_name", "").toUpperCase() + " " + preferences.getString("st_last_name", "").toUpperCase());
      // getTemplatesList();
    }
    //
    public void addTextListener() {

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                clear.setVisibility(View.VISIBLE);
                serach_hide.setVisibility(View.GONE);

                query = query.toString().toLowerCase();
                final List<TemplatesContactDAO> filteredList = new ArrayList<TemplatesContactDAO>();
                if (data != null) {
                    if (data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {

                            String subject = data.get(i).getSubject().toLowerCase();
                            String tag = data.get(i).getTag().toLowerCase();
                            String msg_txt = data.get(i).getSubject().toLowerCase();
                            if (subject.contains(query)) {
                                filteredList.add(data.get(i));
                            } else if (tag.contains(query)) {

                                filteredList.add(data.get(i));
                            } else if (msg_txt.contains(query)) {

                                filteredList.add(data.get(i));
                            }
                        }
                    }
                }

                mleadList.setLayoutManager(new LinearLayoutManager(getActivity()));
                templateListAdpter = new TemplateListAdpter(getActivity(), filteredList);
                mleadList.setAdapter(templateListAdpter);
                templateListAdpter.notifyDataSetChanged();  // data set changed
            }
        });
    }
    //
    public void getTemplatesList() {
        data.clear();
        Cursor cursor = db.getTemplatesDetails();
        assert cursor != null;
        //Log.d("total count",""+cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                data.add(new TemplatesContactDAO(cursor.getString(cursor.getColumnIndex(StudentContract.TrainersTemplate.COLUMN_NAME_ENTRY_ID)),
                        cursor.getString(cursor.getColumnIndex(StudentContract.TrainersTemplate.COLUMN_NAME_SUBJECT)),
                        cursor.getString(cursor.getColumnIndex(StudentContract.TrainersTemplate.COLUMN_NAME_TAG)),
                        cursor.getString(cursor.getColumnIndex(StudentContract.TrainersTemplate.COLUMN_NAME_TEMPLATE_TEXT)), false));

            } while (cursor.moveToNext());
        }


        if (data.size() > 0) {
            templateListAdpter = new TemplateListAdpter(getActivity(), data);
            mleadList.setAdapter(templateListAdpter);
            mleadList.setLayoutManager(new LinearLayoutManager(getActivity()));

        }
    }
}
