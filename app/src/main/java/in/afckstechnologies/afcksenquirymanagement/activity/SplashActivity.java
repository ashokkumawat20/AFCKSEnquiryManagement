package in.afckstechnologies.afcksenquirymanagement.activity;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import in.afckstechnologies.afcksenquirymanagement.R;
import in.afckstechnologies.afcksenquirymanagement.common.accounts.GenericAccountService;
import in.afckstechnologies.afcksenquirymanagement.provider.StudentContract;
import in.afckstechnologies.afcksenquirymanagement.utils.SyncService;
import in.afckstechnologies.afcksenquirymanagement.utils.SyncUtils;
import in.afckstechnologies.afcksenquirymanagement.utils.Utils;

public class SplashActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Handle to a SyncObserver. The ProgressBar element is visible until the SyncObserver reports
     * that the sync is complete.
     *
     * <p>This allows us to delete our SyncObserver once the application is no longer in the
     * foreground.
     */
    private Object mSyncObserverHandle;
    ProgressDialog mProgressDialog;
    /**
     * Create SyncAccount at launch, if needed.
     *
     * <p>This will create a new account with the system for our application, register our
     * {@link SyncService} with it, and establish a sync schedule.
     */
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Create account, if needed
        SyncUtils.CreateSyncAccount(SplashActivity.this);
        Utils.showLoadingDialog(SplashActivity.this);
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 2 seconds
                    sleep(180*1000);
                    Utils.dismissLoadingDialog();
                    finish();
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    // After 5 seconds redirect to another intent

                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
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
            runOnUiThread(new Runnable() {
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

    private void setRefreshActionButtonState(boolean refreshing) {
        // Create a progressdialog
       // mProgressDialog = new ProgressDialog(SplashActivity.this);
        if (refreshing) {
            // Set progressdialog title
           // mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
           // mProgressDialog.setMessage("Syncing data...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
           // mProgressDialog.show();
            //Utils.showLoadingDialog(SplashActivity.this);
        } else {
            //mProgressDialog.dismiss();
           // Utils.dismissLoadingDialog();
             //Intent i = new Intent(SplashActivity.this, MainActivity.class);
             //startActivity(i);
        }

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
