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
package com.shadowcs.themis_old;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * 
 * 
 * @since 0.3.0
 * @author Josh "ShadowLordAlpha"
 *
 */
public class EventManager implements AutoCloseable {
	
	private AsynchronousMode async;
	private ExecutorService pool;
	private LoadingCache<Class<?>, Set<Consumer<?>>> listenerCache;
	
	EventManager(AsynchronousMode async, ExecutorService pool) {
		
		this.async = async;
		this.pool = pool;
		listenerCache = Caffeine.newBuilder().build(key -> ConcurrentHashMap.newKeySet());
		
	}

	public <V> EventManager addListener(Class<V> clazz, Consumer<V> listener) {

		Set<Consumer<?>> list = listenerCache.get(clazz);
		if(list == null) {
			throw new NullPointerException();
		}
		
		list.add(listener);
		
		return this;
	}

	public <V> EventManager removeListener(Class<V> clazz, Consumer<V> listener) {
		
		Set<Consumer<?>> list = listenerCache.get(clazz);
		if(list == null) {
			throw new NullPointerException();
		}
		
		list.remove(listener);
		
		return this;
	}

	
	@SuppressWarnings("unchecked")
	public <E> EventManager executeEvent(E event) {
		return executeEvent((Class<E>) event.getClass(), event);
	}
	
	public <E> EventManager executeEvent(Class<E> clazz, E event) {
		
		submitEvent(clazz, event);
		
		return this;
	}
	
	/**
	 * Simplified Version
	 * 
	 * @param event
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> Future<E> submitEvent(E event) {
		return submitEvent((Class<E>) event.getClass(), event);
	}
	
	/**
	 * 
	 * @param clazz The class of the event
	 * @param event The event to be fired
	 * @return A {@code Future} representing the result of an asynchronous computation. {@code null} is the most common return and does not 
	 * indicate that an error did occur but that there was no {@code Future} to return. This could be due to the {@code AsynchronousMode} or
	 * because of an error.
	 */
	@SuppressWarnings("unchecked")
	public <E> Future<E> submitEvent(Class<E> clazz, E event) {
		
		Set<Consumer<?>> list = listenerCache.get(clazz);
		
		switch(async) {
			case EVENT:
				return pool.submit(() -> list.forEach((listener) -> ((Consumer<E>) listener).accept(event)), event);
			case NONE:
				list.forEach((listener) -> ((Consumer<E>) listener).accept(event));
				return null;
			case TOTAL:
				list.forEach((listener) -> pool.submit(() -> ((Consumer<E>) listener).accept(event)));
				return null;
			default:
				return null;
		}
	}

	@Override
	public void close() throws Exception {
		// TODO: maybe await shutdown?
		pool.shutdown();
	}
}
