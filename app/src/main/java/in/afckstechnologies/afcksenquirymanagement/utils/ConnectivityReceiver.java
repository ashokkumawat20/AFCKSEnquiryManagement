package in.afckstechnologies.afcksenquirymanagement.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.afckstechnologies.afcksenquirymanagement.provider.StudentContract;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentProvider;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    //context and database helper object
    private Context context;
    //database helper object
    private StudentProvider.AFCKSDatabase db;
    int countu = 0;
    int countud = 0;
    int countul = 0;
    int countuc = 0;
    int countuud = 0;
    int countuld = 0;
    int countucd = 0;
    int countudd = 0;
    int totalsize;
    int temp_status;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {

        this.context = context;

        db = new StudentProvider.AFCKSDatabase(context);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }


        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

//getting all the unsynced users
                synchronized (context) {
                    Cursor cursor = db.getUnsyncedUsers();
                    totalsize = cursor.getCount();
                    if (cursor.moveToFirst()) {
                        do {
                            //calling the method to save the unsynced name to MySQL
                            saveUser(cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_ENTRY_ID)), cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_FIRST_NAME)), cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_LAST_NAME)), cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_EMAIL_ID)), cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_MOBILE_NO)), cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_GENDER)));
                            countu++;
                        } while (cursor.moveToNext());
                    }
                    if (totalsize == 0) {
                        userDayPre();
                    }
                }
            }
        }
    }

    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    private void saveUser(final String id, final String first_name, final String last_name, final String email_id, String mobile_no, String gender) {
        JSONObject json = new JSONObject();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        final String date = format.format(cal.getTime());
        try {
            json.put("first_name", first_name);
            json.put("last_name", last_name);
            json.put("mobile_no", mobile_no);
            json.put("email_id", email_id);
            json.put("gender", gender);
            json.put("fcm_id", "Admin");
            json.put("created_at", date);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_STUDENT_REGISTRATION, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("status")) {
                                //updating the status in sqlite
                                String user_id = response.getString("user_id");
                                db.updateUserIdStatus(user_id, id, 1);
                                if (countu == totalsize) {
                                    userDayPre();

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void userDayPre() {
        Cursor cursor = db.getUnsyncedUserDayPrefrence();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveUserDayPre(cursor.getString(cursor.getColumnIndex(StudentContract.UserDayPrefrence.COLUMN_NAME_DAYPREFRENCE_ID)), cursor.getString(cursor.getColumnIndex(StudentContract.UserDayPrefrence.COLUMN_NAME_USER_ID)));
                countud++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {
            userLoc();
        }
    }

    private void saveUserDayPre(final String dayprefrence_id, final String user_id) {
        JSONObject json = new JSONObject();

        try {
            json.put("dayprefrence_id", dayprefrence_id);
            json.put("user_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_SEND_DAYPREFRENCE_DETAILS, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("status")) {
                                //updating the status in sqlite
                                String did = response.getString("did");
                                db.updateUserDayPre(user_id, did, dayprefrence_id, 1);
                                if (countud == totalsize) {
                                    userLoc();

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void userLoc() {
        Cursor cursor = db.getUnsyncedUserLoc();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveUserLoc(cursor.getString(cursor.getColumnIndex(StudentContract.StudentLocation.COLUMN_NAME_ENTRY_ID)), cursor.getString(cursor.getColumnIndex(StudentContract.StudentLocation.COLUMN_NAME_USER_ID)));
                countul++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {
            userCourse();
        }
    }

    private void saveUserLoc(final String id, final String user_id) {
        JSONObject json = new JSONObject();

        try {
            json.put("branch_id", id);
            json.put("user_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_SEND_LOCATION_DETAILS, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("status")) {
                                //updating the status in sqlite
                                String bid = response.getString("bid");
                                db.updateUserLoc(user_id, bid, id, 1);
                                if (countul == totalsize) {
                                    userCourse();

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void userCourse() {
        Cursor cursor = db.getUnsyncedUserCourse();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveUserCourse(cursor.getString(cursor.getColumnIndex(StudentContract.StudentCourse.COLUMN_NAME_ENTRY_ID)), cursor.getString(cursor.getColumnIndex(StudentContract.StudentCourse.COLUMN_NAME_USER_ID)));
                countuc++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {
            deleteUser();
        }

    }


    private void saveUserCourse(final String id, final String user_id) {
        JSONObject json = new JSONObject();

        try {
            json.put("course_id", id);
            json.put("user_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_SEND_DETAILS, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("status")) {
                                //updating the status in sqlite
                                String cid = response.getString("cid");
                                db.updateUserCourse(user_id, cid, id, 1);

                                if (countuc == totalsize) {
                                    deleteUser();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void deleteUser() {
        Cursor cursor = db.getUnsyncedUserDelete();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveUserDelete(cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_ENTRY_ID)));
                countuud++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {
            updateUser();
        }
    }

    private void saveUserDelete(final String user_id) {
        JSONObject json = new JSONObject();

        try {

            json.put("user_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_STUDENT_DELETE, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("status")) {
                                //updating the status in sqlite
                                db.deleteUser(user_id);
                                if (countuud == totalsize) {
                                    updateUser();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void updateUser() {

        Cursor cursor = db.getUserDetailsUpdate();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                countuld++;
                saveUserUpdate(cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_ENTRY_ID)), cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_FIRST_NAME)), cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_LAST_NAME)), cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_EMAIL_ID)), cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_MOBILE_NO)), cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_GENDER)), cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_NOTES)), cursor.getString(cursor.getColumnIndex(StudentContract.Entry.COLUMN_NAME_STATUS)));
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {
            deleteUserLoc();

        }
    }


    private void saveUserUpdate(final String id, final String first_name, final String last_name, final String email_id, final String mobile_no, final String gender, final String notes, final String status) {
        JSONObject json = new JSONObject();

        try {
            json.put("id", id);
            json.put("first_name", first_name);
            json.put("last_name", last_name);
            json.put("mobile_no", mobile_no);
            json.put("email_id", email_id);
            json.put("gender", gender);
            json.put("Notes", notes);
            json.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_UPDATEUSERDETAILSOFFLINE, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("status")) {
                                //updating the status in sqlite

                                db.updateUserDetails(id, first_name, last_name, mobile_no, email_id, gender, 1);
                                if (countuld == totalsize) {
                                    deleteUserLoc();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void deleteUserLoc() {
        Cursor cursor = db.getUnsyncedUserDeleteLoc();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveUserLocDelete(cursor.getString(cursor.getColumnIndex(StudentContract.StudentLocation.COLUMN_NAME_USER_ID)), cursor.getString(cursor.getColumnIndex(StudentContract.StudentLocation.COLUMN_NAME_ENTRY_ID)));
                countucd++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {
            deleteUserCourse();
        }
    }


    private void saveUserLocDelete(final String user_id, final String branch_id) {
        JSONObject json = new JSONObject();

        try {

            json.put("user_id", user_id);
            json.put("branch_id", branch_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_DELETE_CENTER, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("status")) {
                                //updating the status in sqlite
                                db.deleteLoc(branch_id, user_id);
                                if (countucd == totalsize) {
                                    deleteUserCourse();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void deleteUserCourse() {
        Cursor cursor = db.getUnsyncedUserDeleteCourse();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveUserCourseDelete(cursor.getString(cursor.getColumnIndex(StudentContract.StudentCourse.COLUMN_NAME_USER_ID)), cursor.getString(cursor.getColumnIndex(StudentContract.StudentCourse.COLUMN_NAME_ENTRY_ID)));
                countudd++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {
            deleteUserDayPre();
        }
    }

    private void saveUserCourseDelete(final String user_id, final String course_id) {
        JSONObject json = new JSONObject();

        try {

            json.put("user_id", user_id);
            json.put("course_id", course_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_DELETE_COURSE, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("status")) {
                                //updating the status in sqlite
                                db.deleteCourse(course_id, user_id);
                                if (countudd == totalsize) {
                                    deleteUserDayPre();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void deleteUserDayPre() {
        Cursor cursor = db.getUnsyncedUserDeleteDayPre();
        totalsize = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                //calling the method to save the unsynced name to MySQL
                saveUserDayPreDelete(cursor.getString(cursor.getColumnIndex(StudentContract.UserDayPrefrence.COLUMN_NAME_USER_ID)), cursor.getString(cursor.getColumnIndex(StudentContract.UserDayPrefrence.COLUMN_NAME_DAYPREFRENCE_ID)));
                // countudd++;
            } while (cursor.moveToNext());
        }
        if (totalsize == 0) {
            // deleteUserDayPre();
        }
    }

    private void saveUserDayPreDelete(final String user_id, final String dayid) {
        JSONObject json = new JSONObject();

        try {

            json.put("user_id", user_id);
            json.put("dayprefrence_id", dayid);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_DELETE_DAYPREFRENCE, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("status")) {
                                //updating the status in sqlite
                                db.deleteUserDayPre(dayid, user_id);
                                if (countudd == totalsize) {
                                  //  deleteUserDayPre();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

}