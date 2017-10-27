package com.shadowcs.themis.test;

import static org.junit.Assert.assertEquals;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import com.shadowcs.themis.util.concurrent.ProcedureThreadFactory;
import com.shadowcs.themis.util.concurrent.SimpleThreadFactory;


public class ProcedureThreadFactoryTest {

	@Test
	public void objectCreation() {
		new SimpleThreadFactory();
	}
	
	@Test
	public void threadCreationNull() throws InterruptedException {
		
		ThreadFactory sThreadFactory = new ProcedureThreadFactory();
		for(int i = 0; i < 100; i++) {
			Thread t = sThreadFactory.newThread(null);
			t.start();
			t.join(1000);
		}
	}
	
	@Test
	public void threadCreationProc() throws InterruptedException {
		
		// TODO: text output is not the best testing
		ThreadFactory sThreadFactory = new ProcedureThreadFactory(() -> System.out.println("Proc run"));
		for(int i = 0; i < 100; i++) {
			Thread t = sThreadFactory.newThread(null);
			t.start();
			t.join(1000);
		}
	}
	
	@Test
	public void threadWork() throws InterruptedException {
		
		AtomicInteger integer = new AtomicInteger();

		ThreadFactory sThreadFactory = new ProcedureThreadFactory();
		for(int i = 0; i < 100; i++) {
			Thread t = sThreadFactory.newThread(() -> integer.incrementAndGet());
			t.start();
			t.join(1000);
		}
		
		assertEquals(100, integer.get());
	}

}
