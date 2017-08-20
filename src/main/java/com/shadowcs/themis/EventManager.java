package com.shadowcs.themis;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class EventManager {

	private ExecutorService eService;

	EventManager(ExecutorService eService) {
		this.eService = eService;
	}

	public <V> EventManager addListener(Consumer<V> listener, Class<V> klass) {
		
		return this;
	}

	public <V> EventManager removeListener(Consumer<V> listener, Class<V> klass) {
		
		return this;
	}
	
	// TODO: Submit, returns a future
	
	// TODO: Execute, returns nothing
	
	// TODO: Invoke, blocking run on current thread
	
	private class bogus<T> {
		
	}
}
