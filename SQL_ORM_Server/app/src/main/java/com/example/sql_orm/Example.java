package com.example.sql_orm;

import android.content.SyncAdapterType;

import java.util.Arrays;

/**
 * Created by Vilagra on 22.02.2017.
 */

public class Example {
    public static void main(String[] args) {
        int data[] = { 3, 6, 7, 10, 34, 56, 60 };
        int numberToFind = 65;
        int indexLow = 0;
        int indexHigh = data.length - 1;
        int res = -1;
        while (indexLow <= indexHigh) {
            int indexMid = (indexLow+indexHigh)/2;
            int midValuue = data[indexMid];

            if (midValuue < numberToFind) {
                indexLow = indexMid + 1;
            }
            else if (midValuue > numberToFind)
                indexHigh= indexMid-1;
            else{
                res=indexMid;
            break;
            }
        }
        System.out.println(res);
    }

}
