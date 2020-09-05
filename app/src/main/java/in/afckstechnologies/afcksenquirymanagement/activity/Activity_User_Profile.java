package in.afckstechnologies.afcksenquirymanagement.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentContract;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;
import in.afckstechnologies.afcksenquirymanagement.utils.AppStatus;
import in.afckstechnologies.afcksenquirymanagement.utils.Config;
import in.afckstechnologies.afcksenquirymanagement.utils.Constant;
import in.afckstechnologies.afcksenquirymanagement.utils.FeesListener;
import in.afckstechnologies.afcksenquirymanagement.utils.SyncUtils;
import in.afckstechnologies.afcksenquirymanagement.utils.WebClient;

public class Activity_User_Profile extends AppCompatActivity {
    String updateResponse = "";
    private JSONObject jsonLeadObj, jsonObj;
    JSONArray jsonArray;
    String bankDetailsResponse = "";
    ProgressDialog mProgressDialog;
    Boolean status;
    String msg;
    ImageView deleteProfile, closeProfile;
    Button placeBtn;
    private EditText nameEdtTxt;
    private EditText lastnameEdtTxt;
    private EditText phEdtTxt;
    private EditText emailEdtTxt;

    private TextView titleTxt, nameTxtView, lastNameTxtView, phTxtView, emailTxtView, editTxt;

    String f_name = "";
    String l_name = "";
    String mobile_no = "";
    String email_id = "";
    String gender = "";
    int sync_status;
    RadioGroup radioGroup;
    private RadioButton male, female;
    View registerView;
    int pos;
    int pos1;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String device_id;
    private String deviceId = "";

    private TelephonyManager telephonyManager;

    private static FeesListener mListener;
    //database helper object
    private StudentProvider.AFCKSDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__user__profile);
        preferences = getSharedPreferences("Prefrence", MODE_PRIVATE);
        prefEditor = preferences.edit();
        db = new StudentProvider.AFCKSDatabase(this);
        nameEdtTxt = (EditText) findViewById(R.id.nameEdtTxt);
        lastnameEdtTxt = (EditText) findViewById(R.id.lastNameEdtTxt);
        phEdtTxt = (EditText) findViewById(R.id.phEdtTxt);
        emailEdtTxt = (EditText) findViewById(R.id.emailEdtTxt);
        editTxt = (TextView) findViewById(R.id.editTxt);
        titleTxt = (TextView) findViewById(R.id.viewTxt);
        nameTxtView = (TextView) findViewById(R.id.nameTxtView);
        lastNameTxtView = (TextView) findViewById(R.id.lastNameTxtView);
        phTxtView = (TextView) findViewById(R.id.phTxtView);
        emailTxtView = (TextView) findViewById(R.id.emailTxtView);
        deleteProfile = (ImageView) findViewById(R.id.deleteProfile);
        closeProfile = (ImageView) findViewById(R.id.closeProfile);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                // Method 1 For Getting Index of RadioButton
                pos = radioGroup.indexOfChild(findViewById(checkedId));
                switch (pos) {
                    case 1:

                        gender = "Female";
                        // Toast.makeText(getActivity(), "You have Clicked RadioButton 1"+gender,Toast.LENGTH_SHORT).show();
                        break;
                    case 2:

                        gender = "Male";
                        // Toast.makeText(getActivity(), gender, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        //The default selection is RadioButton 1
                        gender = "Female";
                        // Toast.makeText(getActivity(), gender,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        placeBtn = (Button) findViewById(R.id.placeBtn);
        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!preferences.getString("user_id", "").equals("")) {
                    String name = preferences.getString("st_first_name", "") + " " + preferences.getString("st_last_name", "");
                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_User_Profile.this);
                    builder.setMessage("Do you want to delete " + name + " ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.updateUserDeleteStatus(preferences.getString("user_id", ""), 3);
                                    dialog.cancel();
                                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        prefEditor.remove("user_id");
                                        prefEditor.remove("st_first_name");
                                        prefEditor.remove("st_last_name");
                                        prefEditor.remove("student_mob_sms");
                                        prefEditor.remove("student_mail_id");
                                        prefEditor.putString("delete_flag","1");
                                        prefEditor.commit();
                                        finishAffinity();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        prefEditor.remove("user_id");
                                        prefEditor.remove("st_first_name");
                                        prefEditor.remove("st_last_name");
                                        prefEditor.remove("student_mob_sms");
                                        prefEditor.remove("student_mail_id");
                                        prefEditor.putString("delete_flag","1");
                                        prefEditor.commit();
                                        ActivityCompat.finishAffinity(Activity_User_Profile.this);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }*/
                                    prefEditor.remove("user_id");
                                    prefEditor.remove("st_first_name");
                                    prefEditor.remove("st_last_name");
                                    prefEditor.remove("student_mob_sms");
                                    prefEditor.remove("student_mail_id");
                                    prefEditor.putString("delete_flag","1");
                                    prefEditor.commit();
                                    mListener.messageReceived(gender);
                                    Intent i = new Intent(Activity_User_Profile.this, MainActivity.class);
                                    i.putExtra("frgToLoad", 0);

