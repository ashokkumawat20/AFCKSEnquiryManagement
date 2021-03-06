package in.afckstechnologies.afcksenquirymanagement.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.adapter.BatchesDetailsListAdpter;
import in.afckstechnologies.afcksenquirymanagement.models.BatchesForStudentsDAO;
import in.afckstechnologies.afcksenquirymanagement.models.StCoursesDAO;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentContract;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;
import in.afckstechnologies.afcksenquirymanagement.utils.Config;
import in.afckstechnologies.afcksenquirymanagement.utils.JsonHelper;
import in.afckstechnologies.afcksenquirymanagement.utils.WebClient;

public class ComingBatchDetailsView extends DialogFragment {


    Context context;
    SharedPreferences preferences;
    Editor prefEditor;
    String studentfeesListResponse = "";
    JSONObject jsonObj;
    Boolean status;
    int count = 0;
    View registerView;
    private JSONObject jsonLeadObj;
    ProgressDialog mProgressDialog;
    JSONArray jsonArray;
    BatchesDetailsListAdpter batchesDetailsListAdpter;
    private RecyclerView mstudentList;
    List<BatchesForStudentsDAO> data;
    ImageView closeFeesDetails;

    String course_id = "";
    //database helper object
    private StudentProvider.AFCKSDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        registerView = inflater.inflate(R.layout.dialog_batches_details, null);

        context = getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();

        params.y = 50;
        window.setAttributes(params);
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();
        db = new StudentProvider.AFCKSDatabase(getActivity());
        course_id = preferences.getString("course_id_for_batch", "");
        mstudentList = (RecyclerView) registerView.findViewById(R.id.feesdetailsList);
        closeFeesDetails = (ImageView) registerView.findViewById(R.id.closeFeesDetails);
        new getBatchDetailsList().execute();

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        data = new ArrayList<>();
        closeFeesDetails.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        getDialog().setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //Hide your keyboard here!!!
                    //Toast.makeText(getActivity(), "PLease enter your information to get us connected with you.", Toast.LENGTH_LONG).show();
                    return true; // pretend we've processed it
                } else
                    return false; // pass on to be processed as normal
            }
        });
        return registerView;
    }


    //
    private class getBatchDetailsList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
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

            getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            data.clear();
                                Cursor cursor = db.getComingBatches(course_id);
                                assert cursor != null;
                                //Log.d("total count",""+cursor.getCount());
                                 if (cursor.moveToFirst()) {

                                    do {


                                        data.add(new BatchesForStudentsDAO(cursor.getString(cursor.getColumnIndex(StudentContract.ComingBatchdetails.COLUMN_NAME_ENTRY_ID)),
                                                cursor.getString(cursor.getColumnIndex(StudentContract.ComingBatchdetails.COLUMN_NAME_COURSE_ID)),
                                                cursor.getString(cursor.getColumnIndex(StudentContract.ComingBatchdetails.COLUMN_NAME_NEW_START_DATE)),
                                                cursor.getString(cursor.getColumnIndex(StudentContract.ComingBatchdetails.COLUMN_NAME_TIMINGS)),
                                                cursor.getString(cursor.getColumnIndex(StudentContract.ComingBatchdetails.COLUMN_NAME_NOTES)),
                                                cursor.getString(cursor.getColumnIndex(StudentContract.ComingBatchdetails.COLUMN_NAME_FREQUENCY)),
                                                cursor.getString(cursor.getColumnIndex(StudentContract.ComingBatchdetails.COLUMN_NAME_FEES)),
                                                cursor.getString(cursor.getColumnIndex(StudentContract.ComingBatchdetails.COLUMN_NAME_DURATION)),
                                                cursor.getString(cursor.getColumnIndex(StudentContract.ComingBatchdetails.COLUMN_NAME_COURSE_NAME)),
                                                cursor.getString(cursor.getColumnIndex(StudentContract.ComingBatchdetails.COLUMN_NAME_BRANCH_NAME)),
                                                cursor.getString(cursor.getColumnIndex(StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_TYPE)),
                                                cursor.getString(cursor.getColumnIndex(StudentContract.ComingBatchdetails.COLUMN_NAME_FACULTY_NAME))));

                                    } while (cursor.moveToNext());
                                }



                        }
                    });




            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (data.size() > 0) {
                batchesDetailsListAdpter = new BatchesDetailsListAdpter(getActivity(), data);
                mstudentList.setAdapter(batchesDetailsListAdpter);
                mstudentList.setLayoutManager(new LinearLayoutManager(getActivity()));
                batchesDetailsListAdpter.notifyDataSetChanged();
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                Toast.makeText(getActivity(), "Batch not found !!", Toast.LENGTH_LONG).show();
                dismiss();
                mProgressDialog.dismiss();
            }
        }
    }

    private void update() {
       // Intent intent = new Intent(context, StudentList.class);
       // startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        //Toast.makeText(getActivity(), "Your information is valuable for us and won't be misused.", Toast.LENGTH_SHORT).show();
                        count++;
                        if (count >= 1) {
                            // update();
                            dismiss();
                        }
                        return true;
                    } else {
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
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
}