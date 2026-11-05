package ca.bc.gov.open.jagFileSubmission;

import io.netty.util.concurrent.Future;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentJunitRunner extends BlockJUnit4ClassRunner {
    public ConcurrentJunitRunner(final Class<?> klass) throws InitializationError {
        super(klass);
        setScheduler(new RunnerScheduler() {
            ExecutorService executorService = Executors.newFixedThreadPool(
                    klass.isAnnotationPresent((Class<? extends Annotation>) Concurent.Concurrent.class) ?
                            klass.getAnnotation(Concurent.Concurrent.class).threads() :
                            (int) (Runtime.getRuntime().availableProcessors() * 1.5),
                    new NamedThreadFactory(klass.getSimpleName()));
            CompletionService<Void> completionService = new ExecutorCompletionService<Void>(executorService);
            Queue<Future<Void>> tasks = new LinkedList<Future<Void>>();

            public void schedule(Runnable childStatement) {
                tasks.offer((Future<Void>) completionService.submit(childStatement, null));
            }

            public void finished() {
                try {
                    while (!tasks.isEmpty())
                        tasks.remove(completionService.take());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    while (!tasks.isEmpty())
                        tasks.poll().cancel(true);
                    executorService.shutdownNow();
                }
            }
        });
    }

    static final class NamedThreadFactory implements ThreadFactory {
        static final AtomicInteger poolNumber = new AtomicInteger(1);
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final ThreadGroup group;

        NamedThreadFactory(String poolName) {
            group = new ThreadGroup(poolName + "-" + poolNumber.getAndIncrement());
        }

        public Thread newThread(Runnable r) {
            return new Thread(group, r, group.getName() + "-thread-" + threadNumber.getAndIncrement(), 0);
        }
    }}

