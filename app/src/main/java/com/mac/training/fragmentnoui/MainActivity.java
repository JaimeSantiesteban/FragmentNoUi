package com.mac.training.fragmentnoui;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements TaskFragment.TaskCallbacks {

    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private TaskFragment taskFragment;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = true; // Set this to false to disable logs.

    //UI
    private ProgressBar mProgressBar;
    private TextView mPercent;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
        mPercent = (TextView) findViewById(R.id.percent_progress);
        mButton = (Button) findViewById(R.id.task_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskFragment.isRunning()) {
                    taskFragment.cancel();
                } else {
                    taskFragment.start();
                }
            }
        });

        FragmentManager fm = getFragmentManager();
        taskFragment = ((TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT));

        // if the fragment is non-null
        // then it is currently being retained across a configuration change.
        if (taskFragment == null) {
            taskFragment = new TaskFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(taskFragment, TAG_TASK_FRAGMENT);
            ft.commit();
        }
    }


    @Override
    public void onPreExecute() {
        if (DEBUG) Log.i(TAG, "onPreExecute()");
        mButton.setText(getString(R.string.cancel));
        Toast.makeText(this, R.string.task_started_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressUpdate(int percent) {
        if (DEBUG) Log.i(TAG, "onProgressUpdate(" + percent + "%)");
        mProgressBar.setProgress(percent * mProgressBar.getMax() / 100);
        mPercent.setText(percent + "%");
    }

    @Override
    public void onCancelled() {
        if (DEBUG) Log.i(TAG, "onCancelled()");
        mButton.setText(getString(R.string.start));
        mProgressBar.setProgress(0);
        mPercent.setText(getString(R.string.zero_percent));
        Toast.makeText(this, R.string.task_cancelled_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostExecute() {
        if (DEBUG) Log.i(TAG, "onPostExecute()");
        mButton.setText(getString(R.string.start));
        mProgressBar.setProgress(mProgressBar.getMax());
        mPercent.setText(getString(R.string.one_hundred_percent));
        Toast.makeText(this, R.string.task_complete_msg, Toast.LENGTH_SHORT).show();
    }

    /************************/
    /***** LOGS & STUFF *****/
    /************************/

    @Override
    protected void onStart() {
        if (DEBUG) Log.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (DEBUG) Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (DEBUG) Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (DEBUG) Log.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (DEBUG) Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }
}