// Now start your activity
                                    startActivity(i);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Deleting user");
                    alert.show();

                }

            }
        });

        closeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameEdtTxt.setVisibility(View.GONE);
                lastnameEdtTxt.setVisibility(View.GONE);
                phEdtTxt.setVisibility(View.GONE);
                emailEdtTxt.setVisibility(View.GONE);
                placeBtn.setVisibility(View.GONE);
                editTxt.setVisibility(View.GONE);
                closeProfile.setVisibility(View.GONE);

                nameTxtView.setVisibility(View.VISIBLE);
                lastNameTxtView.setVisibility(View.VISIBLE);
                phTxtView.setVisibility(View.VISIBLE);
                emailTxtView.setVisibility(View.VISIBLE);

                titleTxt.setVisibility(View.VISIBLE);


            }
        });
        placeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameEdtTxt.getText().toString();
                String lastname = lastnameEdtTxt.getText().toString();
                String phoneno = phEdtTxt.getText().toString();
                String emailid = emailEdtTxt.getText().toString();
                prefEditor.putString("student_mob_sms", phoneno);
                prefEditor.putString("student_mail_id", emailid);
                prefEditor.commit();
                if (validate(name, phoneno)) {

                    // Toast.makeText(getApplicationContext(), "Thank You! your information is valuable for us.", Toast.LENGTH_LONG).show();

                    if (sync_status == 1) {
                        sync_status = 2;
                    }
                    db.updateUserDetails(preferences.getString("user_id", ""), name, lastname, phoneno, emailid, gender, sync_status);
                    finish();
                    if (preferences.getString("edit_u_p_flag", "").equals("fu")) {
                        mListener.messageReceived(gender);
                    }
                    if (preferences.getString("edit_u_p_flag", "").equals("dfu")) {
                        finish();
                    }


                }


            }
        });


        new getData().execute();


    }

    public boolean validate(String name, String phoneno) {
        boolean isValidate = false;
        if (name.trim().compareTo("") == 0 || phoneno.trim().compareTo("") == 0) {
            Toast.makeText(getApplicationContext(), "Please enter all the values.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (phoneno.trim().compareTo("") == 0 || phoneno.length() != 10) {
            Toast.makeText(getApplicationContext(), "Please enter a 10 digit valid Mobile No.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } /*else if (!validateEmail(emailid)) {
            Toast.makeText(getApplicationContext(), "Please enter valid Email Id.", Toast.LENGTH_LONG).show();
            isValidate = false;
        }*/ else {
            isValidate = true;
        }
        return isValidate;
    }

    /**
     * email validation
     */
    private final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(

            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$");

    public boolean validateEmail(String email) {
        if (!email.contains("@")) {
            return false;
        }
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private class getData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {


            Cursor cursor = db.getUserDetails(preferences.getString("user_id", ""));
            if (cursor.moveToFirst()) {
                do {
                    //calling the method to save the unsynced name to MySQL
                    f_name = cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_FIRST_NAME));
                    l_name = cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_LAST_NAME));
                    email_id = cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_EMAIL_ID));
                    mobile_no = cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_MOBILE_NO));
                    gender = cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_GENDER));
                    sync_status = cursor.getInt(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_SYNC_STATUS));

                } while (cursor.moveToNext());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            nameTxtView.setText(f_name);
            lastNameTxtView.setText(l_name);
            phTxtView.setText(mobile_no);
            emailTxtView.setText(email_id);

            nameEdtTxt.setText(f_name);
            lastnameEdtTxt.setText(l_name);
            phEdtTxt.setText(mobile_no);
            emailEdtTxt.setText(email_id);
            if (gender.equals("Male")) {
                male.setChecked(true);
            }
            if (gender.equals("Female")) {
                female.setChecked(true);
            }

        }
    }


    public void sendData(final String name, final String phoneno, final String emailid, final String lastname) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        final String date = format.format(cal.getTime());

        jsonObj = new JSONObject() {
            {
                try {
                    put("id", preferences.getString("user_id", ""));
                    put("first_name", name);
                    put("last_name", lastname);
                    put("mobile_no", phoneno);
                    put("email_id", emailid);
                    put("gender", gender);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();

                Log.i("jsonObj", "jsonObj" + jsonObj);
                updateResponse = serviceAccess.SendHttpPost(Config.URL_UPDATE_USER_DETAILS, jsonObj);

                Log.i("updateResponse", "updateResponse" + updateResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (updateResponse.compareTo("") == 0) {
                                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                                } else {

                                    try {
                                        JSONObject jObject = new JSONObject(updateResponse);
                                        status = jObject.getBoolean("status");
                                        msg = jObject.getString("message");
                                        if (status) {
                                            if (sync_status == 1) {
                                                sync_status = 1;
                                            }
                                            db.updateUserDetails(preferences.getString("user_id", ""), name, lastname, phoneno, emailid, gender, sync_status);
                                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                            finish();
                                            if (preferences.getString("edit_u_p_flag", "").equals("fu")) {
                                                mListener.messageReceived(gender);
                                            }
                                            if (preferences.getString("edit_u_p_flag", "").equals("dfu")) {
                                                finish();
                                            }

                                        } else {
                                            // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                };

                new Thread(runnable).start();
            }
        });
        objectThread.start();
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

    public static void bindListener(FeesListener listener) {
        mListener = listener;
    }
}

