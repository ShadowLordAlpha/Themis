package com.shadowcs.themis.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import com.shadowcs.themis.util.concurrent.DefaultThreadFactory;
import com.shadowcs.themis.util.concurrent.ProcedureThreadFactory;
import com.shadowcs.themis.util.concurrent.ProvidedThreadFactory;
import com.shadowcs.themis.util.concurrent.SimpleThreadFactory;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class CucumberStepDef {

	private AtomicInteger aInt = new AtomicInteger();
	private ThreadFactory tFactory = Executors.defaultThreadFactory();
	private ExecutorService exService;

	@Given("^Jeff uses the \"(DEFAULT|PROCEDURE|PROVIDED|SIMPLE)\" thread factory\\.$")
	public void createThreadFactory(ThreadFactoryType tfType) throws Throwable {

		switch(tfType) {
			case DEFAULT:
				tFactory = new DefaultThreadFactory();
				break;
			case PROCEDURE:
				tFactory = new ProcedureThreadFactory(null);
				break;
			case PROVIDED:
				tFactory = new ProvidedThreadFactory();
				Semaphore sema = new Semaphore(0);
				new Thread(() -> {
					try {
						((ProvidedThreadFactory) tFactory).cede(() -> sema.release(1));
					} catch(InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}).start();
				
				// YES, you do have to wait for the factory to be ready!
				// could be done though time but this is more reliable and less time consuming most of the time
				// though the procedure will run in its own thread not connected to an executor so you could also
				// continue a main program thread that way or like I do here
				try {
					sema.acquire();
					// System.out.println("work thread ceded");
				} catch(InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			case SIMPLE:
				tFactory = new SimpleThreadFactory();
				break;
			default:
				throw new RuntimeException("Unknown Thread Factory!");

		}
	}

	@Given("^Jeff creates a new \"(SINGLE|FIXED|CACHED)\" thread executor\\.$")
	public void createExecutor(ExecutorType exType) throws Throwable {

		switch(exType) {
			case CACHED:
				exService = Executors.newCachedThreadPool(tFactory);
				break;
			case FIXED:
				exService = Executors.newFixedThreadPool(25, tFactory);
				break;
			case SINGLE:
				exService = Executors.newSingleThreadExecutor(tFactory);
				break;
			default:
				throw new RuntimeException("Unhandled Executor Type");
		}
	}

	@Given("^Jeff executes (\\d+) basic test tasks\\.$")
	public void executeBasicTestTask(int arg1) throws Throwable {

		for(int i = 0; i < arg1; i++) {
			exService.execute(() -> aInt.incrementAndGet());
		}
		
		exService.shutdown();
		if(!exService.awaitTermination(2, TimeUnit.MINUTES)) {
			throw new RuntimeException("Termination Timeout!");
		}
	}

	@Then("^Jeff should have a value of (\\d+)$")
	public void checkValue(int arg1) throws Throwable {
		Assert.assertEquals(arg1, aInt.get());
	}

	private enum ExecutorType {
		SINGLE,
		FIXED,
		CACHED;
	}

	private enum ThreadFactoryType {
		DEFAULT,
		PROCEDURE,
		PROVIDED,
		SIMPLE;
	}
}
