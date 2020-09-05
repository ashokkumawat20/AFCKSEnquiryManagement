package in.afckstechnologies.afcksenquirymanagement.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.adapter.PreBatchDetailsListAdpter;
import in.afckstechnologies.afcksenquirymanagement.adapter.StCenterListAdpter;
import in.afckstechnologies.afcksenquirymanagement.adapter.StCoursesListAdpter;
import in.afckstechnologies.afcksenquirymanagement.fragments.TabsFragmentActivity;
import in.afckstechnologies.afcksenquirymanagement.models.ContactListDAO;
import in.afckstechnologies.afcksenquirymanagement.models.StCenterDAO;
import in.afckstechnologies.afcksenquirymanagement.models.StCoursesDAO;
import in.afckstechnologies.afcksenquirymanagement.models.StudentListDAO;
import in.afckstechnologies.afcksenquirymanagement.models.StudentsInBatchListDAO;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentContract;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;
import in.afckstechnologies.afcksenquirymanagement.utils.AppStatus;
import in.afckstechnologies.afcksenquirymanagement.utils.Config;
import in.afckstechnologies.afcksenquirymanagement.utils.ConnectivityReceiver;
import in.afckstechnologies.afcksenquirymanagement.utils.Constant;
import in.afckstechnologies.afcksenquirymanagement.utils.MyApplication;
import in.afckstechnologies.afcksenquirymanagement.utils.SmsListener;
import in.afckstechnologies.afcksenquirymanagement.utils.SyncUtils;
import in.afckstechnologies.afcksenquirymanagement.utils.WebClient;
import in.afckstechnologies.afcksenquirymanagement.view.CommentAddView;
import in.afckstechnologies.afcksenquirymanagement.view.CommentsDetailsView;
import in.afckstechnologies.afcksenquirymanagement.view.MultipleCommentAddView;
import in.afckstechnologies.afcksenquirymanagement.view.RegistrationView;

public class DisplayStudentEditPreActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    static String sms_user = "";
    static String sms_pass = "";
    JSONObject jsonCenterObj, jsonobject, jsonLeadObj, jsonLeadObj1, jsonObj1, jsonObj;
    ImageView clear, whatsapp, calling, dummy, settingimg;
    LinearLayout takeChangeLocation, takeChangeCourses, takeChangedayprefrence, takeTemplate, takeCreateComment, takeAddUser, takeEditUser, takeDeleteUser;

    //verify and duefees
    String verifyMobileDeviceIdResponse = "";
    boolean statusv;
    String mobileDeviceId = "";
    String st_name = "", course_name = "", next_due_date = "", st_mobile = "", da = "", dueFeesSMSResponse = "";
    String sms = "";
    String batch_id = "";
    String reminder_count = "";
    //Last call
    String dir = null;
    List<ContactListDAO> contactListDAOArrayList = new ArrayList<ContactListDAO>();
    ContactListDAO contactListDAO;
    String dName = "";
    String email_id = "";
    String first_name = "";
    String last_name = "";
    String mobile_no = "";
    //user verify by mobile no
    boolean status;
    String user_id = "";
    //database helper object
    private StudentProvider.AFCKSDatabase db;
    //serach student
    AutoCompleteTextView autoCompleteTextViewStudent;
    TextView showContacts;
    public static SmsListener mListener;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    String newText;
    public List<StudentListDAO> studentArrayList;
    public ArrayAdapter<StudentListDAO> aAdapter;
    //get user location
    List<StCenterDAO> data;
    StCenterListAdpter centerListAdpter;
    private RecyclerView mleadList;
    //get user courses
    List<StCoursesDAO> cdata;
    private RecyclerView cmleadList;
    StCoursesListAdpter coursesListAdpter;
    //get history
    Button userPreferences, userbatches;
    //
    String usernotes = "";
    TextView commentEdtTxt;
    //user histroy
    private RecyclerView preBatchStudentsDetailsList;
    List<StudentsInBatchListDAO> pre_batch_data;
    PreBatchDetailsListAdpter preBatchDetailsListAdpter;
    //refresh
    SwipeRefreshLayout mSwipeRefreshLayout;
    //user delete
    String userDeleteResponse = "";
    String message = "";

    int refreshflag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_student_edit_pre);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        db = new StudentProvider.AFCKSDatabase(this);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        sms_user = preferences.getString("sms_username", "");
        sms_pass = preferences.getString("sms_password", "");
        showContacts = (TextView) findViewById(R.id.showContacts);
        dummy = (ImageView) findViewById(R.id.dummy);
        clear = (ImageView) findViewById(R.id.clear);
        whatsapp = (ImageView) findViewById(R.id.whatsapp);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        calling = (ImageView) findViewById(R.id.calling);
        userPreferences = (Button) findViewById(R.id.userPreferences);
        userbatches = (Button) findViewById(R.id.userbatches);
        studentArrayList = new ArrayList<StudentListDAO>();
        settingimg = (ImageView) findViewById(R.id.settingimg);
        mleadList = (RecyclerView) findViewById(R.id.centerList);
        cmleadList = (RecyclerView) findViewById(R.id.coursesList);
        commentEdtTxt = (TextView) findViewById(R.id.commentEdtTxt);
        takeCreateComment = (LinearLayout) findViewById(R.id.takeCreateComment);
        takeChangeLocation = (LinearLayout) findViewById(R.id.takeChangeLocation);
        takeChangedayprefrence = (LinearLayout) findViewById(R.id.takeChangedayprefrence);
        takeChangeCourses = (LinearLayout) findViewById(R.id.takeChangeCourses);
        takeTemplate = (LinearLayout) findViewById(R.id.takeTemplate);
        takeDeleteUser = (LinearLayout) findViewById(R.id.takeDeleteUser);
        takeAddUser = (LinearLayout) findViewById(R.id.takeAddUser);
        takeEditUser = (LinearLayout) findViewById(R.id.takeEditUser);
        preBatchStudentsDetailsList = (RecyclerView) findViewById(R.id.preBatchStudentsDetailsList);
        pre_batch_data = new ArrayList<>();
        data = new ArrayList<>();
        cdata = new ArrayList<>();
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            // register connection status listener

            verifyMobileDeviceId();
            new dueFeesAvailable().execute();
        } else {

            // Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
        }
        if (preferences.getString("quantity_for_contacts", "").equals("")) {
            prefEditor.putString("quantity_for_contacts", "5");
            prefEditor.commit();
        }
        showContacts.setText(preferences.getString("quantity_for_contacts", ""));
        LastCall();
        RegistrationView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                //Toast.makeText(getApplicationContext(), messageText, Toast.LENGTH_LONG).show();
                autoCompleteTextViewStudent.setText(messageText);
                mobile_no = messageText;
                new availableStudent().execute();

            }
        });
