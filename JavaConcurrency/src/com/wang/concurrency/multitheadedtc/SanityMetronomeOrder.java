package com.wang.concurrency.multitheadedtc;

import org.junit.Before;
import org.junit.Test;

import edu.umd.cs.mtc.MultithreadedTestCase;

public class SanityMetronomeOrder extends MultithreadedTestCase
{
    String s;

    @Before
    public void initialize()
    {
        s = "";
    }

    public void thread1()
    {
        waitForTick(1);
        s += "A";

        waitForTick(3);
        s += "C";

        waitForTick(6);
        s += "F";
    }

    public void thread2()
    {
        waitForTick(2);
        s += "B";

        waitForTick(5);
        s += "E";

        waitForTick(8);
        s += "H";
    }

    public void thread3()
    {
        waitForTick(4);
        s += "D";

        waitForTick(7);
        s += "G";

        waitForTick(9);
        s += "I";
    }

    @Test
    public void finish()
    {
    	System.out.println(s);
        assertEquals(s, "ABCDEFGHI");
    }
}
