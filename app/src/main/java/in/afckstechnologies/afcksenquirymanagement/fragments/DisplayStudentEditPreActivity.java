package in.afckstechnologies.afcksenquirymanagement.fragments;


import android.Manifest;
import android.accounts.Account;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import in.afckstechnologies.afcksenquirymanagement.activity.Activity_User_Profile;
import in.afckstechnologies.afcksenquirymanagement.activity.SplashActivity;
import in.afckstechnologies.afcksenquirymanagement.activity.SplashScreenActivity;
import in.afckstechnologies.afcksenquirymanagement.activity.TemplateDisplayActivity;
import in.afckstechnologies.afcksenquirymanagement.adapter.PreBatchDetailsListAdpter;
import in.afckstechnologies.afcksenquirymanagement.adapter.StCenterListAdpter;
import in.afckstechnologies.afcksenquirymanagement.adapter.StCoursesListAdpter;
import in.afckstechnologies.afcksenquirymanagement.common.accounts.GenericAccountService;
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
import in.afckstechnologies.afcksenquirymanagement.utils.FeesListener;
import in.afckstechnologies.afcksenquirymanagement.utils.SmsListener;
import in.afckstechnologies.afcksenquirymanagement.utils.SyncUtils;
import in.afckstechnologies.afcksenquirymanagement.utils.Utils;
import in.afckstechnologies.afcksenquirymanagement.utils.WebClient;
import in.afckstechnologies.afcksenquirymanagement.view.CommentAddView;
import in.afckstechnologies.afcksenquirymanagement.view.RegistrationView;
import in.afckstechnologies.afcksenquirymanagement.view.ShowingDayYearView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayStudentEditPreActivity extends Fragment {

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    static String sms_user = "";
    static String sms_pass = "";
    JSONObject jsonCenterObj, jsonobject, jsonLeadObj, jsonLeadObj1, jsonObj1, jsonObj;
    ImageView clear, whatsapp, calling, dummy, settingimg, active, inActive;
    //verify and duefees
    String verifyMobileDeviceIdResponse = "";
    boolean statusv;
    String mobileDeviceId = "";
    String st_name = "", course_name = "", next_due_date = "", st_mobile = "", da = "", dueFeesSMSResponse = "", userstatus = "";
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
    int tabclick = 1;
    View v;
    Spinner spinnerCustom;
    TextView clickTxt;

    /**
     * Handle to a SyncObserver. The ProgressBar element is visible until the SyncObserver reports
     * that the sync is complete.
     *
     * <p>This allows us to delete our SyncObserver once the application is no longer in the
     * foreground.
     */
    private Object mSyncObserverHandle;
    //
    private Paint p = new Paint();

    public DisplayStudentEditPreActivity() {
        // Required empty public constructor
    }

    /**
     * Options menu used to populate ActionBar.
     */
    private Menu mOptionsMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.activity_display_student_edit_pre, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //  getActivity().registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        db = new StudentProvider.AFCKSDatabase(getActivity());
        preferences = getActivity().getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        sms_user = preferences.getString("sms_username", "");
        sms_pass = preferences.getString("sms_password", "");
        dummy = (ImageView) v.findViewById(R.id.dummy);
        clear = (ImageView) v.findViewById(R.id.clear);
        active = (ImageView) v.findViewById(R.id.active);
        inActive = (ImageView) v.findViewById(R.id.inActive);
        whatsapp = (ImageView) v.findViewById(R.id.whatsapp);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeToRefresh);
        calling = (ImageView) v.findViewById(R.id.calling);
        userPreferences = (Button) v.findViewById(R.id.userPreferences);
        userbatches = (Button) v.findViewById(R.id.userbatches);
        settingimg = (ImageView) v.findViewById(R.id.settingimg);
        mleadList = (RecyclerView) v.findViewById(R.id.centerList);
        cmleadList = (RecyclerView) v.findViewById(R.id.coursesList);
        commentEdtTxt = (TextView) v.findViewById(R.id.commentEdtTxt);
        spinnerCustom = (Spinner) v.findViewById(R.id.spinnerLastCall);
        clickTxt = (TextView) v.findViewById(R.id.clickTxt);
        preBatchStudentsDetailsList = (RecyclerView) v.findViewById(R.id.preBatchStudentsDetailsList);
        studentArrayList = new ArrayList<StudentListDAO>();
        pre_batch_data = new ArrayList<>();
        data = new ArrayList<>();
        cdata = new ArrayList<>();
        if (preferences.getString("quantity_for_contacts", "").equals("")) {
            prefEditor.putString("quantity_for_contacts", "5");
            prefEditor.commit();
        }
        if (refreshflag == 0) {
            LastCall();
        }
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            verifyMobileDeviceId();
        }
        //auto search
        autoCompleteTextViewStudent = (AutoCompleteTextView) v.findViewById(R.id.SearchStudent);
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

        RegistrationView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                //Toast.makeText(getApplicationContext(), messageText, Toast.LENGTH_LONG).show();
                autoCompleteTextViewStudent.setText(messageText);
                mobile_no = messageText;
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
                availableStudent();

            }
        });

       /* Activity_User_Profile.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                prefEditor.putString("delete_flag", "");
                prefEditor.commit();
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
                LastCall();

            }
        });*/

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
                prefEditor.remove("userstatus");
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
                LastCall();
            }
        });
        userbatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabclick = 2;
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
                if (pre_batch_data.size() > 0) {
                    pre_batch_data.clear(); // this list which you hava passed in Adapter for your listview
                    preBatchDetailsListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                }

                if (!user_id.equals("")) {
                    mleadList.setVisibility(View.GONE);
                    cmleadList.setVisibility(View.GONE);
                    preBatchStudentsDetailsList.setVisibility(View.VISIBLE);
                    initBatchDetails();
                }

            }
        });
        userPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabclick = 1;
                userPreferences.setBackgroundColor(getResources().getColor(R.color.color_sbutton));
                userbatches.setBackgroundColor(getResources().getColor(R.color.color_ubutton));
                preBatchStudentsDetailsList.setVisibility(View.GONE);
                mleadList.setVisibility(View.VISIBLE);
                cmleadList.setVisibility(View.VISIBLE);
                getUserContinueDiscontinueCount();
                getLocationList();


            }
        });

        calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user_id.equals("")) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);

                    callIntent.setData(Uri.parse("tel:" + preferences.getString("student_mob_sms", "")));
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                    PackageManager packageManager = getActivity().getPackageManager();
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

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    verifyMobileDeviceId();
                    getActivity().registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                    // new dueFeesAvailable().execute();

                } else {
                }

                if (tabclick == 1) {

                    if (!user_id.equals("")) {
                        preBatchStudentsDetailsList.setVisibility(View.GONE);
                        mleadList.setVisibility(View.VISIBLE);
                        cmleadList.setVisibility(View.VISIBLE);
                        getUserContinueDiscontinueCount();
                        getLocationList();
                    } else {


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
                        studentFeesEntryView.show(getActivity().getSupportFragmentManager(), "studentFeesEntryView");

                    }

                } else {
                    if (!user_id.equals("")) {
                        mleadList.setVisibility(View.GONE);
                        cmleadList.setVisibility(View.GONE);
                        preBatchStudentsDetailsList.setVisibility(View.VISIBLE);
                        getUserContinueDiscontinueCount();
                        initBatchDetails();
                    } else {

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
                        studentFeesEntryView.show(getActivity().getSupportFragmentManager(), "studentFeesEntryView");

                    }

                }

                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
        settingimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(getActivity(), settingimg);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_home, popup.getMenu());
                popup.getMenu().add("Call Limit(" + preferences.getString("quantity_for_contacts", "") + ")");


                popup.show();
                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        // Toast.makeText(DisplayStudentEditPreActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals("Templates")) {
                            prefEditor.putString("temp_type_sms", "2");
                            prefEditor.commit();
                            Intent intent = new Intent(getActivity(), TemplateDisplayActivity.class);
                            startActivity(intent);


                        } else if (item.getTitle().equals("Sync")) {
                            if (AppStatus.getInstance(getActivity()).isOnline()) {
                                SyncUtils.TriggerRefresh();
                            }
                        } else if (item.getTitle().equals("Call Limit(" + preferences.getString("quantity_for_contacts", "") + ")")) {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                            alertDialog.setMessage("Enter Quantity for display contacts");

                            final EditText input = new EditText(getActivity());
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
                        } else if (item.getTitle().equals("Login")) {
                            if (AppStatus.getInstance(getActivity()).isOnline()) {
                                ShowingDayYearView showingDayYearView = new ShowingDayYearView();
                                showingDayYearView.show(getActivity().getSupportFragmentManager(), "showingDayYearView");
                            } else {
                                Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                            }
                        }
                        popup.dismiss();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        commentEdtTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prefEditor.putString("temp_type_sms", "1");
                prefEditor.commit();
                CommentAddView commentAddView = new CommentAddView();
                commentAddView.show(getActivity().getSupportFragmentManager(), "commentAddView");


            }
        });
        CommentAddView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                commentEdtTxt.setText(messageText);
                //  new getStudentCommentDetailsList().execute();
            }
        });

        clickTxt.setOnClickListener(new View.OnClickListener() {
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
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to unsubscribe " + preferences.getString("st_first_name", "") + " " + preferences.getString("st_last_name", "") + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id1) {

                                active.setVisibility(View.GONE);
                                inActive.setVisibility(View.VISIBLE);
                                prefEditor.putString("userstatus", "0");
                                prefEditor.commit();
                                db.updateUserActiveStatus(preferences.getString("user_id", ""), 0);
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
                alert.setTitle("User Unsubscribe");
                alert.show();
            }
        });
        inActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to subscribe " + preferences.getString("st_first_name", "") + " " + preferences.getString("st_last_name", "") + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id1) {
                                inActive.setVisibility(View.GONE);
                                active.setVisibility(View.VISIBLE);
                                prefEditor.putString("userstatus", "1");
                                prefEditor.commit();
                                db.updateUserActiveStatus(preferences.getString("user_id", ""), 1);
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
                alert.setTitle("User Subscribe");
                alert.show();
            }
        });
        if (preferences.getString("userstatus", "").equals("0")) {
            active.setVisibility(View.GONE);
            inActive.setVisibility(View.VISIBLE);
        } else {
            inActive.setVisibility(View.GONE);
            active.setVisibility(View.VISIBLE);

        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Here we call the data setup methods again, to reflect
        // the changes which took place then the Fragment was paused


        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);

        if (tabclick == 1) {

            if (!user_id.equals("")) {
                preBatchStudentsDetailsList.setVisibility(View.GONE);
                mleadList.setVisibility(View.VISIBLE);
                cmleadList.setVisibility(View.VISIBLE);
                getUserContinueDiscontinueCount();
                getLocationList();
            }

        } else {
            if (!user_id.equals("")) {
                mleadList.setVisibility(View.GONE);
                cmleadList.setVisibility(View.GONE);
                preBatchStudentsDetailsList.setVisibility(View.VISIBLE);
                getUserContinueDiscontinueCount();
                initBatchDetails();
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

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        aAdapter = new ArrayAdapter<StudentListDAO>(getActivity(), R.layout.item, studentArrayList);
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
                                prefEditor.putString("userstatus", student.getStatus());
                                prefEditor.commit();
                                mleadList.setVisibility(View.VISIBLE);
                                cmleadList.setVisibility(View.VISIBLE);

                                if (preferences.getString("userstatus", "").equals("0")) {
                                    active.setVisibility(View.GONE);
                                    inActive.setVisibility(View.VISIBLE);
                                } else {
                                    inActive.setVisibility(View.GONE);
                                    active.setVisibility(View.VISIBLE);

                                }
                                getUserContinueDiscontinueCount();
                                getLocationList();
                                //
                                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
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

    public String LastCall() {
        if (contactListDAOArrayList.size() > 0) {
            contactListDAOArrayList.clear();
        }
        StringBuffer sb = new StringBuffer();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // return TODO;
        }
        Cursor managedCursor = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC");

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
        getContactList();
        return sb.toString();
    }

    public String getContactDetails(String phoneNumber1) {

        String searchNumber = phoneNumber1;
        String searchNumber1 = phoneNumber1.replace(" ", "");
        StringBuffer sb = new StringBuffer();
        // Cursor c =  getContentResolver().query(contactData, null, null, null, null);
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(searchNumber));
        Cursor c = getActivity().getContentResolver().query(uri, null, null, null, null);
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
                Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (phones.moveToNext()) {
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }

            // Find Email Addresses
            Cursor emails = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
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
                Toast.makeText(getActivity(), "Please save last calling number then try again!", Toast.LENGTH_SHORT).show();
            }