//auto search
        autoCompleteTextViewStudent = (AutoCompleteTextView) findViewById(R.id.SearchStudent);
        autoCompleteTextViewStudent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextViewStudent.getText())) {
                        clear.setVisibility(View.VISIBLE);
                        dummy.setVisibility(View.GONE);
                        newText = autoCompleteTextViewStudent.getText().toString();
                        getchannelPartnerSelect(newText);

                    }
                }
                return false;
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextViewStudent.setText("");
                userbatches.setText("");
                commentEdtTxt.setText("");
                commentEdtTxt.setVisibility(View.GONE);
                user_id = "";
                clear.setVisibility(View.GONE);
                //  add_student.setVisibility(View.GONE);

                dummy.setVisibility(View.VISIBLE);
                user_id = "";
                prefEditor.remove("user_id");
                prefEditor.remove("st_first_name");
                prefEditor.remove("st_last_name");
                prefEditor.remove("student_mob_sms");
                prefEditor.remove("student_mail_id");
                prefEditor.commit();
                if (data.size() > 0) {
                    data.clear(); // this list which you hava passed in Adapter for your listview
                    centerListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                }
                if (cdata.size() > 0) {
                    cdata.clear(); // this list which you hava passed in Adapter for your listview
                    coursesListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                }
                if (pre_batch_data.size() > 0) {
                    pre_batch_data.clear();
                    preBatchDetailsListAdpter.notifyDataSetChanged();
                }
            }
        });
        userbatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userbatches.setBackgroundColor(getResources().getColor(R.color.color_sbutton));
                userPreferences.setBackgroundColor(getResources().getColor(R.color.color_ubutton));
                if (data.size() > 0) {
                    data.clear(); // this list which you hava passed in Adapter for your listview
                    centerListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                }
                if (cdata.size() > 0) {
                    cdata.clear(); // this list which you hava passed in Adapter for your listview
                    coursesListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                }

                if (!user_id.equals("")) {
                    preBatchStudentsDetailsList.setVisibility(View.VISIBLE);
                    new initBatchDetails().execute();
                }

            }
        });
        userPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPreferences.setBackgroundColor(getResources().getColor(R.color.color_sbutton));
                userbatches.setBackgroundColor(getResources().getColor(R.color.color_ubutton));
                preBatchStudentsDetailsList.setVisibility(View.GONE);
                getUserContinueDiscontinueCount();
                new getLocationList().execute();


            }
        });

        calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user_id.equals("")) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);

                    callIntent.setData(Uri.parse("tel:" + preferences.getString("student_mob_sms", "")));
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                }

            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user_id.equals("")) {
                    PackageManager packageManager = getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    try {
                        String url = "https://api.whatsapp.com/send?phone=" + "91" + preferences.getString("student_mob_sms", "") + "&text=" + URLEncoder.encode("", "UTF-8");

                        if (preferences.getString("enquiry_user_id", "").equals("AT")) {
                            i.setPackage("com.whatsapp.w4b");
                        } else {
                            i.setPackage("com.whatsapp");
                        }
                        i.setData(Uri.parse(url));
                        if (i.resolveActivity(packageManager) != null) {
                            startActivity(i);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        showContacts.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DisplayStudentEditPreActivity.this);
                alertDialog.setMessage("Enter Quantity for display contacts");

                final EditText input = new EditText(DisplayStudentEditPreActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                // alertDialog.setIcon(R.drawable.msg_img);

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String entity = input.getText().toString();
                                showContacts.setText("" + entity);
                                prefEditor.putString("quantity_for_contacts", entity);
                                prefEditor.commit();
                                if (contactListDAOArrayList.size() > 0) {
                                    contactListDAOArrayList.clear();
                                }
                                autoCompleteTextViewStudent.setText("");
                                userbatches.setText("");
                                commentEdtTxt.setText("");
                                commentEdtTxt.setVisibility(View.GONE);
                                user_id = "";
                                clear.setVisibility(View.GONE);
                                prefEditor.remove("user_id");
                                prefEditor.remove("st_first_name");
                                prefEditor.remove("st_last_name");
                                prefEditor.remove("student_mob_sms");
                                prefEditor.remove("student_mail_id");
                                prefEditor.commit();
                                if (data.size() > 0) {
                                    data.clear(); // this list which you hava passed in Adapter for your listview
                                    centerListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                                }
                                if (cdata.size() > 0) {
                                    cdata.clear(); // this list which you hava passed in Adapter for your listview
                                    coursesListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                                }
                                if (pre_batch_data.size() > 0) {
                                    pre_batch_data.clear();
                                    preBatchDetailsListAdpter.notifyDataSetChanged();
                                }
                                String lastcall = LastCall();
                                Log.d("Last call", lastcall);


                            }
                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
                return true;
            }
        });

        settingimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(DisplayStudentEditPreActivity.this, settingimg);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_home, popup.getMenu());
                popup.getMenu().add(preferences.getString("quantity_for_contacts", ""));


                popup.show();
                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        // Toast.makeText(DisplayStudentEditPreActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals("Templates")) {
                            if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                                prefEditor.putString("temp_type_sms", "2");
                                prefEditor.commit();
                                Intent intent = new Intent(getApplicationContext(), TemplateDisplayActivity.class);
                                startActivity(intent);
                            } else {

                                Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                            }

                        } else if (item.getTitle().equals("Sync")) {
                            if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                                SyncUtils.TriggerRefresh();
                            } else {

                                Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                            }
                        } else if (item.getTitle().equals(preferences.getString("quantity_for_contacts", ""))) {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DisplayStudentEditPreActivity.this);
                            alertDialog.setMessage("Enter Quantity for display contacts");

                            final EditText input = new EditText(DisplayStudentEditPreActivity.this);
                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                            input.setText(preferences.getString("quantity_for_contacts", ""));
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            alertDialog.setView(input);
                            // alertDialog.setIcon(R.drawable.msg_img);

                            alertDialog.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            String entity = input.getText().toString();
                                            showContacts.setText("" + entity);
                                            prefEditor.putString("quantity_for_contacts", entity);
                                            prefEditor.commit();
                                            if (contactListDAOArrayList.size() > 0) {
                                                contactListDAOArrayList.clear();
                                            }
                                            autoCompleteTextViewStudent.setText("");
                                            userbatches.setText("");
                                            commentEdtTxt.setText("");
                                            commentEdtTxt.setVisibility(View.GONE);
                                            user_id = "";
                                            clear.setVisibility(View.GONE);
                                            prefEditor.remove("user_id");
                                            prefEditor.remove("st_first_name");
                                            prefEditor.remove("st_last_name");
                                            prefEditor.remove("student_mob_sms");
                                            prefEditor.remove("student_mail_id");
                                            prefEditor.commit();
                                            if (data.size() > 0) {
                                                data.clear(); // this list which you hava passed in Adapter for your listview
                                                centerListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                                            }
                                            if (cdata.size() > 0) {
                                                cdata.clear(); // this list which you hava passed in Adapter for your listview
                                                coursesListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                                            }
                                            if (pre_batch_data.size() > 0) {
                                                pre_batch_data.clear();
                                                preBatchDetailsListAdpter.notifyDataSetChanged();
                                            }
                                            String lastcall = LastCall();
                                            Log.d("Last call", lastcall);


                                        }
                                    });

                            alertDialog.setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            alertDialog.show();
                        }
                        popup.dismiss();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (refreshflag == 0) {
                    // LastCall();
                    if (!user_id.equals("")) {
                        getUserContinueDiscontinueCount();
                        new getLocationList().execute();
                    }
                    if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                        verifyMobileDeviceId();
                        new dueFeesAvailable().execute();

                    } else {

                        Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        takeTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!preferences.getString("user_id", "").equals("")) {
                    // finish();
                    prefEditor.putString("temp_type_sms", "1");
                    prefEditor.commit();
                    Intent intent = new Intent(DisplayStudentEditPreActivity.this, TemplateDisplayActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }
            }
        });
        takeDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    if (!preferences.getString("user_id", "").equals("")) {
                        String name = preferences.getString("st_first_name", "") + " " + preferences.getString("st_last_name", "");

                        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayStudentEditPreActivity.this);
                        builder.setMessage("Do you want to delete " + name + " ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        new deleteUser().execute();
                                        dialog.cancel();
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

                    } else {

                        Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                    }


                } else {
                    if (!preferences.getString("user_id", "").equals("")) {
                        String name = preferences.getString("st_first_name", "") + " " + preferences.getString("st_last_name", "");
                        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayStudentEditPreActivity.this);
                        builder.setMessage("Do you want to delete " + name + " ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        db.updateUserDeleteStatus(preferences.getString("user_id", ""), 3);
                                        autoCompleteTextViewStudent.setText("");
                                        if (data.size() > 0) {
                                            data.clear(); // this list which you hava passed in Adapter for your listview
                                            centerListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                                        }
                                        if (cdata.size() > 0) {
                                            cdata.clear(); // this list which you hava passed in Adapter for your listview
                                            coursesListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                                        }
                                        if (pre_batch_data.size() > 0) {
                                            pre_batch_data.clear();
                                            preBatchDetailsListAdpter.notifyDataSetChanged();
                                        }
                                        dialog.cancel();
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

                    } else {

                        Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                    }

                    // Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }


            }
        });

        takeAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefEditor.putString("student_name", newText);
                preferences.edit().remove("st_mobile_no").commit();
                preferences.edit().remove("emil_id").commit();
                preferences.edit().remove("student_last_name").commit();
                prefEditor.commit();
                RegistrationView registrationView = new RegistrationView();
                registrationView.show(getSupportFragmentManager(), "registrationView");


            }
        });
        RegistrationView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                //Toast.makeText(getApplicationContext(), messageText, Toast.LENGTH_LONG).show();
                autoCompleteTextViewStudent.setText(messageText);
                mobile_no = messageText;
                new availableStudent().execute();

            }
        });

        takeEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!preferences.getString("user_id", "").equals("")) {
                    prefEditor.putString("edit_u_p_flag", "dfu");
                    prefEditor.commit();
                    Intent intent = new Intent(DisplayStudentEditPreActivity.this, Activity_User_Profile.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }


            }
        });

        takeCreateComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    if (!preferences.getString("user_id", "").equals("")) {
                        MultipleCommentAddView commentAddView = new MultipleCommentAddView();
                        commentAddView.show(getSupportFragmentManager(), "commentAddView");
                    } else {

                        Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                    }


                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }

            }
        });

        takeCreateComment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!preferences.getString("user_id", "").equals("")) {
                    prefEditor.putString("da", preferences.getString("st_first_name", "") + " " + preferences.getString("st_last_name", ""));
                    prefEditor.commit();
                    CommentsDetailsView commentAddView = new CommentsDetailsView();
                    commentAddView.show(getSupportFragmentManager(), "commentAddView");
                    return true;
                } else {

                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        takeChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!preferences.getString("user_id", "").equals("")) {
                    //finish();
                    prefEditor.putString("temp_type_sms", "1");
                    prefEditor.commit();
                    Intent intent = new Intent(DisplayStudentEditPreActivity.this, Activity_Location_Details.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }


            }
        });
        takeChangeCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!preferences.getString("user_id", "").equals("")) {
                    // finish();
                    prefEditor.putString("temp_type_sms", "1");
                    prefEditor.commit();
                    Intent intent = new Intent(DisplayStudentEditPreActivity.this, TabsFragmentActivity.class);
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
                    // finish();
                    prefEditor.putString("temp_type_sms", "1");
                    prefEditor.commit();
                    Intent intent = new Intent(DisplayStudentEditPreActivity.this, Activity_DayPrefrence_Display.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }


            }
        });


        MultipleCommentAddView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                commentEdtTxt.setText(messageText);
                //  new getStudentCommentDetailsList().execute();
            }
        });
        commentEdtTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prefEditor.putString("temp_type_sms", "1");
                prefEditor.commit();
                CommentAddView commentAddView = new CommentAddView();
                commentAddView.show(getSupportFragmentManager(), "commentAddView");


            }
        });
        CommentAddView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                commentEdtTxt.setText(messageText);
                //  new getStudentCommentDetailsList().execute();
            }
        });

        checkConnection();
    }

    public void verifyMobileDeviceId() {


        jsonObj1 = new JSONObject() {
            {
                try {
                    put("user_id", preferences.getString("enquiry_user_id", ""));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();
                verifyMobileDeviceIdResponse = serviceAccess.SendHttpPost(Config.URL_AVAILABLE_MOBILE_DEVICEID, jsonObj1);
                Log.i("loginResponse", "verifyMobileDeviceIdResponse" + verifyMobileDeviceIdResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (verifyMobileDeviceIdResponse.compareTo("") == 0) {

                                } else {

                                    try {
                                        JSONObject jObject = new JSONObject(verifyMobileDeviceIdResponse);
                                        statusv = jObject.getBoolean("status");

                                        if (statusv) {
                                            JSONArray introJsonArray = jObject.getJSONArray("data");
                                            for (int i = 0; i < introJsonArray.length(); i++) {
                                                JSONObject introJsonObject = introJsonArray.getJSONObject(i);
                                                mobileDeviceId = introJsonObject.getString("enquiry_mobile_deviceid");
                                            }
                                            if (preferences.getString("enquiry_mobile_deviceid", "").equals(mobileDeviceId)) {

                                            } else {
                                                prefEditor.putString("enquiry_user_id", "");
                                                prefEditor.commit();
                                                Intent i = new Intent(DisplayStudentEditPreActivity.this, SplashScreenActivity.class);
                                                startActivity(i);
                                            }

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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyApplication.getInstance().setConnectivityListener(this);

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;


        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;

        }

        Snackbar snackbar = Snackbar.make(takeTemplate, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    private class dueFeesAvailable extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            jsonObj = new JSONObject() {
                {
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj1);
            dueFeesSMSResponse = serviceAccess.SendHttpPost(Config.URL_GETSENDINGPENDINGSMSDETAILS, jsonObj);
            Log.i("resp", "dueFeesSMSResponse" + dueFeesSMSResponse);


            if (dueFeesSMSResponse.compareTo("") != 0) {
                if (isJSONValid(dueFeesSMSResponse)) {
                    JSONArray leadJsonObj = null;
                    try {
                        leadJsonObj = new JSONArray(dueFeesSMSResponse);
                        for (int i = 0; i < leadJsonObj.length(); i++) {
                            JSONObject json_data = leadJsonObj.getJSONObject(i);
                            st_name = json_data.getString("first_name");
                            batch_id = json_data.getString("BatchID");
                            course_name = json_data.getString("course_name");
                            next_due_date = json_data.getString("next_due_date");
                            st_mobile = json_data.getString("mobile_no");
                            reminder_count = json_data.getString("reminder_count");
                            if (!json_data.getString("due_amount").equals("null")) {
                                da = json_data.getString("due_amount");
                                if (reminder_count.equals("0")) {
                                    sms = "Hi " + st_name + System.getProperty("line.separator") + System.getProperty("line.separator")
                                            + "Your fees is due for Batch " + batch_id + ", " + course_name + System.getProperty("line.separator")
                                            + "Would appreciate if you can pay your due fees before " + formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", next_due_date) + "." + System.getProperty("line.separator") + System.getProperty("line.separator")
                                            + "Regards" + System.getProperty("line.separator")
                                            + "Raza" + System.getProperty("line.separator")
                                            + "9762118718";
                                } else {
                                    sms = "Reminder " + reminder_count + System.getProperty("line.separator") + System.getProperty("line.separator")
                                            + "Hi " + st_name + System.getProperty("line.separator") + System.getProperty("line.separator")
                                            + "Your fees is due for Batch " + batch_id + ", " + course_name + System.getProperty("line.separator")
                                            + "Would appreciate if you can pay your due fees before " + formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", next_due_date) + "." + System.getProperty("line.separator") + System.getProperty("line.separator")
                                            + "Regards" + System.getProperty("line.separator")
                                            + "Raza" + System.getProperty("line.separator")
                                            + "9762118718";
                                }
                            } else {

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    //  Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                }
            } else {

                //  Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (!sms.equals("")) {
                //  Toast.makeText(getApplicationContext(), sms, Toast.LENGTH_SHORT).show();
                String result = sendSms1(st_mobile, sms);
                sms = "";
                new dueFeesAvailable().execute();
            }

        }
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            //LOGE(TAG, "ParseException - dateFormat");
        }

        return outputDate;

    }

    public static String sendSms1(String tempMobileNumber, String message) {
        String sResult = null;
        try {
// Construct data
            //String phonenumbers = "9657816221";
            String data = "user=" + URLEncoder.encode(sms_user, "UTF-8");
            data += "&password=" + URLEncoder.encode(sms_pass, "UTF-8");
            data += "&message=" + URLEncoder.encode(message, "UTF-8");
            data += "&sender=" + URLEncoder.encode("AFCKST", "UTF-8");
            data += "&mobile=" + URLEncoder.encode(tempMobileNumber, "UTF-8");
            data += "&type=" + URLEncoder.encode("3", "UTF-8");
// Send data
            URL url = new URL("http://login.bulksmsgateway.in/sendmessage.php?" + data);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
// Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String sResult1 = "";
            while ((line = rd.readLine()) != null) {
// Process line...
                sResult1 = sResult1 + line + " ";
            }
            wr.close();
            rd.close();
            return sResult1;
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
            return "Error " + e;
        }
    }

    public String LastCall() {
        if (contactListDAOArrayList.size() > 0) {
            contactListDAOArrayList.clear();
        }
        StringBuffer sb = new StringBuffer();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // return TODO;
        }
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC");

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);


        sb.append("Call Details :");

        int i = 0;
        while (managedCursor.moveToNext()) {
            if (i < Integer.parseInt(preferences.getString("quantity_for_contacts", ""))) {
                String uName = managedCursor.getString(name);
                // String emailAddress = managedCursor.getString(managedCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString(duration);


                int dircode = Integer.parseInt(callType);
                switch (dircode) {

                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }

                sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                        + dir + " \nCall Date:--- " + callDayTime
                        + " \nCall duration in sec :--- " + callDuration
                        + " \nUser Details " + getContactDetails(phNumber));
                sb.append("\n----------------------------------");
                i++;

            }


        }

        managedCursor.close();
        new getContactList().execute();
        return sb.toString();
    }

    public String getContactDetails(String phoneNumber1) {

        String searchNumber = phoneNumber1;
        String searchNumber1 = phoneNumber1.replace(" ", "");
        StringBuffer sb = new StringBuffer();
        // Cursor c =  getContentResolver().query(contactData, null, null, null, null);
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(searchNumber));
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        if (c.moveToNext()) {

            String phoneNumber = "", emailAddress = "";
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            //http://stackoverflow.com/questions/866769/how-to-call-android-contacts-list   our upvoted answer

            String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if (hasPhone.equalsIgnoreCase("1"))
                hasPhone = "true";
            else
                hasPhone = "false";

            if (Boolean.parseBoolean(hasPhone)) {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (phones.moveToNext()) {
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }

            // Find Email Addresses
            Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
            while (emails.moveToNext()) {
                emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            }
            emails.close();


            if (!name.equals("")) {


                email_id = emailAddress;

                Log.d("curs", name + " num" + phoneNumber + " " + "mail" + emailAddress);

                if (dName.equals("") || !dName.equals(phoneNumber)) {
                    contactListDAO = new ContactListDAO();
                    contactListDAO.setName(name);
                    contactListDAO.setMobileNumber(phoneNumber);
                    contactListDAO.setEmailId(emailAddress);
                    contactListDAO.setCallType(dir);
                    contactListDAOArrayList.add(contactListDAO);
                    dName = phoneNumber;
                }


            } else {
                Toast.makeText(getApplicationContext(), "Please save last calling number then try again!", Toast.LENGTH_SHORT).show();
            }


// add elements to al, including duplicates


        }
        c.close();
        return sb.toString();
    }

    //
    private class getContactList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog

        }

        @Override
        protected Void doInBackground(Void... params) {


            if (contactListDAOArrayList.size() > 0) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                    }
                });


            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            if (contactListDAOArrayList.size() > 0) {
                Spinner spinnerCustom = (Spinner) findViewById(R.id.spinnerLastCall);
                ArrayAdapter<ContactListDAO> adapter = new ArrayAdapter<ContactListDAO>(DisplayStudentEditPreActivity.this, android.R.layout.simple_spinner_dropdown_item, contactListDAOArrayList);
                spinnerCustom.setAdapter(adapter);
                spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        ContactListDAO LeadSource = (ContactListDAO) parent.getSelectedItem();
                        String name = LeadSource.getName();
                        String searchNum = LeadSource.getMobileNumber();
                        String searchNumber1 = searchNum.replaceAll("\\s", "");
                        if (!name.equals("")) {
                            String columns[] = name.split(" ");
                            first_name = columns[0];
                            if (columns.length > 1) {
                                String name1 = columns[1];
                                String columns2[] = name1.split(",");
                                last_name = columns2[0];
                            }

                        }

                        if (searchNumber1.length() == 10) {

                            mobile_no = searchNumber1;

                        }
                        if (searchNumber1.length() == 11) {
                            //Toast.makeText(context, "" + current.getMobileNumber().substring(3, 13), Toast.LENGTH_SHORT).show();
                            mobile_no = searchNumber1.substring(1, 11);

                        }
                        if (searchNumber1.length() == 13) {
                            //Toast.makeText(context, "" + current.getMobileNumber().substring(3, 13), Toast.LENGTH_SHORT).show();
                            mobile_no = searchNumber1.substring(3, 13);

                        }
                        refreshflag = 0;
                        status = false;
                        prefEditor.putString("emil_id", LeadSource.getEmailId());
                        prefEditor.putString("student_name", first_name);
                        prefEditor.putString("student_last_name", last_name);
                        prefEditor.putString("st_first_name", first_name);
                        prefEditor.putString("st_last_name", last_name);
                        prefEditor.putString("st_mobile_no", mobile_no);
                        prefEditor.commit();
                        new availableStudent().execute();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }


                });

            } else {

            }
        }
    }

    private class availableStudent extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {


            Cursor c = db.getUserId(mobile_no);
            assert c != null;
            if (c.moveToFirst()) {
                do {
                    status = true;
                    user_id = c.getString(c.getColumnIndex(StudentContract.Entry.COLUMN_NAME_ENTRY_ID));
                    prefEditor.putString("user_id", user_id);
                    prefEditor.putString("student_mob_sms", mobile_no);
                    prefEditor.commit();

                } while (c.moveToNext());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {

                prefEditor.putString("flag_edit_pre", "2");
                prefEditor.putString("temp_type_sms", "1");
                prefEditor.commit();
                mleadList.setVisibility(View.VISIBLE);
                cmleadList.setVisibility(View.VISIBLE);
                getUserContinueDiscontinueCount();
                new getLocationList().execute();

            } else {

                autoCompleteTextViewStudent.setText("");
                userbatches.setText("");
                commentEdtTxt.setText("");
                commentEdtTxt.setVisibility(View.GONE);
                clear.setVisibility(View.GONE);
                user_id = "";
                prefEditor.remove("user_id");
                prefEditor.remove("st_first_name");
                prefEditor.remove("st_last_name");
                prefEditor.remove("student_mob_sms");
                prefEditor.remove("student_mail_id");
                prefEditor.commit();
                if (data.size() > 0) {
                    data.clear(); // this list which you hava passed in Adapter for your listview
                    centerListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                }
                if (cdata.size() > 0) {
                    cdata.clear(); // this list which you hava passed in Adapter for your listview
                    coursesListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                }
                if (pre_batch_data.size() > 0) {
                    pre_batch_data.clear();
                    preBatchDetailsListAdpter.notifyDataSetChanged();
                }
                RegistrationView studentFeesEntryView = new RegistrationView();
                studentFeesEntryView.show(getSupportFragmentManager(), "studentFeesEntryView");

            }


        }

    }

    public void getchannelPartnerSelect(final String channelPartnerSelect) {


        Thread objectThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                Cursor c = db.getSerachUser(channelPartnerSelect);
                assert c != null;
                studentArrayList.clear();
                if (c.moveToFirst()) {
                    do {
                        status = true;
                        studentArrayList.add(new StudentListDAO(c.getString(c.getColumnIndex("Details")), c.getString(c.getColumnIndex("id")), c.getString(c.getColumnIndex("first_name")), c.getString(c.getColumnIndex("last_name")), c.getString(c.getColumnIndex("mobile_no")), c.getString(c.getColumnIndex("email_id")), c.getString(c.getColumnIndex("status"))));
                    } while (c.moveToNext());
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        aAdapter = new ArrayAdapter<StudentListDAO>(getApplicationContext(), R.layout.item, studentArrayList);
                        autoCompleteTextViewStudent.setAdapter(aAdapter);

                        autoCompleteTextViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                                // String s = parent.getItemAtPosition(i).toString();
                                StudentListDAO student = (StudentListDAO) parent.getAdapter().getItem(i);
                                refreshflag = 1;
                                user_id = student.getId();
                                Log.d("Id---->", user_id + "" + student.getId());
                                prefEditor.putString("user_id", user_id);
                                prefEditor.putString("st_first_name", student.getFirst_Name());
                                prefEditor.putString("st_last_name", student.getLast_Name());
                                prefEditor.putString("student_mob_sms", student.getMobile_No());
                                prefEditor.putString("student_mail_id", student.getStudent_mail());
                                prefEditor.commit();
                                mleadList.setVisibility(View.VISIBLE);
                                cmleadList.setVisibility(View.VISIBLE);
                                getUserContinueDiscontinueCount();
                                new getLocationList().execute();
                                //
                                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


                            }
                        });
                        aAdapter.notifyDataSetChanged();

                    }
                });


            }
        });

        objectThread.start();

    }

    private class getLocationList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            data.clear();
            Cursor cursor = db.getLocNames(user_id);
            assert cursor != null;
            //Log.d("total count",""+cursor.getCount());

            if (cursor.moveToFirst()) {
                do {

                    data.add(new StCenterDAO(cursor.getString(cursor.getColumnIndex(StudentContract.StudentLocation.COLUMN_NAME_ENTRY_ID)),
                            cursor.getString(cursor.getColumnIndex(StudentContract.StudentLocation.COLUMN_NAME_BRANCH_NAME))));
                } while (cursor.moveToNext());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (data.size() > 0) {

                centerListAdpter = new StCenterListAdpter(DisplayStudentEditPreActivity.this, data);
                mleadList.setAdapter(centerListAdpter);
                mleadList.setLayoutManager(new LinearLayoutManager(DisplayStudentEditPreActivity.this));
                mleadList.setHasFixedSize(true);
                setUpItemTouchHelper();
                setUpAnimationDecoratorHelper();
                // Close the progressdialog

                new getCoursesList().execute();
            } else {
                //Toast.makeText(getApplicationContext(), "No Location were found", Toast.LENGTH_LONG).show();


            }
        }
    }

    private class getCoursesList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            cdata.clear();
            Cursor cursor = db.getCourseNames(user_id);
            assert cursor != null;
            //Log.d("total count",""+cursor.getCount());

            if (cursor.moveToFirst()) {
                int i = 1;
                do {

                    String sequence = String.format("%03d", i);
                    cdata.add(new StCoursesDAO(cursor.getString(cursor.getColumnIndex(StudentContract.StudentCourse.COLUMN_NAME_ENTRY_ID)),
                            cursor.getString(cursor.getColumnIndex(StudentContract.StudentCourse.COLUMN_NAME_TYPE_NAME)), cursor.getString(cursor.getColumnIndex(StudentContract.StudentCourse.COLUMN_NAME_COURSE_NAME)), cursor.getString(cursor.getColumnIndex(StudentContract.StudentCourse.COLUMN_NAME_COURSE_CODE)), cursor.getString(cursor.getColumnIndex(StudentContract.StudentCourse.COLUMN_NAME_USER_ID)), cursor.getString(cursor.getColumnIndex(StudentContract.StudentCourse.COLUMN_NAME_TYPE_ID)), sequence));
                    i++;
                } while (cursor.moveToNext());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (cdata.size() > 0) {

                coursesListAdpter = new StCoursesListAdpter(DisplayStudentEditPreActivity.this, cdata);
                cmleadList.setAdapter(coursesListAdpter);
                cmleadList.setLayoutManager(new LinearLayoutManager(DisplayStudentEditPreActivity.this));
                cmleadList.setHasFixedSize(true);
                setUpItemTouchHelperCourse();
                setUpAnimationDecoratorHelperCourse();
                // Close the progressdialog

                new getStudentCommentDetailsList().execute();
            } else {


            }
        }
    }

    public void getUserContinueDiscontinueCount() {

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub

                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (!user_id.equals("")) {
                                    String count = db.getConDisCountUsers(Integer.parseInt(user_id));
                                    userbatches.setText("History" + count);
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

    private class getStudentCommentDetailsList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog

        }

        @Override
        protected Void doInBackground(Void... params) {


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    usernotes = db.getUserNotes(user_id);

                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            if (usernotes == null) {
                commentEdtTxt.setVisibility(View.GONE);

            } else if (usernotes.equals("null")) {
                commentEdtTxt.setVisibility(View.GONE);

            } else if (usernotes.equals("")) {
                commentEdtTxt.setVisibility(View.GONE);

            } else {

                commentEdtTxt.setVisibility(View.VISIBLE);
                commentEdtTxt.setText(usernotes);

            }


        }
    }

    private class initBatchDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    pre_batch_data.clear();
                    Cursor cursor = db.getPreBatchStudentDetails(Integer.parseInt(user_id));
                    assert cursor != null;
                    //Log.d("total count",""+cursor.getCount());

                    if (cursor.moveToFirst()) {
                        do {
                            pre_batch_data.add(new StudentsInBatchListDAO(cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_BATCH_CODE)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_STUDENTS_NAME)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_FIRST_NAME)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_MOBILE_NO)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_EMAIL_ID)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_GENDER)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_COURSE_NAME)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_START_DATE)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_BASEFEES)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_STATUS)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_PREVIOUS_ATTENDANCE)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_DISCONTINUE_REASON)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_COURSE_CODE)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_FEES)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_DUE_AMOUNT)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_ENTRY_ID)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_LAST_NAME)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_SBD_ID)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_STUDENT_BATCH_CAT)),
                                    cursor.getString(cursor.getColumnIndex(StudentContract.StudentBatchdetails.COLUMN_NAME_NOTES_ID))));

                        } while (cursor.moveToNext());
                    }


                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (pre_batch_data.size() > 0) {
                preBatchDetailsListAdpter = new PreBatchDetailsListAdpter(DisplayStudentEditPreActivity.this, pre_batch_data);
                preBatchStudentsDetailsList.setAdapter(preBatchDetailsListAdpter);
                preBatchStudentsDetailsList.setLayoutManager(new LinearLayoutManager(DisplayStudentEditPreActivity.this));


            } else {
                Toast.makeText(getApplicationContext(), "No Data were found", Toast.LENGTH_LONG).show();

            }
        }
    }

    private class deleteUser extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("user_id", preferences.getString("user_id", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            userDeleteResponse = serviceAccess.SendHttpPost(Config.URL_STUDENT_DELETE, jsonLeadObj);
            Log.i("resp", "userDeleteResponse" + userDeleteResponse);
            if (userDeleteResponse.compareTo("") != 0) {
                if (isJSONValid(userDeleteResponse)) {

                    try {

                        JSONObject jObject = new JSONObject(userDeleteResponse);
                        status = jObject.getBoolean("status");
                        if (status) {
                            db.deleteUser(preferences.getString("user_id", ""));
                        }
                        message = jObject.getString("message");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();

                }
            } else {

                Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {

                // removeAt(ID);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                // Close the progressdialog
                autoCompleteTextViewStudent.setText("");

            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();


            }

        }
    }

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.parseColor("#E6E6DC"));
                xMark = ContextCompat.getDrawable(DisplayStudentEditPreActivity.this, R.drawable.ic_delete_black_24dp);
                xMark.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) DisplayStudentEditPreActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                StCenterListAdpter testAdapter = (StCenterListAdpter) recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                StCenterListAdpter adapter = (StCenterListAdpter) mleadList.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mleadList);
    }

    private void setUpAnimationDecoratorHelper() {
        mleadList.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

    private void setUpItemTouchHelperCourse() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.parseColor("#E6E6DC"));
                xMark = ContextCompat.getDrawable(DisplayStudentEditPreActivity.this, R.drawable.ic_delete_black_24dp);
                xMark.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) DisplayStudentEditPreActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                StCoursesListAdpter testAdapter = (StCoursesListAdpter) recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                StCoursesListAdpter adapter = (StCoursesListAdpter) cmleadList.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(cmleadList);
    }

    private void setUpAnimationDecoratorHelperCourse() {
        cmleadList.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
