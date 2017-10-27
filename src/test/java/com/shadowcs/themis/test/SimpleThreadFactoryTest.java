package com.shadowcs.themis.test;

import static org.junit.Assert.*;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import com.shadowcs.themis.util.concurrent.SimpleThreadFactory;


public class SimpleThreadFactoryTest {

	@Test
	public void objectCreation() {
		new SimpleThreadFactory();
	}
	
	@Test
	public void threadCreation() throws InterruptedException {
		
		ThreadFactory sThreadFactory = new SimpleThreadFactory();
		for(int i = 0; i < 100; i++) {
			Thread t = sThreadFactory.newThread(null);
			t.start();
			t.join(1000);
		}
	}
	
	@Test
	public void threadWork() throws InterruptedException {
		
		AtomicInteger integer = new AtomicInteger();

		ThreadFactory sThreadFactory = new SimpleThreadFactory();
		for(int i = 0; i < 100; i++) {
			Thread t = sThreadFactory.newThread(() -> integer.incrementAndGet());
			t.start();
			t.join(1000);
		}
		
		assertEquals(100, integer.get());
	}

}
