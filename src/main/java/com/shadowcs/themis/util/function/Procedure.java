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
package com.shadowcs.themis.util.function;

import java.util.Objects;

/**
 * Represents an operation that accepts no input arguments and returns no result. Unlike most other functional
 * interfaces, {@code Procedure} is expected to operate via side-effects.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a> whose functional method is {@link #invoke()}.
 */
@FunctionalInterface
public interface Procedure {

	/**
	 * Performs this procedure.
	 */
	void invoke();

	/**
	 * Returns a composed {@code Procedure} that performs, in sequence, this operation followed by the {@code after}
	 * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
	 * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
	 *
	 * @param after the operation to perform after this operation
	 * @return a composed {@code Procedure} that performs in sequence this operation followed by the {@code after}
	 *         operation
	 * @throws NullPointerException if {@code after} is null
	 */
	default Procedure andThen(Procedure after) {
		Objects.requireNonNull(after);
		return () -> {
			invoke();
			after.invoke();
		};
	}
}
