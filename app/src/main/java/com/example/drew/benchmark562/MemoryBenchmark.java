package com.example.drew.benchmark562;

// NOTES:
// Apps are each given a fixed amount of heap (generally RAM) space

public class MemoryBenchmark
{
    //----------------------------------------------------------------------------------------------
    // Primary Storage Tests
    //----------------------------------------------------------------------------------------------

    static public long TestPrimSeqRead()
    {
        return 0;
    }

    static public long TestPrimSeqWrite()
    {
        return 0;
    }

    static public long TestPrimRandRead()
    {
        return 0;
    }

    static public long TestPrimRandWrite()
    {
        return 0;
    }
    static public long TestPrimZeroing()
    {
        return 0;
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
