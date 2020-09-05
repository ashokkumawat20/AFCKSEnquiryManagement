package in.afckstechnologies.afcksenquirymanagement.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.adapter.LoginLogsDetailsListAdpter;
import in.afckstechnologies.afcksenquirymanagement.adapter.TemplateListAdpter;
import in.afckstechnologies.afcksenquirymanagement.models.StudentsLoginLogsDAO;
import in.afckstechnologies.afcksenquirymanagement.models.TemplatesContactDAO;
import in.afckstechnologies.afcksenquirymanagement.utils.AppStatus;
import in.afckstechnologies.afcksenquirymanagement.utils.Config;
import in.afckstechnologies.afcksenquirymanagement.utils.Constant;
import in.afckstechnologies.afcksenquirymanagement.utils.JsonHelper;
import in.afckstechnologies.afcksenquirymanagement.utils.WebClient;

public class LoginLogsActivity extends AppCompatActivity {
    ImageView closeDetails;
    String pNameFlag = "", studentLoginLogsListResponse = "";
    private JSONObject jsonLeadObj;
    ProgressDialog mProgressDialog;
    List<StudentsLoginLogsDAO> data;
    JSONArray jsonArray;
    LoginLogsDetailsListAdpter loginLogsDetailsListAdpter;
    RecyclerView loginLogsList;
    public EditText search;
    ImageView serach_hide, clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_logs);
        closeDetails = (ImageView) findViewById(R.id.closeDetails);
        loginLogsList = (RecyclerView) findViewById(R.id.loginLogsList);
        search = (EditText) findViewById(R.id.search);
        serach_hide = (ImageView) findViewById(R.id.serach_hide);
        clear = (ImageView) findViewById(R.id.clear);
        Intent intent = getIntent();
        pNameFlag = intent.getStringExtra("pNameFlag");
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            new getStudentLoginLogsList().execute();
        } else {
            Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
        }
        closeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                final List<StudentsLoginLogsDAO> filteredList = new ArrayList<StudentsLoginLogsDAO>();
                if (data != null) {
                    if (data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            String subject = data.get(i).getName().toLowerCase();
                            if (subject.contains(query)) {
                                filteredList.add(data.get(i));
                            }
                        }
                    }
                }

                loginLogsList.setLayoutManager(new LinearLayoutManager(LoginLogsActivity.this));
                loginLogsDetailsListAdpter = new LoginLogsDetailsListAdpter(LoginLogsActivity.this, filteredList);
                loginLogsList.setAdapter(loginLogsDetailsListAdpter);
                loginLogsDetailsListAdpter.notifyDataSetChanged();  // data set changed


            }
        });
    }

    //
    private class getStudentLoginLogsList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(LoginLogsActivity.this);
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

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("pNameFlag", pNameFlag);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/srujanlms_new/api/Leadraw/showleadraw";
            Log.i("json", "json" + jsonLeadObj);
            studentLoginLogsListResponse = serviceAccess.SendHttpPost(Config.URL_GETALLLOGINLOGS, jsonLeadObj);
            Log.i("resp", "batchesListResponse" + studentLoginLogsListResponse);
            if (studentLoginLogsListResponse.compareTo("") != 0) {
                if (isJSONValid(studentLoginLogsListResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                data = new ArrayList<>();
                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.studentLoginLogsList(studentLoginLogsListResponse);
                                jsonArray = new JSONArray(studentLoginLogsListResponse);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (data.size() > 0) {
                mProgressDialog.dismiss();
                loginLogsDetailsListAdpter = new LoginLogsDetailsListAdpter(LoginLogsActivity.this, data);
                loginLogsList.setAdapter(loginLogsDetailsListAdpter);
                loginLogsList.setLayoutManager(new LinearLayoutManager(LoginLogsActivity.this));


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
