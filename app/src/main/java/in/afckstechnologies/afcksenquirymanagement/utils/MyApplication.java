package in.afckstechnologies.afcksenquirymanagement.utils;

import android.app.Application;

/**
 * Created by Ashok Kumawat on 3/19/2018.
 */

public class MyApplication extends Application {

     static MyApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

   /* public static MyApplicatio getContext() {
        return mContext;
    }*/

    public static synchronized MyApplication getInstance()
    {
        return mContext;
    }
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}