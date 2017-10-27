/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2017 Josh "ShadowLordAlpha"
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
package com.shadowcs.themis.util.concurrent;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;

/**
 * An object that takes already started and provided threads and allows them to be used as part of a thread pool without
 * changing most of the given threads settings. For this to work a thread must first be ceded to the ThreadFactory by
 * calling the cede() method from within the running portion of the thread. After it is ceded the thread waits to be
 * given a runnable to run and then proceeds on with the work queue. If for some reason the thread is returned or stops
 * running it should be returned to the inactive thread pool unless an uncaught IntteruptException occurred in which
 * case the exception will continue up.
 * 
 * @author Josh "ShadowLordAlpha"
 *
 */
public class ProvidedThreadFactory implements ThreadFactory, AutoCloseable {

	private volatile Queue<ThreadWork> queue = new LinkedBlockingQueue<ThreadWork>();
	private volatile Queue<ThreadWork> workQueue = queue;

	public ProvidedThreadFactory() {

	}

	// TODO: document and maybe throw Interrupt as a different type?
	// just a cleaner cede
	public void cede() {
		cede(null);
	}

	/**
	 * Cedes the current thread to the ThreadFactory for use as a Provided Thread. The provided Procedure will be called
	 * after the thread is first added to the thread waiting queue. All this is for is making sure that when a
	 * ProvidedThreadFactory has successfully hijacked a thread another thread is able to start. A Procedure is always
	 * started in a throw away thread. This means that the procedure may go on forever without affecting the provided
	 * thread as it is running on a newly created thread. If the provided thread is interrupted it is not recycled. Instead the
	 * exception is caught and the method is returned. It is assumed that the interrupt is expected behavior. If not simply recalling
	 * cede on the thread (though a loop in the calling method) will basically work the same.
	 * 
	 * @param procedure
	 * @throws InterruptedException
	 */
	public void cede(Runnable procedure) {
		
		ThreadWork work = new ThreadWork();
		new Thread(procedure).start();
		
		while(queue != null && queue.offer(work)) {
			try {
				work.await();
			} catch(InterruptedException e) {
				return;
			}
		}
	}

	public Thread newThread(Runnable r) {
		ThreadWork work = workQueue.poll();
		if(work != null) { return new Thread(() -> work.submit(r)); }

		return null;
	}
	
	@Override
	public void close() {
		
		queue = null;
		workQueue.forEach(work -> work.submit(null));
	}

	private class ThreadWork {

		private Semaphore sema = new Semaphore(0);
		private Runnable rWork;

		public ThreadWork() {

		}

		public void await() throws InterruptedException {
			sema.acquire();
			if(rWork != null) {
				rWork.run();
			}
		}

		public void submit(Runnable r) {
			rWork = r;
			sema.release();
		}
	}
}
