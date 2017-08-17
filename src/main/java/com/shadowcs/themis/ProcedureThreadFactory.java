package com.shadowcs.themis;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import com.shadowcs.themis.util.function.Procedure;

/**
 * A ThreadFactory that operates nearly identically to the default factory provided in the Executor class. The only
 * difference is that this factory takes in a procedure that is run before any other job is taken on the thread and then
 * runs the first worker. This allows things that must be made current on a thread to still be run using an
 * ExecutorService and not having to constantly reset that it is active on a thread. This is useful for using native
 * libraries like OpenAL that do require this to properly function.
 * 
 * @author Josh "ShadowLordAlpha"
 *
 */
public class ProcedureThreadFactory implements ThreadFactory {

	private static final AtomicInteger poolNumber = new AtomicInteger(1);
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String namePrefix;
	private Procedure procedure;

	public ProcedureThreadFactory(Procedure procedure) {
		this.procedure = procedure;
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, () -> {
			procedure.invoke();
			r.run();
		}, namePrefix + threadNumber.getAndIncrement(), 0);
		if(t.isDaemon()) t.setDaemon(false);
		if(t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);
		return t;
	}
}
