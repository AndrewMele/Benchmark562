package com.example.drew.benchmark562;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    // Variables for the controls we will be manipulating in software
    TextView loggerPanel;
    TextView bmark1Score;
    TextView bmark2Score;
    TextView bmark3Score;
    TextView totalScore;
    Button bmark1RunBtn;
    Button bmark2RunBtn;
    Button fullTestBtn;

    // To keep track of the scores for each of the benchmarks
    long cpuScore = 0;
    long memScore = 0;
    long batScore = 0;
    long totScore = 0;

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
        totalScore = (TextView) findViewById(R.id.totalScore);
        bmark1RunBtn = (Button) findViewById(R.id.bmark1RunBtn);
        bmark2RunBtn = (Button) findViewById(R.id.bmark2RunBtn);
        fullTestBtn = (Button) findViewById(R.id.fullTestBtn);

        // Allows text view to scroll
        loggerPanel.setMovementMethod(new ScrollingMovementMethod());

        // Provide some default data for some controls
        loggerPanel.setText("Waiting for user input...\n");
        bmark1Score.setText("---");
        bmark2Score.setText("---");
        bmark3Score.setText("---");
        totalScore.setText("?");

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

        // Event for when bmark2RunBtn is pressed
        fullTestBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Context context = getBaseContext();
                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = context.registerReceiver(null, ifilter);;
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                float preTestBatPct = level / (float)scale;
                loggerPanel.append("Pre-test battery percentage: " + preTestBatPct*100 + "%\n");

                RunCpuBenchmark cpuBenchmark = new RunCpuBenchmark();
                cpuBenchmark.execute();

                // need to come up with a way to wait until cpu benchmark is done,
                // may want to consider removing async tasks, there is nothing to do on the
                // UI thread while a test is running anyway.

                RunMemoryBenchmark memoryBenchmark = new RunMemoryBenchmark();
                memoryBenchmark.execute();

                level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                float postTestBatPct = level / (float)scale;
                loggerPanel.append("Post-test battery percentage: " + postTestBatPct*100 + "%\n");
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
            loggerPanel.append("------------RUNNING CPU BENCHMARKS------------\n");
            DisableAllButtons();
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
            // Log results
            loggerPanel.append("DONE with CPU Benchmark!\n");
            loggerPanel.append("Low-order NanoSecs: " + lowOrderNanos + "\n");
            loggerPanel.append("High-order NanoSecs: " + highOrderNanos + "\n");
            loggerPanel.append("Compression NanoSecs: " + compressionNanos + "\n");
            loggerPanel.append("Sorting NanoSecs: " + sortingNanos + "\n");
            loggerPanel.append("Hashing NanoSecs: " + hashingNanos + "\n");
            loggerPanel.append("----------CONCLUDING CPU BENCHMARKS-----------\n\n");

            // Compute the CPU Benchmark score
            // The CPU Benchmark score is simply the sum of the elapsed nano second
            // execution times for each test converted to millis, rounded to the nearest millis
            long CpuNanos = lowOrderNanos + highOrderNanos + compressionNanos
                            + sortingNanos + hashingNanos;
            cpuScore = ((CpuNanos / 1000/*microS*/) / 1000/*milliS*/);
            bmark1Score.setText(Long.toString(cpuScore));

            // Update the Total Score
            totScore = cpuScore + memScore + batScore;
            totalScore.setText(Long.toString(totScore));

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

        long secSeqReadNanos = 0;
        long secSeqWriteNanos = 0;
        long secRandReadNanos = 0;
        long secRandWriteNanos = 0;
        long secZeroingNanos = 0;

        @Override
        protected void onPreExecute()
        {
            loggerPanel.append("------------RUNNING MEM BENCHMARKS------------\n");
            DisableAllButtons();
        }

        @Override
        protected Void doInBackground(Void... values)
        {
            primSeqReadNanos = MemoryBenchmark.TestPrimSeqRead();
            primSeqWriteNanos = MemoryBenchmark.TestPrimSeqWrite();
            primRandReadNanos = MemoryBenchmark.TestPrimRandRead();
            primRandWriteNanos = MemoryBenchmark.TestPrimRandWrite();
            primZeroingNanos = MemoryBenchmark.TestPrimZeroing();

            secSeqReadNanos = MemoryBenchmark.TestSecSeqRead();
            secSeqWriteNanos = MemoryBenchmark.TestSecSeqWrite();
            secRandReadNanos = MemoryBenchmark.TestSecRandRead();
            secRandWriteNanos = MemoryBenchmark.TestSecRandWrite();
            secZeroingNanos = MemoryBenchmark.TestSecZeroing();
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            // Log results
            loggerPanel.append("DONE with MEM Benchmark!\n");
            loggerPanel.append("Primary seq read NanoSecs: " + primSeqReadNanos + "\n");
            loggerPanel.append("Primary seq write NanoSecs: " + primSeqWriteNanos + "\n");
            loggerPanel.append("Primary rand read NanoSecs: " + primRandReadNanos + "\n");
            loggerPanel.append("Primary rand write NanoSecs: " + primRandWriteNanos + "\n");
            loggerPanel.append("Primary zeroing NanoSecs: " + primZeroingNanos + "\n");

            loggerPanel.append("Secondary seq read NanoSecs: " + secSeqReadNanos + "\n");
            loggerPanel.append("Secondary seq write NanoSecs: " + secSeqWriteNanos + "\n");
            loggerPanel.append("Secondary rand read NanoSecs: " + secRandReadNanos + "\n");
            loggerPanel.append("Secondary rand write NanoSecs: " + secRandWriteNanos + "\n");
            loggerPanel.append("Secondary zeroing NanoSecs: " + secZeroingNanos + "\n");
            loggerPanel.append("----------CONCLUDING MEM BENCHMARKS-----------\n\n");

            // Compute the Memory Benchmark score
            long primNanos = primSeqReadNanos + primSeqWriteNanos + primRandReadNanos
                    + primRandWriteNanos + primZeroingNanos;
            long secNanos = secSeqReadNanos + secSeqWriteNanos + secRandReadNanos
                    + secRandWriteNanos + secZeroingNanos;
            memScore = (((primNanos+secNanos) / 1000/*microS*/) / 1000/*milliS*/);
            bmark1Score.setText(Long.toString(cpuScore));

            // Update the Total Score
            totScore = cpuScore + memScore + batScore;
            totalScore.setText(Long.toString(totScore));

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
        fullTestBtn.setEnabled(false);
    }

    public void EnableAllButtons()
    {
        bmark1RunBtn.setEnabled(true);
        bmark2RunBtn.setEnabled(true);
        fullTestBtn.setEnabled(false);
    }
}