// add elements to al, including duplicates


        }
        c.close();
        return sb.toString();
    }

    //
    public void getContactList() {


        if (contactListDAOArrayList.size() > 0) {

            ArrayAdapter<ContactListDAO> adapter = new ArrayAdapter<ContactListDAO>(getActivity(), R.layout.item_array, contactListDAOArrayList);
            spinnerCustom.setAdapter(adapter);
            spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
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
                    availableStudent();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }


            });

        } else {

        }

    }

    public void availableStudent() {
        Cursor c = db.getUserId(mobile_no);
        assert c != null;
        if (c.moveToFirst()) {
            do {
                status = true;
                user_id = c.getString(c.getColumnIndex(StudentContract.Entry.COLUMN_NAME_ENTRY_ID));
                userstatus = c.getString(c.getColumnIndex(StudentContract.Entry.COLUMN_NAME_STATUS));
                prefEditor.putString("user_id", user_id);
                prefEditor.putString("student_mob_sms", mobile_no);
                prefEditor.commit();

            } while (c.moveToNext());
        }
        if (userstatus.equals("0")) {
            active.setVisibility(View.GONE);
            inActive.setVisibility(View.VISIBLE);
        } else {
            inActive.setVisibility(View.GONE);
            active.setVisibility(View.VISIBLE);

        }

        if (status) {

            prefEditor.putString("flag_edit_pre", "2");
            prefEditor.putString("temp_type_sms", "1");
            prefEditor.commit();
            if (tabclick == 1) {

                if (!user_id.equals("")) {
                    preBatchStudentsDetailsList.setVisibility(View.GONE);
                    mleadList.setVisibility(View.VISIBLE);
                    cmleadList.setVisibility(View.VISIBLE);
                    getUserContinueDiscontinueCount();
                    getLocationList();
                }

            } else {
                if (!user_id.equals("")) {
                    mleadList.setVisibility(View.GONE);
                    cmleadList.setVisibility(View.GONE);
                    preBatchStudentsDetailsList.setVisibility(View.VISIBLE);
                    getUserContinueDiscontinueCount();
                    initBatchDetails();
                }

            }

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
            studentFeesEntryView.show(getActivity().getSupportFragmentManager(), "studentFeesEntryView");

        }


    }

    public void getLocationList() {


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

        if (data.size() > 0) {

            centerListAdpter = new StCenterListAdpter(getActivity(), data);
            mleadList.setAdapter(centerListAdpter);
            //  mleadList.setLayoutManager(new LinearLayoutManager(getActivity()));
            // mleadList.setHasFixedSize(true);
            mleadList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            setUpItemTouchHelper();
            setUpAnimationDecoratorHelper();
            // Close the progressdialog

            getCoursesList();
        } else {
            mleadList.setVisibility(View.GONE);
            cmleadList.setVisibility(View.GONE);
        }
    }

    public void getCoursesList() {

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


        if (cdata.size() > 0) {

            coursesListAdpter = new StCoursesListAdpter(getActivity(), cdata);
            cmleadList.setAdapter(coursesListAdpter);
            cmleadList.setLayoutManager(new LinearLayoutManager(getActivity()));
            cmleadList.setHasFixedSize(true);
            setUpItemTouchHelperCourse();
            setUpAnimationDecoratorHelperCourse();
            // Close the progressdialog

            new getStudentCommentDetailsList().execute();
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


            getActivity().runOnUiThread(new Runnable() {

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

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.parseColor("#E6E6DC"));
                xMark = ContextCompat.getDrawable(getActivity(), R.drawable.ic_delete_black_24dp);
                xMark.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.ic_clear_margin);
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

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.parseColor("#E6E6DC"));
                xMark = ContextCompat.getDrawable(getActivity(), R.drawable.ic_delete_black_24dp);
                xMark.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.ic_clear_margin);
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

    public void initBatchDetails() {
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


        if (pre_batch_data.size() > 0) {
            preBatchDetailsListAdpter = new PreBatchDetailsListAdpter(getActivity(), pre_batch_data);
            preBatchStudentsDetailsList.setAdapter(preBatchDetailsListAdpter);
            preBatchStudentsDetailsList.setLayoutManager(new LinearLayoutManager(getActivity()));


        }
    }

    public void verifyMobileDeviceId() {


        jsonObj1 = new JSONObject() {
            {
                try {
                    put("pDeviceID", preferences.getString("enquiry_mobile_deviceid", ""));
                    put("role_id", "1");

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
                                        } else {
                                            getActivity().fileList();
                                            prefEditor.putString("enquiry_user_id", "");
                                            prefEditor.commit();
                                            Intent i = new Intent(getActivity(), SplashScreenActivity.class);
                                            startActivity(i);
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
//syncing


    @Override
    public void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    /**
     * Create the ActionBar.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mOptionsMenu = menu;
        mOptionsMenu.add("Call Limit(" + preferences.getString("quantity_for_contacts", "") + ")");
        inflater.inflate(R.menu.main, menu);
    }

    /**
     * Respond to user gestures on the ActionBar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Templates")) {
            prefEditor.putString("temp_type_sms", "2");
            prefEditor.commit();
            Intent intent = new Intent(getActivity(), TemplateDisplayActivity.class);
            startActivity(intent);


        } else if (item.getTitle().equals("Call Limit(" + preferences.getString("quantity_for_contacts", "") + ")")) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setMessage("Enter Quantity for display contacts");

            final EditText input = new EditText(getActivity());
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
        } else if (item.getTitle().equals("Login")) {
            if (AppStatus.getInstance(getActivity()).isOnline()) {
                ShowingDayYearView showingDayYearView = new ShowingDayYearView();
                showingDayYearView.show(getActivity().getSupportFragmentManager(), "showingDayYearView");
            } else {
                Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
            }
        }
        switch (item.getItemId()) {
            // If the user clicks the "Refresh" button.
            case R.id.menu_refresh:
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    Utils.showLoadingDialog(getActivity());
                   //  SyncUtils.TriggerRefresh();
                    // Create account, if needed
                    /****** Create Thread that will sleep for 2 seconds****/
                    Thread background = new Thread() {
                        public void run() {
                            try {
                                // Thread will sleep for 2 seconds
                                sleep(60*1000);
                                Utils.dismissLoadingDialog();
                                // After 5 seconds redirect to another intent

                            } catch (Exception e) {
                            }
                        }
                    };
                    // start thread
                    background.start();
                    SyncUtils.CreateSyncAccount(getActivity());
                    // active.setVisibility(View.GONE);
                    // inActive.setVisibility(View.VISIBLE);

                } else {

                    Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set the state of the Refresh button. If a sync is active, turn on the ProgressBar widget.
     * Otherwise, turn it off.
     *
     * @param refreshing True if an active sync is occuring, false otherwise
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setRefreshActionButtonState(boolean refreshing) {
        if (mOptionsMenu == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return;
        }

        final MenuItem refreshItem = mOptionsMenu.findItem(R.id.menu_refresh);
        if (refreshItem != null) {
            if (refreshing) {
                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);

            } else {
                refreshItem.setActionView(null);
               // Utils.dismissLoadingDialog();
                //inActive.setVisibility(View.GONE);
                // active.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Crfate a new anonymous SyncStatusObserver. It's attached to the app's ContentResolver in
     * onResume(), and removed in onPause(). If status changes, it sets the state of the Refresh
     * button. If a sync is active or pending, the Refresh button is replaced by an indeterminate
     * ProgressBar; otherwise, the button itself is displayed.
     */
    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            getActivity().runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = GenericAccountService.GetAccount(SyncUtils.ACCOUNT_TYPE);
                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
                        setRefreshActionButtonState(false);
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, StudentContract.CONTENT_AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, StudentContract.CONTENT_AUTHORITY);
                    setRefreshActionButtonState(syncActive || syncPending);
                }
            });
        }
    };


}
