package com.example.drew.benchmark562;

import java.util.Random;

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
        return 0;
    }
}
