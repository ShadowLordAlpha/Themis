package com.shadowcs.themis.util.function;

import java.util.function.Supplier;

public class RepeatSupplier<T> implements Supplier<T> {

	private T value;
	private Supplier<T> supplier;
	
	public RepeatSupplier(Supplier<T> supplier) {
		this.supplier = supplier;
	}
	
	@Override
	public T get() {
		if(value == null) {
			value = supplier.get();
		}
		
		return value;
	}
}
