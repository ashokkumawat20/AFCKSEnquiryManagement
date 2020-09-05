package in.afckstechnologies.afcksenquirymanagement.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.List;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.models.StudentsLoginLogsDAO;
import in.afckstechnologies.afcksenquirymanagement.utils.AppStatus;
import in.afckstechnologies.afcksenquirymanagement.utils.Constant;
import in.afckstechnologies.afcksenquirymanagement.utils.FeesListener;
import in.afckstechnologies.afcksenquirymanagement.view.AttendanceDetailsView;


/**
 * Created by admin on 3/18/2017.
 */

public class LoginLogsDetailsListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<StudentsLoginLogsDAO> data;
    StudentsLoginLogsDAO current;
    int currentPos = 0;
    String id, id1;
    String centerId;
    int ID;
    int clickflag = 1;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    private static FeesListener mListener;

    // create constructor to innitilize context and data sent from MainActivity
    public LoginLogsDetailsListAdpter(Context context, List<StudentsLoginLogsDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();

    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_login_logs_students_details, parent, false);
        LoginLogsDetailsListAdpter.MyHolder holder = new LoginLogsDetailsListAdpter.MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        final LoginLogsDetailsListAdpter.MyHolder myHolder = (LoginLogsDetailsListAdpter.MyHolder) holder;
        current = data.get(position);
        myHolder.view_courses_name.setText(current.getUser_id() + " - " + current.getName());
        myHolder.view_courses_name.setTag(position);

        myHolder.view_mobile.setText(current.getMobile_no());
        myHolder.view_mobile.setTag(position);

        myHolder.login_time.setText(current.getLogin_time());
        myHolder.login_time.setTag(position);

        myHolder.user_inBatch.setText(current.getUser_inBatch());
        myHolder.user_inBatch.setTag(position);


        myHolder.lead_Layout.setTag(position);
        myHolder.clickLayout.setTag(position);

        myHolder.callingButton.setTag(position);
        myHolder.whatsappeButton.setTag(position);

        if (current.getStatus().equals("0")) {
            myHolder.i_prebatch.setBackgroundColor(context.getResources().getColor(R.color.color_rbutton));
        }
        if (current.getStatus().equals("1")) {
            myHolder.i_prebatch.setBackgroundColor(context.getResources().getColor(R.color.confirm_color));
        }
        myHolder.callingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + current.getMobile_no()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(callIntent);


            }
        });
        myHolder.whatsappeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                PackageManager packageManager = context.getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    String url = "https://api.whatsapp.com/send?phone=" + "91" + current.getMobile_no() + "&text=" + URLEncoder.encode("", "UTF-8");

                    if (preferences.getString("enquiry_user_id", "").equals("AT")) {
                        i.setPackage("com.whatsapp.w4b");
                    } else {
                        i.setPackage("com.whatsapp");
                    }
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        context.startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


        myHolder.lead_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickflag == 1) {
                    clickflag = 2;
                    myHolder.clickLayout.setVisibility(View.VISIBLE);

                } else {
                    clickflag = 1;
                    myHolder.clickLayout.setVisibility(View.GONE);

                }
            }
        });
    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView view_courses_name, view_mobile, login_time, user_inBatch;
        LinearLayout i_prebatch, lead_Layout, clickLayout;
        ImageView callingButton, whatsappeButton;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            view_courses_name = (TextView) itemView.findViewById(R.id.view_courses_name);
            view_mobile = (TextView) itemView.findViewById(R.id.view_mobile);
            login_time = (TextView) itemView.findViewById(R.id.login_time);
            user_inBatch = (TextView) itemView.findViewById(R.id.user_inBatch);
            i_prebatch = (LinearLayout) itemView.findViewById(R.id.i_prebatch);
            lead_Layout = (LinearLayout) itemView.findViewById(R.id.lead_Layout);
            clickLayout = (LinearLayout) itemView.findViewById(R.id.clickLayout);
            callingButton = (ImageView) itemView.findViewById(R.id.callingButton);
            whatsappeButton = (ImageView) itemView.findViewById(R.id.whatsappeButton);
        }

    }

    public static void bindListener(FeesListener listener) {
        mListener = listener;
    }
}
