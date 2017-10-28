package com.shadowcs.themis.test;

import static org.junit.Assert.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import com.shadowcs.themis.EventManager;
import com.shadowcs.themis.util.concurrent.SimpleThreadFactory;


public class EventManagerTest {

	@Test
	public void objectCreation() {
		new EventManager().close();;
	}
	
	@Test
	public void threadCreationNull() throws InterruptedException {
		
		AtomicInteger integer = new AtomicInteger();
		
		EventManager eventManager = new EventManager();
		eventManager.addListener(null, Runnable.class);
		for(int i = 0; i < 100; i++) {
			eventManager.execute((Runnable) () -> integer.incrementAndGet());
		}
		
		for(int i = 0; i < 100; i++) {
			try {
				eventManager.submit((Runnable) () -> integer.incrementAndGet()).get();
			} catch(ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < 100; i++) {
			eventManager.invoke((Runnable) () -> integer.incrementAndGet());
		}
		
		assertEquals(0, integer.get());
	}
	
	@Test
	public void threadCreationListener() throws InterruptedException {
		
		AtomicInteger integer = new AtomicInteger();
		AtomicInteger expected = new AtomicInteger();
		
		EventManager eventManager = new EventManager();
		eventManager.addListener((run) -> run.run(), Runnable.class);
		for(int i = 0; i < 100; i++) {
			expected.incrementAndGet();
			eventManager.execute((Runnable) () -> integer.incrementAndGet());
		}
		
		for(int i = 0; i < 100; i++) {
			try {
				expected.incrementAndGet();
				eventManager.submit((Runnable) () -> integer.incrementAndGet()).get();
			} catch(ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < 100; i++) {
			expected.incrementAndGet();
			eventManager.invoke((Runnable) () -> integer.incrementAndGet());
		}
		
		Thread.sleep(1000);
		
		assertEquals(300, integer.get());
	}

}
