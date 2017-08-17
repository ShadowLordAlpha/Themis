/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2017 ShadowLordAlpha
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package com.shadowcs.themis;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@code ThreadFactory} designed to take an already started thread or threads and, after control of the threads have
 * been surrendered, use the provided threads in place of creating a new threads on demand.
 * <p>
 * <b>Warning</b><br>
 * It is not recommended to use this ThreadFactory as the proper use of the ThreadFactory methods and threads is not
 * followed. It is highly preferred that a normal ThreadFactory is used that creates new Threads instead of attempting
 * to repurpose old already running threads. This exists for use cases where a library requires use of a thread that may
 * already be running, such as the main thread of an application.
 * 
 * @author Josh "ShadowLordAlpha"
 *
 */
public class ProvidedThreadFactory implements ThreadFactory {

	private static AtomicInteger wrapperCount = new AtomicInteger(0);

	private AtomicInteger threadCount = new AtomicInteger(0);
	private Semaphore sema = new Semaphore(0);
	private Semaphore confirmSema = new Semaphore(0);
	private Queue<Runnable> q = new ConcurrentLinkedQueue<Runnable>();

	public ProvidedThreadFactory() {

	}

	/**
	 * Surrender control of the thread that runs this method. This method should not return as the thread that runs this
	 * method will be held until it is requested for work and then repurposed for that work. If it is returned something
	 * probably went wrong.
	 */
	public void cede() {
		threadCount.incrementAndGet();
		confirmSema.release();
		try {
			while(true) {
				sema.acquire();

				q.poll().run();
				// We broke out for some reason so recycle the thread into the pool
				threadCount.incrementAndGet();
			}

		} catch(InterruptedException e) {

		}
	}
	
	// Tells when this class has obtained at least one thread
	public void isReady() {
		try {
			confirmSema.acquire();
			confirmSema.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Thread newThread(Runnable r) {
		if(threadCount.getAndDecrement() < 0) {
			threadCount.incrementAndGet(); // Increment the value to bring it back to 0
			return null; // request to repurpose thread was rejected so return null
		}
		
		// TODO: set priorities and other thread properties?

		// A thread is available to be repurposed
		return new Thread(() -> {
			q.offer(r);
			sema.release();
		}, "wrapper-" + wrapperCount.incrementAndGet());
	}
}
