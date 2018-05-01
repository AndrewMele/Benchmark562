package com.example.drew.benchmark562;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    Context context;

    // Variables for the controls we will be manipulating in software
    TextView loggerPanel;
    TextView bmark1Score;
    TextView bmark2Score;
    TextView bmark3Score;
    //TextView totalScore;
    Button bmark1RunBtn;
    Button bmark2RunBtn;
    Button bmark3RunBtn;

    // To keep track of the scores for each of the benchmarks
    long cpuScore = 0;
    long memScore = 0;
    long batScore = 0;
    long totScore = 0;

    AlertDialog batDialog;
    AlertDialog cpuDialog;
    AlertDialog memDialog;

    Boolean perfBatteryFlag = false;
    BatteryManager bm;
    int preTestBatPct;
    int postTestBatPct;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benchmark_home);

        // Associate the variables created for each control with specific controls
        // that was created for the UI
        loggerPanel = (TextView) findViewById(R.id.LoggerPanel);
        bmark1Score = (TextView) findViewById(R.id.bmark1Score);
        bmark2Score = (TextView) findViewById(R.id.bmark2Score);
        bmark3Score = (TextView) findViewById(R.id.bmark3Score);
        //totalScore = (TextView) findViewById(R.id.totalScoreLabel);
        bmark1RunBtn = (Button) findViewById(R.id.bmark1RunBtn);
        bmark2RunBtn = (Button) findViewById(R.id.bmark2RunBtn);
        bmark3RunBtn = (Button) findViewById(R.id.bmark3RunBtn);

        // Allows text view to scroll
        loggerPanel.setMovementMethod(new ScrollingMovementMethod());

        // Provide some default data for some controls
        loggerPanel.setText("Waiting for user input...\n");
        bmark1Score.setText("---");
        bmark2Score.setText("---");
        bmark3Score.setText("---");
        //totalScore.setText("Total Score: ?");

        context = getBaseContext();

        bm = (BatteryManager)getSystemService(BATTERY_SERVICE);

        // Event for when bmark1RunBtn is pressed
        bmark1RunBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RunCpuBenchmark cpuBenchmark = new RunCpuBenchmark();
                cpuBenchmark.execute();
            }
        });

        // Event for when bmark2RunBtn is pressed
        bmark2RunBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RunMemoryBenchmark memoryBenchmark = new RunMemoryBenchmark();
                memoryBenchmark.execute();
            }
        });

        // Event for when bmark3RunBtn is pressed
        bmark3RunBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RunBatBenchmark runBatBenchmark = new RunBatBenchmark();
                runBatBenchmark.execute();
            }
        });
    }

    // sub-class so that all vars within the MainActivity class (including controls)
    // can be accessed within the class
    private class RunCpuBenchmark extends AsyncTask<Void, Void, Void>
    {
        long lowOrderNanos = 0;
        long highOrderNanos = 0;
        long compressionNanos = 0;
        long sortingNanos = 0;
        long hashingNanos = 0;

        @Override
        protected void onPreExecute()
        {
            DisableAllButtons();

            cpuDialog = new AlertDialog.Builder(MainActivity.this).create();
            cpuDialog.setTitle("Running CPU benchmark...");
            cpuDialog.setMessage("This pop-up will disappear when the benchmark finishes.");
            cpuDialog.setCancelable(false);
            cpuDialog.setCanceledOnTouchOutside(false);
            cpuDialog.show();
            loggerPanel.append("\n------------RUNNING CPU BENCHMARKS------------\n");
        }

        @Override
        protected Void doInBackground(Void... values)
        {
            lowOrderNanos = CpuBenchmark.TestLowOrderOps();
            highOrderNanos = CpuBenchmark.TestHighOrderOps();
            compressionNanos = CpuBenchmark.TestCompressionPerf();
            sortingNanos = CpuBenchmark.TestSortingPerf();
            hashingNanos = CpuBenchmark.TestHashingPerf();
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            cpuDialog.dismiss();

            // Log results
            loggerPanel.append("Low-order nano secs: " + lowOrderNanos + "\n");
            loggerPanel.append("High-order nano secs: " + highOrderNanos + "\n");
            loggerPanel.append("Compression nano secs: " + compressionNanos + "\n");
            loggerPanel.append("Sorting nano secs: " + sortingNanos + "\n");
            loggerPanel.append("Hashing nano secs: " + hashingNanos + "\n");
            loggerPanel.append("----------CONCLUDING CPU BENCHMARKS-----------\n");

            // Compute the CPU Benchmark score
            // The CPU Benchmark score is simply the sum of the elapsed nano second
            // execution times for each test converted to millis, rounded to the nearest millis
            long CpuNanos = lowOrderNanos + highOrderNanos + compressionNanos
                    + sortingNanos + hashingNanos;
            cpuScore = ((CpuNanos / 1000/*microS*/) / 1000/*milliS*/);
            bmark1Score.setText(Long.toString(cpuScore));

            // Update the Total Score
            //totScore = cpuScore + memScore + batScore;
            //totalScore.setText("Total Score: " + Long.toString(totScore));

            cpuDialog.dismiss();

            // Return UI control to the user
            EnableAllButtons();
        }
    }

    // sub-class so that all vars within the MainActivity class (including controls)
    // can be accessed within the class
    private class RunMemoryBenchmark extends AsyncTask<Void, Void, Void>
    {
        long primSeqReadNanos = 0;
        long primSeqWriteNanos = 0;
        long primRandReadNanos = 0;
        long primRandWriteNanos = 0;
        long primZeroingNanos = 0;

        @Override
        protected void onPreExecute()
        {
            loggerPanel.append("\n------------RUNNING MEMORY BENCHMARKS------------\n");
            DisableAllButtons();

            memDialog = new AlertDialog.Builder(MainActivity.this).create();
            memDialog.setTitle("Running battery benchmark...");
            memDialog.setMessage("This pop-up will disappear when the benchmark finishes.");
            memDialog.setCancelable(false);
            memDialog.setCanceledOnTouchOutside(false);
            memDialog.show();
        }

        @Override
        protected Void doInBackground(Void... values)
        {
            primSeqReadNanos = MemoryBenchmark.TestPrimSeqRead(context);
            primSeqWriteNanos = MemoryBenchmark.TestPrimSeqWrite(context);
            primRandReadNanos = MemoryBenchmark.TestPrimRandRead(context);
            primRandWriteNanos = MemoryBenchmark.TestPrimRandWrite(context);
            primZeroingNanos = MemoryBenchmark.TestPrimZeroing(context);

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            memDialog.dismiss();

            // Log results
            loggerPanel.append("Primary seq read nano secs: " + primSeqReadNanos + "\n");
            loggerPanel.append("Primary seq write nano secs: " + primSeqWriteNanos + "\n");
            loggerPanel.append("Primary rand read nano secs: " + primRandReadNanos + "\n");
            loggerPanel.append("Primary rand write nano secs: " + primRandWriteNanos + "\n");
            loggerPanel.append("Primary zeroing nano secs: " + primZeroingNanos + "\n");
            loggerPanel.append("----------CONCLUDING MEMORY BENCHMARKS-----------\n");

            // Compute the Memory Benchmark score
            long primNanos = primSeqReadNanos + primSeqWriteNanos + primRandReadNanos + primRandWriteNanos + primZeroingNanos;
            memScore = (((primNanos) / 1000/*microS*/) / 1000/*milliS*/);
            bmark2Score.setText(Long.toString(memScore));

            // Update the Total Score
            //totScore = cpuScore + memScore + batScore;
            //totalScore.setText("Total Score: " + Long.toString(totScore));

            // Return UI control to the user
            EnableAllButtons();
        }
    }

    // sub-class so that all vars within the MainActivity class (including controls)
    // can be accessed within the class
    private class RunBatBenchmark extends AsyncTask<Void, Void, Void>
    {
        long elapsedNanos = 0;

        @Override
        protected void onPreExecute()
        {
            DisableAllButtons();

            batDialog = new AlertDialog.Builder(MainActivity.this).create();
            batDialog.setTitle("Running BATTERY benchmark...");
            batDialog.setMessage("This pop-up will disappear when the benchmark finishes.");
            batDialog.setCancelable(false);
            batDialog.setCanceledOnTouchOutside(false);
            batDialog.show();

            loggerPanel.append("\n------------RUNNING BATTERY BENCHMARK------------\n");
            preTestBatPct = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            loggerPanel.append("Pre-test battery percentage: " + preTestBatPct + "%\n");
        }

        @Override
        protected Void doInBackground(Void... values)
        {
            // Summary: the function measures how long it takes for the battery%
            // to drop by 1 percent while performing the tasks located in the
            // CpuBatteryKill() function

            // Create battery manager object to obtain battery%
            BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
            // Get the battery% at the beginning of the test
            int startBat = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            // Declare variables needed within the loop
            long startNanos = 0, stopNanos = 0;
            int currBat = 0;
            Boolean startTimeFlag = false;

            // Loop until the battery% has dropped by 2%
            do {
                // Get the battery% at the beginning of each loop
                currBat = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                // Once the battery has dropped
                if(currBat == startBat-1 && !startTimeFlag)
                {
                    startTimeFlag = true;
                    startNanos = System.nanoTime();
                }

                // Call this function to perform a CPU intensive task
                // on each iteration of the loop to drain the battery
                // more quickly
                CpuBenchmark.TestHashingPerf();
            } while(currBat > startBat - 2);

            // Stop the timer and calculate the elapsed time
            stopNanos = System.nanoTime();
            elapsedNanos = stopNanos - startNanos;

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            postTestBatPct = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            loggerPanel.append("Post-test battery percentage: " + postTestBatPct + "%\n");
            loggerPanel.append("------------CONCLUDING BATTERY BENCHMARK------------\n");
            perfBatteryFlag = false;
            batDialog.dismiss();

            batScore = (((elapsedNanos) / 1000/*microS*/) / 1000/*milliS*/);
            bmark3Score.setText(Long.toString(batScore));

            // Update the Total Score
            //totScore = cpuScore + memScore + batScore;
            //totalScore.setText("Total Score: " + Long.toString(totScore));


            // Return UI control to the user
            EnableAllButtons();
        }
    }


    //----------------------------------------------------------------------------------------------
    // Helper functions
    //----------------------------------------------------------------------------------------------

    public void DisableAllButtons()
    {
        bmark1RunBtn.setEnabled(false);
        bmark2RunBtn.setEnabled(false);
        bmark3RunBtn.setEnabled(false);
    }

    public void EnableAllButtons()
    {
        bmark1RunBtn.setEnabled(true);
        bmark2RunBtn.setEnabled(true);
        bmark3RunBtn.setEnabled(true);
    }
}
