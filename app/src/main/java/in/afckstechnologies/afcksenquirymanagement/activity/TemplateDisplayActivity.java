package in.afckstechnologies.afcksenquirymanagement.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.adapter.TemplateListAdpter;
import in.afckstechnologies.afcksenquirymanagement.fragments.TabsFragmentActivity;
import in.afckstechnologies.afcksenquirymanagement.models.TemplatesContactDAO;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentContract;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;
import in.afckstechnologies.afcksenquirymanagement.view.CommentsDetailsView;
import in.afckstechnologies.afcksenquirymanagement.view.MultipleCommentAddView;

public class TemplateDisplayActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    List<TemplatesContactDAO> data;
    TemplateListAdpter templateListAdpter;
    private FloatingActionButton fab;
    public EditText search;
    String flag = "0";
    ImageView serach_hide, clear;
    private RecyclerView mleadList;
    ProgressDialog mProgressDialog;
    //database helper object
    private StudentProvider.AFCKSDatabase db;
    LinearLayout takeChangeHome, takeChangeCourses, takeChangeLocation, takeChangedayprefrence, takeCreateComment, takeBookSeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_main_contact_layout_copy);
        db = new StudentProvider.AFCKSDatabase(this);
        preferences = getSharedPreferences("Prefrence", MODE_PRIVATE);
        prefEditor = preferences.edit();
        mleadList = (RecyclerView) findViewById(R.id.templateList);
        search = (EditText) findViewById(R.id.search);
        serach_hide = (ImageView) findViewById(R.id.serach_hide);
        clear = (ImageView) findViewById(R.id.clear);
        data = new ArrayList<>();
        new getTemplatesList().execute();
        takeCreateComment = (LinearLayout) findViewById(R.id.takeCreateComment);
        takeChangeHome = (LinearLayout) findViewById(R.id.takeChangeHome);
        takeChangeLocation = (LinearLayout) findViewById(R.id.takeChangeLocation);
        takeChangeCourses = (LinearLayout) findViewById(R.id.takeChangeCourses);
        takeChangedayprefrence = (LinearLayout) findViewById(R.id.takeChangedayprefrence);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TemplateDisplayActivity.this, AddTemplateContactsActivity.class);
                startActivity(intent);

            }
        });
        serach_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
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

                mleadList.setLayoutManager(new LinearLayoutManager(TemplateDisplayActivity.this));
                templateListAdpter = new TemplateListAdpter(TemplateDisplayActivity.this, filteredList);
                mleadList.setAdapter(templateListAdpter);
                templateListAdpter.notifyDataSetChanged();  // data set changed
            }
        });
    }
    //
    private class getTemplatesList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(TemplateDisplayActivity.this);
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


            data.clear();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {


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


                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (data.size() > 0) {
                templateListAdpter = new TemplateListAdpter(TemplateDisplayActivity.this, data);
                mleadList.setAdapter(templateListAdpter);
                mleadList.setLayoutManager(new LinearLayoutManager(TemplateDisplayActivity.this));
                // Close the progressdialog
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();

            }
        }
    }
}
