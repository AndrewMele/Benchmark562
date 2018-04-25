package com.example.drew.benchmark562;

import java.util.Arrays;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Inflater;
import java.util.zip.Deflater;

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
        // https://developer.android.com/reference/java/util/zip/Deflater

        int numOps = 1000;
        int randStrLen = 10000;
        long stopTime;
        long startTime;
        long elapsedNanos = 0;
        String str2Compress;
        byte[] bytes2Compress;
        int compressedDataLength;
        byte[] compressedBytes;
        int decompressedBytesLen;
        byte[] decompressedBytes;
        String result;
        Deflater compressor = new Deflater();
        Inflater decompressor = new Inflater();

        for (int i = 0; i < numOps; i++)
        {
            // Create a string to compress and convert it to bytes
            try
            {
                str2Compress = GenRandString(randStrLen);
                bytes2Compress = str2Compress.getBytes("UTF-8"); // Encode a String into bytes
            } catch (java.io.UnsupportedEncodingException ex)
            {
                return -1;
            }

            // Compress the bytes
            compressedBytes = new byte[randStrLen];
            startTime = System.nanoTime(); // start compression
            compressor.setInput(bytes2Compress);
            compressor.finish();
            compressedDataLength = compressor.deflate(compressedBytes);
            compressor.end();
            stopTime = System.nanoTime(); // end compression
            elapsedNanos += stopTime - startTime;

            // Decompress the bytes
            decompressor.setInput(compressedBytes, 0, compressedDataLength);
            decompressedBytes = new byte[randStrLen];
            try
            {
                decompressedBytesLen = decompressor.inflate(decompressedBytes);
                decompressor.end();
            } catch (java.util.zip.DataFormatException ex)
            {
                return -1;
            }

            try
            {
                result = new String(decompressedBytes, 0, decompressedBytesLen, "UTF-8");
            }
            catch (java.io.UnsupportedEncodingException ex)
            {
                return -1;
            }
            stopTime = System.nanoTime(); // end decompression
            elapsedNanos += stopTime - startTime;
        }

        return 0;
    }

    static public long TestSortingPerf()
    {
        int numEleToSort = 10000;
        int intArr[] = new int[numEleToSort];
        Random rng = new Random(1); // seed of 1
        int maxRand = 1000000;
        long stopTime;
        long startTime;
        long elapsedNanos = 0;

        // fill the int array with random integers
        for (int i = 0; i < numEleToSort; i++)
        {
            intArr[i] = rng.nextInt(maxRand);
        }

        int bSortArray[] = intArr.clone();
        int rSortArray[] = intArr.clone();
        int qSortArray[] = intArr.clone();

        startTime = System.nanoTime();
        BubbleSort(bSortArray);
        RadixSort(rSortArray);
        QuickSort(qSortArray, 0, qSortArray.length - 1);
        stopTime = System.nanoTime();
        elapsedNanos += stopTime - startTime;

        return elapsedNanos;
    }

    static public long TestHashingPerf()
    {
        // https://developer.android.com/reference/java/security/MessageDigest.html

        Random rng = new Random(1); // seed of 1
        int maxStrLen = 1000;
        long startTime;
        long stopTime;
        long elapsedNanos = 0;
        int numHashOps = 10000;
        String randString;
        byte[] textBytes;
        byte[] sha1hash;
        MessageDigest md;

        // Get the instance of the MessageDigest object
        try
        {
            md = MessageDigest.getInstance("SHA-512");
        }
        catch(NoSuchAlgorithmException nsae)
        {
            System.out.println("NoSuchAlgorithmException occurred...");
            return -1;
        }

        for (int i = 0; i < numHashOps; i++)
        {
            randString = GenRandString(rng.nextInt(maxStrLen)+1); // +1 because we don't want a string of length 0
            textBytes = randString.getBytes();
            startTime = System.nanoTime();
            md.update(textBytes, 0, textBytes.length);
            sha1hash = md.digest();
            stopTime = System.nanoTime();
            elapsedNanos += stopTime - startTime;

            // Debug
            //System.out.println("The string is: " + randString);
            //System.out.println(String.format("The hash is: " + ByteArrayToHex(sha1hash)));
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

    //-------------------------------------------------------------------
    // Bubble Sort
    //-------------------------------------------------------------------
    // https://www.geeksforgeeks.org/bubble-sort/

    public static void BubbleSort(int[] arr)
    {
        for (int i = 0; i < arr.length-1; i++)
            for (int j = 0; j < arr.length-i-1; j++)
                if (arr[j] > arr[j+1])
                {
                    // swap temp and arr[i]
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
    }

    //-------------------------------------------------------------------
    // Radix Sort
    //-------------------------------------------------------------------
    // https://www.geeksforgeeks.org/radix-sort/

    // A utility function to get maximum value in arr[]
    static int getMax(int arr[], int n)
    {
        int mx = arr[0];
        for (int i = 1; i < n; i++)
            if (arr[i] > mx)
                mx = arr[i];
        return mx;
    }

    // A function to do counting sort of arr[] according to
    // the digit represented by exp.
    static void countSort(int arr[], int n, int exp)
    {
        int output[] = new int[n]; // output array
        int i;
        int count[] = new int[10];
        Arrays.fill(count,0);

        // Store count of occurrences in count[]
        for (i = 0; i < n; i++)
            count[ (arr[i]/exp)%10 ]++;

        // Change count[i] so that count[i] now contains
        // actual position of this digit in output[]
        for (i = 1; i < 10; i++)
            count[i] += count[i - 1];

        // Build the output array
        for (i = n - 1; i >= 0; i--)
        {
            output[count[ (arr[i]/exp)%10 ] - 1] = arr[i];
            count[ (arr[i]/exp)%10 ]--;
        }

        // Copy the output array to arr[], so that arr[] now
        // contains sorted numbers according to curent digit
        for (i = 0; i < n; i++)
            arr[i] = output[i];
    }

    // The main function to that sorts arr[] of size n using
    // Radix Sort
    static void RadixSort(int arr[])
    {
        // Find the maximum number to know number of digits
        int m = getMax(arr, arr.length);

        // Do counting sort for every digit. Note that instead
        // of passing digit number, exp is passed. exp is 10^i
        // where i is current digit number
        for (int exp = 1; m/exp > 0; exp *= 10)
            countSort(arr, arr.length, exp);
    }

    //-------------------------------------------------------------------
    // Quick Sort
    //-------------------------------------------------------------------
    // https://www.geeksforgeeks.org/quick-sort/

    /* This function takes last element as pivot,
       places the pivot element at its correct
       position in sorted array, and places all
       smaller (smaller than pivot) to left of
       pivot and all greater elements to right
       of pivot */
    static int partition(int arr[], int low, int high)
    {
        int pivot = arr[high];
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++)
        {
            // If current element is smaller than or
            // equal to pivot
            if (arr[j] <= pivot)
            {
                i++;

                // swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        int temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;

        return i+1;
    }


    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    static void QuickSort(int arr[], int low, int high)
    {
        if (low < high)
        {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition(arr, low, high);

            // Recursively sort elements before
            // partition and after partition
            QuickSort(arr, low, pi-1);
            QuickSort(arr, pi+1, high);
        }
    }
}
