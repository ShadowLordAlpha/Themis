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

/**
 * An object that creates new threads on demand with a procedure that is run at some point before the first task takes
 * place.
 * 
 * @author Josh "ShadowLordAlpha"
 *
 */
public class ProcedureThreadFactory extends DefaultThreadFactory {

	private Runnable procedure;
	
	public ProcedureThreadFactory() {
		this(null);
	}

	public ProcedureThreadFactory(Runnable procedure) {
		this.procedure = procedure;
	}

	public Thread newThread(Runnable r) {
		return super.newThread(() -> {
			if(procedure != null) {
				procedure.run();
			}
			if(r != null) {
				r.run();
			}
		});
	}
}
