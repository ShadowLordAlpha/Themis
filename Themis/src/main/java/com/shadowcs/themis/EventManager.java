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

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * The EventManager class follows the Event -> Listener model where when an event is fired its listeners are invoked
 * and deal with it.
 * 
 * @version 1.0.0
 * @author Josh "ShadowLordAlpha"
 *
 */
public class EventManager implements AutoCloseable {
	
	protected ExecutorService threadPool;
	protected Map<Class<?>, List<Consumer<?>>> listenerMap;
	
	/**
	 * Creates a new EventManager. Default EventManagers are fully separate from each other and do not share resources.
	 */
	public EventManager() {
		threadPool = Executors.newCachedThreadPool();
		listenerMap = new ConcurrentHashMap<Class<?>, List<Consumer<?>>>();
	}
	
	public <T> void addListener(Class<T> clazz, Consumer<T> listener) {
		
		List<Consumer<?>> listenerList = listenerMap.get(clazz);
		if(listenerList == null) {
			listenerList = new Vector<Consumer<?>>(); // Might actually be able to make due with a simple list
			listenerMap.put(clazz, listenerList);
		}
		
		listenerList.add(listener);
	}
	
	public <T> Future<?> addListenerAsync(Class<T> clazz, Consumer<T> listener) {
		
		return threadPool.submit(() -> addListener(clazz, listener));
	}
	
	public <T> void removeListener(Class<T> clazz, Consumer<T> listener) {
		
		List<Consumer<?>> listenerList = listenerMap.get(clazz);
		if(listenerList != null) {
			listenerList.remove(listener);
			if(listenerList.isEmpty()) {
				listenerMap.remove(clazz);
			}
		}
	}
	
	public <T> Future<?> removeListenerAsync(Class<T> clazz, Consumer<T> listener) {
		
		return threadPool.submit(() -> removeListener(clazz, listener));
	}
	
	public <T> void fireEvent(T event) {
		
		// Annoying cast here because of generics stuff
		List<Consumer<?>> listenerList = listenerMap.get(event.getClass());
		if(listenerList != null) {
			listenerList.forEach(consumer -> ((Consumer<T>) consumer).accept(event));
		}
	}
	
	public <T> Future<?> fireEventAsync(T event) {
		return threadPool.submit(() -> fireEvent(event));
	}

	@Override
	public void close() {
		
		// in order to properly close and not wait 20 to 30 seconds or so we need to shut down and clean up objects
		listenerMap.clear();
		// we might actually need some of this information
		threadPool.shutdown();
	}
}
