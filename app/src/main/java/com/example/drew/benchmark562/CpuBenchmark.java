package com.example.drew.benchmark562;

import java.util.Random;
import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class CpuBenchmark
{
    static public long TestLowOrderOps()
    {
        // Operations such as: + and -
        int opA;
        int opB;
        Random rng = new Random(1); // seed of 1
        long stopTime;
        long startTime;
        long elapsedNanos = 0;
        int maxRand = 100000;
        int numOps = 100000;

        // perform numOps + operations
        for(int i = 0; i < numOps; i++)
        {
            opA = rng.nextInt(maxRand);
            opB = rng.nextInt(maxRand);
            startTime = System.nanoTime();
            opA += opB;
            stopTime = System.nanoTime();
            elapsedNanos += stopTime - startTime;

            // Check if nanos timer rollover occurred
            if(startTime > stopTime)
                System.out.println("Nanos timer rollover occurred");
        }

        // perform numOps - operations
        for(int i = 0; i < numOps; i++)
        {
            opA = rng.nextInt(maxRand);
            opB = rng.nextInt(maxRand);
            startTime = System.nanoTime();
            opA -= opB;
            stopTime = System.nanoTime();
            elapsedNanos += stopTime - startTime;

            // Check if nanos timer rollover occurred
            if(startTime > stopTime)
                System.out.println("Nanos timer rollover occurred");
        }

        return elapsedNanos;
    }

    static public long TestHighOrderOps()
    {
        // Operations such as: * and /
        int opA;
        int opB;
        Random rng = new Random(1); // seed of 1
        long stopTime;
        long startTime;
        long elapsedNanos = 0;
        int maxRand = 1000; // ensures that 2 times the max val does not exceed var opA's size
        int numOps = 100000;

        // perform numOps * operations
        for(int i = 0; i < numOps; i++)
        {
            opA = rng.nextInt(maxRand);
            opB = rng.nextInt(maxRand);
            startTime = System.nanoTime();
            opA *= opB;
            stopTime = System.nanoTime();
            elapsedNanos += stopTime - startTime;

            // Check if nanos timer rollover occurred
            if(startTime > stopTime)
                System.out.println("Nanos timer rollover occurred");
        }

        // perform numOps / operations
        for(int i = 0; i < numOps; i++)
        {
            opA = rng.nextInt(maxRand) + 1; // +1 for no zeros
            opB = rng.nextInt(maxRand) + 1; // +1 for no zeros
            startTime = System.nanoTime();
            opA /= opB; // okay to truncate because we don't care about the result
            stopTime = System.nanoTime();
            elapsedNanos += stopTime - startTime;

            // Check if nanos timer rollover occurred
            if(startTime > stopTime)
                System.out.println("Nanos timer rollover occurred");
        }

        return elapsedNanos;
    }

    static public long TestCompressionPerf()
    {
        return 0;
    }

    static public long TestSortingPerf()
    {
        return 0;
    }

    static public long TestHashingPerf()
    {
        Random rng = new Random(1); // seed of 1
        long startTime;
        long stopTime;
        long elapsedNanos = 0;

        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            String randString = GenRandString(rng.nextInt());
            byte[] textBytes = randString.getBytes();
            startTime = System.nanoTime();
            md.update(textBytes, 0, textBytes.length);
            byte[] sha1hash = md.digest();
            stopTime = System.nanoTime();
            elapsedNanos += stopTime - startTime;

            // Debug
            System.out.println(sha1hash);
        }
        catch(NoSuchAlgorithmException nsae)
        {
            elapsedNanos = 0;
        }

        return elapsedNanos;
    }

    static public String GenRandString(int randStrLen)
    {
        String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012345789";
        Random rng = new Random(1); // seed of 1
        String randString = "";
        for (int i = 0; i < randStrLen; i++)
        {
            randString += alphaNumeric.indexOf(rng.nextInt());
        }
        return randString;
    }
}
