package com.shadowcs.themis;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

public class EventManager {

	private ExecutorService eService;
	private LoadingCache<Class<?>, Collection<Consumer<?>>> lCache;

	// TODO: eventually allow a custom cache or something if I replace caffeine
	public EventManager(ExecutorService eService) {
		this.eService = eService;
		lCache = Caffeine.newBuilder().build(key -> ConcurrentHashMap.newKeySet());
	}

	public <V> EventManager addListener(Consumer<V> listener, Class<V> klass) {
		lCache.get(klass).add(listener);
		return this;
	}

	public <V> EventManager removeListener(Consumer<V> listener, Class<V> klass) {
		lCache.getIfPresent(klass).remove(listener);
		return this;
	}

	// TODO: Submit, returns a future
	public <T> Future<T> submit(T event) {
		
		return eService.submit(() -> event(event), event);
	}

	// TODO: Execute, returns nothing
	public <T> void execute(T event) {

		eService.execute(() -> event(event));
	}

	// TODO: Invoke, blocking run on current thread
	public <T> void invoke(T event) {
		event(event);
	}

	private <T> void event(T event) {
		// this should be working
		lCache.asMap().forEach((key, col) -> {
			if(key.isInstance(event)) {
				col.forEach((listener) -> ((Consumer<T>) listener).accept(event)); // unsafe but should be valid and I
																					// really don't have a better way
			}
		});
	}

	public static void main(String[] args) {
		
	}
}
