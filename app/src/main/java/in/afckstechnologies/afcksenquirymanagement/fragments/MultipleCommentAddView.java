package in.afckstechnologies.afcksenquirymanagement.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.activity.Activity_User_Profile;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;
import in.afckstechnologies.afcksenquirymanagement.utils.AppStatus;
import in.afckstechnologies.afcksenquirymanagement.utils.SmsListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class MultipleCommentAddView extends Fragment {

    Button saveBtn, editBtn;
    private EditText commentEdtTxt;


    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String loginResponse = "";

    String message="";
    int user_id;
    boolean click = true;
    String studentcommentListResponse = "";
    JSONObject jsonObj;
    Boolean status;
    int count = 0;
    View registerView;
    private JSONObject jsonLeadObj;
    ProgressDialog mProgressDialog;
    JSONArray jsonArray;
    public static SmsListener mListener;
    String cooments = "";
    //database helper object
    private StudentProvider.AFCKSDatabase db;
    TextView stName;

    public MultipleCommentAddView() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dialog_comment_view_add_, container, false);
        commentEdtTxt = (EditText) v.findViewById(R.id.commentEdtTxt);
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();
        stName = (TextView) v.findViewById(R.id.stName);
        stName.setText(preferences.getString("user_id", "")+"-"+preferences.getString("st_first_name", "").toUpperCase() + " " + preferences.getString("st_last_name", "").toUpperCase());

        db = new StudentProvider.AFCKSDatabase(getActivity());
        saveBtn = (Button) v.findViewById(R.id.saveBtn);
        editBtn = (Button) v.findViewById(R.id.editBtn);
        getStudentCommentDetailsList();
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                cooments = commentEdtTxt.getText().toString();
                db.updateUserNotes(preferences.getString("user_id", ""), cooments, 2);
             //   mListener.messageReceived(cooments);
                saveBtn.setVisibility(View.GONE);
                editBtn.setVisibility(View.VISIBLE);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cooments = commentEdtTxt.getText().toString();
                db.updateUserNotes(preferences.getString("user_id", ""), cooments, 2);
//                mListener.messageReceived(cooments);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

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
        getStudentCommentDetailsList();
    }

    //
    public void getStudentCommentDetailsList() {
        message = "";
        commentEdtTxt.setText("");
        message = db.getUserNotes(preferences.getString("user_id", ""));
        if (message.equals(null)) {
            commentEdtTxt.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.GONE);
        } else if (message.equals("null")) {
            commentEdtTxt.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.GONE);
        } else if (message.equals("")) {
            commentEdtTxt.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.GONE);
        } else {

            saveBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.VISIBLE);
            commentEdtTxt.setText(message);


        }
    }
}
