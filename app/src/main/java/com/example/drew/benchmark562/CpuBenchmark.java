package com.example.drew.benchmark562;

import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        int maxStrLen = 1000;
        long startTime;
        long stopTime;
        long elapsedNanos = 0;
        int numHashOps = 100000;

        for (int i = 0; i < numHashOps; i++)
        {
            try
            {
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                String randString = GenRandString(rng.nextInt(maxStrLen)+1); // +1 because we don't want a string of length 0
                byte[] textBytes = randString.getBytes();
                startTime = System.nanoTime();
                md.update(textBytes, 0, textBytes.length);
                byte[] sha1hash = md.digest();
                stopTime = System.nanoTime();
                elapsedNanos += stopTime - startTime;

                // Debug
                System.out.println("The string is: " + randString);
                System.out.println(String.format("The hash is: " + ByteArrayToHex(sha1hash)));
            }
            catch(NoSuchAlgorithmException nsae)
            {
                System.out.println("NoSuchAlgorithmException occurred...");
                elapsedNanos = 0;
            }
        }

        return elapsedNanos;
    }

    static public String GenRandString(int randStrLen)
    {
        String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012345789";
        Random rng = new Random(1); // seed of 1
        String randString = "";
        int eleNum;
        for (int i = 0; i < randStrLen; i++)
        {
            randString += alphaNumeric.charAt(rng.nextInt(alphaNumeric.length()));
        }
        return randString;
    }

    public static String ByteArrayToHex(byte[] a)
    {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
