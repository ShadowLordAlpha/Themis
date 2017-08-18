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
}
