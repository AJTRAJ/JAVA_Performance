package com.by.practice.mdap;

import java.util.*;
import java.io.*;
import java.lang.Thread;
class demo {
    public static void main(String args[])
    {
        // initialization expression
        int i = 1;
        try {
        // test expression
        while (true) {
            System.out.println("HELLO DEAR = " +i);
            Thread.sleep(10000);
            i++;
            // update expression
        }
        }
        catch (Exception e) {

            // catching the exception
            System.out.println(e);
        }
    }
}
