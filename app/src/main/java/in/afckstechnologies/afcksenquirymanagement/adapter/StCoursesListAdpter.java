package in.afckstechnologies.afcksenquirymanagement.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.models.StCoursesDAO;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;
import in.afckstechnologies.afcksenquirymanagement.utils.AppStatus;
import in.afckstechnologies.afcksenquirymanagement.utils.Config;
import in.afckstechnologies.afcksenquirymanagement.utils.WebClient;
import in.afckstechnologies.afcksenquirymanagement.view.ComingBatchDetailsView;


/**
 * Created by admin on 12/20/2016.
 */

public class StCoursesListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<StCoursesDAO> data;
    StCoursesDAO current;
    int currentPos = 0;
    String id;

    int ID;


    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;
    JSONArray jsonArray;
    String courseListResponse = "";
    boolean status;
    String message = "";
    String msg = "";
    int clickFlag = 0;

    //database helper object
    private StudentProvider.AFCKSDatabase db;
    boolean undoOn; // is undo on, you can turn it on from the toolbar menu
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    List<StCoursesDAO> itemsPendingRemoval = new ArrayList<>();

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<StCoursesDAO, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    // create constructor to innitilize context and data sent from MainActivity
    public StCoursesListAdpter(Context context, List<StCoursesDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        db = new StudentProvider.AFCKSDatabase(context);
    }


    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_st_course_details, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);
        myHolder.view_code.setText(current.getCourse_code());
        myHolder.view_code.setTag(position);

        myHolder.view_courses_name.setText(current.getNumbers() + " : " + current.getCourse_name());
        myHolder.view_courses_name.setTag(position);


        myHolder.view_courses_type.setText(current.getType_name());
        myHolder.view_courses_type.setTag(position);
        myHolder.lead_Layout.setTag(position);
        myHolder.clickLayout.setTag(position);
        myHolder.showLayout.setTag(position);
        myHolder.deleteCourse.setTag(position);

        myHolder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("course_id_for_batch", current.getId());
                prefEditor.commit();
                //  Toast.makeText(context,"C id"+current.getId(),Toast.LENGTH_SHORT).show();
                ComingBatchDetailsView comingBatchDetailsView = new ComingBatchDetailsView();
                comingBatchDetailsView.show(((FragmentActivity) context).getSupportFragmentManager(), "feesDetailsView");

            }
        });

        myHolder.deleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                id = current.getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete Course ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id1) {

                                db.updateCourseDeleteStatus(preferences.getString("user_id", ""), Integer.parseInt(id), 3);
                                removeAt(ID);
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
                alert.setTitle("Deleting Course");
                alert.show();

            }
        });


    }
    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        current = data.get(position);
        if (!itemsPendingRemoval.contains(current)) {
            itemsPendingRemoval.add(current);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {

                    remove(data.indexOf(current));

                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(current, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        current = data.get(position);
        id = current.getId();
        ID = position;
        //  Toast.makeText(context, "Remove id" + id, Toast.LENGTH_LONG).show();

        if (itemsPendingRemoval.contains(current)) {
            itemsPendingRemoval.remove(current);
        }
        if (data.contains(current)) {
            data.remove(position);
            notifyItemRemoved(position);
        }
        if (AppStatus.getInstance(context).isOnline()) {
            new deleteSale().execute();
        } else {
            db.updateCourseDeleteStatus(preferences.getString("user_id", ""), Integer.parseInt(id), 3);

        }


    }

    public boolean isPendingRemoval(int position) {
        current = data.get(position);
        return itemsPendingRemoval.contains(current);
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView view_code;
        TextView view_courses_name;
        TextView view_courses_type;
        LinearLayout clickLayout, showLayout, lead_Layout;
        ImageView deleteCourse;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            view_code = (TextView) itemView.findViewById(R.id.view_code);
            view_courses_name = (TextView) itemView.findViewById(R.id.view_courses_name);
            view_courses_type = (TextView) itemView.findViewById(R.id.view_courses_type);
            clickLayout = (LinearLayout) itemView.findViewById(R.id.clickLayout);
            showLayout = (LinearLayout) itemView.findViewById(R.id.showLayout);
            lead_Layout = (LinearLayout) itemView.findViewById(R.id.lead_Layout);
            deleteCourse=(ImageView)itemView.findViewById(R.id.deleteCourse);

        }

    }

    //
    private class deleteSale extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog

        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("user_id", preferences.getString("user_id", ""));
                        put("course_id", id);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            courseListResponse = serviceAccess.SendHttpPost(Config.URL_DELETE_COURSE, jsonLeadObj);
            Log.i("resp", "leadListResponse" + courseListResponse);


            if (courseListResponse.compareTo("") != 0) {
                if (isJSONValid(courseListResponse)) {

                    try {

                        JSONObject jObject = new JSONObject(courseListResponse);
                        status = jObject.getBoolean("status");
                        message = jObject.getString("message");
                        jsonArray = new JSONArray(courseListResponse);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(context, "Please check your network connection", Toast.LENGTH_LONG).show();

                }
            } else {

                Toast.makeText(context, "Please check your network connection.", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {
                db.deleteCourse(id, preferences.getString("user_id", ""));
            }
        }
    }

    //
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
