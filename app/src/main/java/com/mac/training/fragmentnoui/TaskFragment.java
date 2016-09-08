package com.mac.training.fragmentnoui;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by User on 9/7/2016.
 */
public class TaskFragment extends Fragment {

    private static final String TAG = TaskFragment.class.getSimpleName();
    private static final boolean DEBUG = true; // Set this to false to disable logs.
    private boolean mRunning;

    private TaskCallbacks mCallbacks;
    private DummyTask mtask;

    static interface TaskCallbacks {
        void onPreExecute();

        void onProgressUpdate(int percent);

        void onCancelled();

        void onPostExecute();
    }


    @Override
    public void onAttach(Context context) {
        if (DEBUG) Log.i(TAG, "onAttach(Activity)");
        super.onAttach(context);
        if (!(getActivity() instanceof TaskCallbacks)) {
            throw new IllegalStateException("Target fragment must implement the TaskCallbacks interface.");
        }
        // Hold a reference to the target fragment so we can report back the task's
        // current progress and results.
        mCallbacks = (TaskCallbacks) getActivity();
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }


    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach()");
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
        cancel();
    }

    /**
     * A dummy task that performs some (dumb) background work and
     * proxies progress updates and results back to the Activity.
     * <p/>
     * Note that we need to check if the callbacks are null in each
     * method in case they are invoked after the Activity's and
     * Fragment's onDestroy() method have been called.
     */
    private class DummyTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
                mRunning = true;
            }
        }

        /**
         * Note that we do NOT call the callback object's methods
         * directly from the background thread, as this could result
         * in a race condition.
         */
        @Override
        protected Void doInBackground(Void... ignore) {
            for (int i = 0; !isCancelled() && i < 100; i++) {
                if (DEBUG) Log.i(TAG, "publishProgress(" + i + "%)");
                SystemClock.sleep(100);
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... percent) {
            if (mCallbacks != null) {
                mCallbacks.onProgressUpdate(percent[0]);
            }
        }


        @Override
        protected void onCancelled() {
            if (mCallbacks != null) {
                mCallbacks.onCancelled();
                mRunning = false;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mCallbacks != null) {
                mCallbacks.onPostExecute();
                mRunning = false;
            }
        }
    }

    /*****************************/
    /***** TASK FRAGMENT API *****/
    /*****************************/

    /**
     * Start the background task.
     */
    public void start() {
        if (!mRunning) {
            mtask = new DummyTask();
            mtask.execute();
            mRunning = true;
        }
    }

    /**
     * Cancel the background task.
     */
    public void cancel() {
        if (mRunning) {
            mtask.cancel(false);
            mtask = null;
            mRunning = false;
        }
    }

    /**
     * Returns the current state of the background task.
     */
    public boolean isRunning() {
        return mRunning;
    }
}
