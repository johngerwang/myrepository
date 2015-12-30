package com.wang.concurrency.multitheadedtc;

import java.util.concurrent.ArrayBlockingQueue;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import junit.framework.TestCase;


public class SampleTests extends TestCase {

    // -- EXAMPLE 1 --
        
        class MTCBoundedBufferTest extends MultithreadedTestCase {
                ArrayBlockingQueue<Integer> buf;
                @Override public void initialize() {
                        buf = new ArrayBlockingQueue<Integer>(1); 
                }

                public void threadPutPut() throws InterruptedException {
                	System.out.println("threadPutPut() tick1 :"+this.getTick());
                        buf.put(42);
                    	System.out.println("threadPutPut() buf.put(42) :"+this.getTick());
                        buf.put(17);
                    	System.out.println("threadPutPut() buf.put(17) :"+this.getTick());
                        assertTick(1);
                }

                public void threadTakeTake() throws InterruptedException {
                	System.out.println("threadTakeTake() :"+this.getTick());
                        waitForTick(1);
                    	System.out.println("threadTakeTake() waitForTick(1) :"+this.getTick());
                        assertTrue(buf.take() == 42);
                    	System.out.println("threadTakeTake() buf.take() == 42 :"+this.getTick());
                        assertTrue(buf.take() == 17);
                    	System.out.println("threadTakeTake() buf.take() == 17 :"+this.getTick());
                }

                @Override public void finish() {
                        assertTrue(buf.isEmpty());
                }               
        }
        
    public void testMTCBoundedBuffer() throws Throwable {
        TestFramework.runOnce( new MTCBoundedBufferTest() );
    }
    
    public static void main(String[] args) throws Throwable{
    	new SampleTests().testMTCBoundedBuffer();
    }
}