package com.example.drew.benchmark562;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    // Variables for the controls we will be manipulating in software
    TextView loggerPanel;
    Button bmark1RunBtn;
    Button bmark2RunBtn;
    Button bmark3RunBtn;
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benchmark_home);

        //------------------------------------------------------------------------------------------
        // Associate the variables created for each control with specific controls
        // that was created for the UI
        loggerPanel = (TextView) findViewById(R.id.LoggerPanel);
        bmark1RunBtn = (Button) findViewById(R.id.bmark3RunBtn);
        bmark2RunBtn = (Button) findViewById(R.id.bmark1RunBtn);
        bmark3RunBtn = (Button) findViewById(R.id.bmark2RunBtn);
        //------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------
        // Provide some default data for some controls
        loggerPanel.setText(Get_ms_String() + "Waiting for user input...");
        //------------------------------------------------------------------------------------------


        // Event for when bmark1RunBtn is pressed
        bmark1RunBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                System.out.println("bmark1RunBtn pressed!\n");
            }
        });

        // Event for when bmark2RunBtn is pressed
        bmark2RunBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                System.out.println("bmark2RunBtn pressed!\n");
            }
        });

        // Event for when bmark3RunBtn is pressed
        bmark3RunBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                System.out.println("bmark3RunBtn pressed!\n");
            }
        });
    }

    private class RunCpuBenchmark extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... values)
        {
            return null;
        }
    }

    private class RunMemoryBenchmark extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... values)
        {
            return null;
        }
    }

    private class RunAllBenchmarks extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... values)
        {
            return null;
        }
    }

    //----------------------------------------------------------------------------------------------
    // Helper functions
    //----------------------------------------------------------------------------------------------

    // A method to return a string with the current milli-seconds in brackets
    static public String Get_ms_String()
    {
        return "[" + System.currentTimeMillis() + "] ";
    }
}
