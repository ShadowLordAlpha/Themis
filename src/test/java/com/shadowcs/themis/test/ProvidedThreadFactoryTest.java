package com.shadowcs.themis.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import com.shadowcs.themis.util.concurrent.ProvidedThreadFactory;

public class ProvidedThreadFactoryTest {

	@Test
	public void objectCreation() {
		new ProvidedThreadFactory().close();
	}
	
	@Test
	public void threadCreationBlank() throws InterruptedException {
		
		ProvidedThreadFactory pThreadFactory = new ProvidedThreadFactory();
		new Thread(() -> pThreadFactory.cede()).start();
		Thread.sleep(1000);
		for(int i = 0; i < 100; i++) {
			Thread t = pThreadFactory.newThread(null);
			if(t != null) {
				t.start();
				t.join();
			}
		}
		
		pThreadFactory.close();
	}
	
	@Test
	public void threadCreationCede() throws InterruptedException {
		
		ProvidedThreadFactory pThreadFactory = new ProvidedThreadFactory();
		new Thread(() -> pThreadFactory.cede(() -> System.out.println("Test"))).start(); // TODO: replace this with a boolean or something
		Thread.sleep(500); // sleeps are bad for tests but I can't think of a better way atm
		for(int i = 0; i < 100; i++) {
			Thread t = pThreadFactory.newThread(null);
			if(t != null) {
				t.start();
				t.join();
			}
		}
		
		Thread.sleep(500);
		pThreadFactory.close();
	}
	
	@Test
	public void threadWorkSingle() throws InterruptedException {
		
		AtomicInteger expected = new AtomicInteger();
		AtomicInteger integer = new AtomicInteger();

		ProvidedThreadFactory pThreadFactory = new ProvidedThreadFactory();
		Thread thread = new Thread(() -> pThreadFactory.cede());
		thread.start();
		
		Thread.sleep(500); // TODO: this one is an even worse test
		
		for(int i = 0; i < 100; i++) {
			Thread t = pThreadFactory.newThread(() -> integer.incrementAndGet());
			if(t != null) {
				expected.incrementAndGet();
				t.start();
			}
		}
		
		Thread.sleep(500);
		pThreadFactory.close();
		thread.join();
		
		System.out.println(expected.get());
		assertEquals(expected.get(), integer.get());
	}
	
	@Test
	public void threadWorkInterrupt() throws InterruptedException {

		ProvidedThreadFactory pThreadFactory = new ProvidedThreadFactory();
		Thread thread = new Thread(() -> pThreadFactory.cede());
		thread.start();
		
		Thread.sleep(500); // TODO: this one is an even worse test
		
		for(int i = 0; i < 100; i++) {
			Thread t = pThreadFactory.newThread(() -> {
				
				Thread.currentThread().interrupt();
			});
			
			if(t != null) {
				t.start();
			}
		}
		
		thread.join(5000);
		pThreadFactory.close();
	}

	@Test
	public void threadWorkMulti() throws InterruptedException {
		
		AtomicInteger expected = new AtomicInteger();
		AtomicInteger integer = new AtomicInteger();

		ProvidedThreadFactory pThreadFactory = new ProvidedThreadFactory();
		Thread thread0 = new Thread(() -> pThreadFactory.cede());
		Thread thread1 = new Thread(() -> pThreadFactory.cede());
		Thread thread2 = new Thread(() -> pThreadFactory.cede());
		Thread thread3 = new Thread(() -> pThreadFactory.cede());
		Thread thread4 = new Thread(() -> pThreadFactory.cede());
		Thread thread5 = new Thread(() -> pThreadFactory.cede());
		Thread thread6 = new Thread(() -> pThreadFactory.cede());
		Thread thread7 = new Thread(() -> pThreadFactory.cede());
		Thread thread8 = new Thread(() -> pThreadFactory.cede());
		Thread thread9 = new Thread(() -> pThreadFactory.cede());
		thread0.start();
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		thread5.start();
		thread6.start();
		thread7.start();
		thread8.start();
		thread9.start();
		
		for(int i = 0; i < 100; i++) {
			Thread t = pThreadFactory.newThread(() -> integer.incrementAndGet());
			if(t != null) {
				expected.incrementAndGet();
				t.start();
			}
		}
		
		Thread.sleep(1000);
		
		pThreadFactory.close();
		thread0.join();
		thread1.join();
		thread2.join();
		thread3.join();
		thread4.join();
		thread5.join();
		thread6.join();
		thread7.join();
		thread8.join();
		thread9.join(); // need to find a better way than this
		
		System.out.println(expected.get());
		assertTrue(expected.get() > 0); // TODO: need a better way than this
		assertEquals(expected.get(), integer.get());
	}
}
