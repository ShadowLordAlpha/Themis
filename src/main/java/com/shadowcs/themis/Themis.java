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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A builder of {@link EventManager} instances having some set of the following features
 * <ul>
 * <li>Multiple {@link AsynchronousMode} options for threading or lack thereof
 * <li>Specified {@link ExecutorService} thread pool
 * </ul>
 * <p>
 * <b>Usage Examples</b>
 * 
 * <pre>
 * {@code Themis.newBuilder()
 * 	.setAsyncEvents(AsynchronousMode.EVENT)
 * 	.setExecutorService(Executors.newCachedThreadPool())
 * 	.build();}
 * </pre>
 * 
 * @version 0.3.0
 * @author Josh "ShadowLordAlpha"
 *
 */
public class Themis {

	private AsynchronousMode async = AsynchronousMode.EVENT;
	private ExecutorService executorServicer;

	private Themis() {}

	/**
	 * Constructs a new {@code Themis} instance with default settings, including asynchronous event throwing, and a
	 * cached thread pool.
	 *
	 * @return A new instance with default settings
	 * @see Themis
	 */
	public static Themis newBuilder() {
		return new Themis();
	}

	/**
	 * Set the {@link AsynchronousMode} used by an {@link EventManager}.
	 * 
	 * @param async The {@link AsynchronousMode} that should be used by the event manager
	 * @return this builder instance
	 */
	public Themis setAsyncEvents(AsynchronousMode async) {
		this.async = async;
		return this;
	}

	AsynchronousMode getAsyncEvents() {
		return this.async;
	}

	/**
	 * 
	 * 
	 * @param executorServicer The {@link ExecutorService} that will be used to handle asynchronous running of the
	 *            different events as needed.
	 * @return this builder instance
	 */
	public Themis setExecutorService(ExecutorService executorServicer) {
		this.executorServicer = executorServicer;
		return this;
	}

	ExecutorService getExecutorSerivce() {
		return (this.executorServicer == null) ? Executors.newCachedThreadPool() : this.executorServicer;
	}

	/**
	 * Builds an {@code EventManager} that can be used to throw events and add listeners to events that get thrown
	 * though that specific {@code EventManager}.
	 * 
	 * <p>
	 * This method does not alter the state of a {@code Themis} instance, so it can be invoked again to create multiple
	 * independent {@code EventManager}s
	 * 
	 * @return An {@code EventManager} with the requested features.
	 * @see EventManager
	 */
	public EventManager build() {
		return new EventManager(getAsyncEvents(), getExecutorSerivce());
	}
}
