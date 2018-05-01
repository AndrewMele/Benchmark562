package com.example.drew.benchmark562;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import android.util.Log;
import android.content.Context;
import java.util.Random;
import java.lang.String;
import java.io.File;
// NOTES:
// Apps are each given a fixed amount of heap (generally RAM) space

public class MemoryBenchmark
{
    //----------------------------------------------------------------------------------------------
    // Primary Storage Tests
    //----------------------------------------------------------------------------------------------

    static public long TestPrimSeqRead(Context context)
    {
        String data = "";
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        String receiveString;
        StringBuilder stringBuilder;
        long startTime;
        long stopTime;
        long elapsedNanos;
        int numOps = 1000;

        startTime = System.nanoTime();
        for (int i = 0; i < numOps; i++)
        {
            try{
                 inputStream = context.openFileInput("test" + i + ".txt");

                if ( inputStream != null ) {
                    inputStreamReader = new InputStreamReader(inputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    receiveString = "";
                    stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    data = stringBuilder.toString();
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
        }
        stopTime = System.nanoTime();
        elapsedNanos = stopTime - startTime;

        return elapsedNanos;
    }

    static public long TestPrimSeqWrite(Context context)
    {
        String data = "Testing...";
        OutputStreamWriter stream;
        long startTime;
        long stopTime;
        long elapsedNanos;
        int numOps = 1000;
        startTime = System.nanoTime();
        for (int i = 0; i < numOps; i++)
        {
            try {
                stream = new OutputStreamWriter(context.openFileOutput("test" + i + ".txt", Context.MODE_PRIVATE));
                stream.write(data);
                stream.close();
            }
            catch(IOException e){
                Log.e("Exception", "File write failed: " + e.toString());
            }

        }
        stopTime = System.nanoTime();
        elapsedNanos = stopTime - startTime;

        return elapsedNanos;
    }

    static public long TestPrimRandRead(Context context)
    {
        String data = "";
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        String receiveString;
        StringBuilder stringBuilder;
        Random rand = new Random();
        long startTime;
        long stopTime;
        long elapsedNanos;
        int numOps = 1000;

        startTime = System.nanoTime();
        for (int i = 0; i < numOps; i++)
        {
            try{
                inputStream = context.openFileInput("test" + rand.nextInt(numOps+1) + ".txt");

                if ( inputStream != null ) {
                    inputStreamReader = new InputStreamReader(inputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    receiveString = "";
                    stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    data = stringBuilder.toString();
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
        }
        stopTime = System.nanoTime();
        elapsedNanos = stopTime - startTime;

        return elapsedNanos;
    }

    static public long TestPrimRandWrite(Context context)
    {
        String data = "Testing Random...";
        OutputStreamWriter stream;
        Random rand = new Random();
        long startTime;
        long stopTime;
        long elapsedNanos;
        int numOps = 1000;
        startTime = System.nanoTime();
        for (int i = 0; i < numOps; i++)
        {
            try {
                stream = new OutputStreamWriter(context.openFileOutput("test" + rand.nextInt(numOps+1) + ".txt", Context.MODE_PRIVATE));
                stream.write(data);
                stream.close();
            }
            catch(IOException e){
                Log.e("Exception", "File write failed: " + e.toString());
            }

        }
        stopTime = System.nanoTime();
        elapsedNanos = stopTime - startTime;

        return elapsedNanos;
    }
    static public long TestPrimZeroing(Context context)
    {
        long startTime;
        long stopTime;
        long elapsedNanos;
        String[] file_list = context.fileList();
        startTime = System.nanoTime();
        for (int i = 0; i < file_list.length; i++)
        {
            context.deleteFile(file_list[i]);
        }
        stopTime = System.nanoTime();
        elapsedNanos = stopTime - startTime;

        return elapsedNanos;
    }

    //----------------------------------------------------------------------------------------------
    // Secondary Storage Tests
    //----------------------------------------------------------------------------------------------

    static public long TestSecSeqRead()
    {
        return 0;
    }

    static public long TestSecSeqWrite()
    {
        return 0;
    }

    static public long TestSecRandRead()
    {
        return 0;
    }
    static public long TestSecRandWrite()
    {
        return 0;
    }

    static public long TestSecZeroing()
    {
        return 0;
    }



    //----------------------------------------------------------------------------------------------
    // Helper functions
    //----------------------------------------------------------------------------------------------

    public static long GetAvailableMemInBytes()
    {
        Runtime runtime = Runtime.getRuntime();
        long maxMemBytes = runtime.maxMemory();
        long usedMemBytes = runtime.totalMemory() - runtime.freeMemory();
        return maxMemBytes - usedMemBytes;
    }
}
