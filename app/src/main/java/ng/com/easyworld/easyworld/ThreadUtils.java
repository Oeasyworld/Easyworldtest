package ng.com.easyworld.easyworld;

/**
 * Created by Oisrael on 3/8/2018.
 */

import android.os.Handler;
import android.os.SystemClock;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;



public class ThreadUtils {
    /**
     * Utility class to be used for checking that a method is called on the correct thread.
     */
    public static class ThreadChecker {
        private Thread thread = Thread.currentThread();
        public void checkIsOnValidThread() {
            if (thread == null) {
                thread = Thread.currentThread();
            }
            if (Thread.currentThread() != thread) {
                throw new IllegalStateException("Wrong thread");
            }
        }
        public void detachThread() {
            thread = null;
        }
    }
    /**
     * Utility interface to be used with executeUninterruptibly() to wait for blocking operations
     * to complete without getting interrupted..
     */
    public interface BlockingOperation {
        void run() throws InterruptedException;
    }
    /**
     * Utility method to make sure a blocking operation is executed to completion without getting
     * interrupted. This should be used in cases where the operation is waiting for some critical
     * work, e.g. cleanup, that must complete before returning. If the thread is interrupted during
     * the blocking operation, this function will re-run the operation until completion, and only then
     * re-interrupt the thread.
     */
    public static void executeUninterruptibly(BlockingOperation operation) {
        boolean wasInterrupted = false;
        while (true) {
            try {
                operation.run();
                break;
            } catch (InterruptedException e) {
                // Someone is asking us to return early at our convenience. We can't cancel this operation,
                // but we should preserve the information and pass it along.
                wasInterrupted = true;
            }
        }
        // Pass interruption information along.
        if (wasInterrupted) {
            Thread.currentThread().interrupt();
        }
    }
    public static boolean joinUninterruptibly(final Thread thread, long timeoutMs) {
        final long startTimeMs = SystemClock.elapsedRealtime();
        long timeRemainingMs = timeoutMs;
        boolean wasInterrupted = false;
        while (timeRemainingMs > 0) {
            try {
                thread.join(timeRemainingMs);
                break;
            } catch (InterruptedException e) {
                // Someone is asking us to return early at our convenience. We can't cancel this operation,
                // but we should preserve the information and pass it along.
                wasInterrupted = true;
                final long elapsedTimeMs = SystemClock.elapsedRealtime() - startTimeMs;
                timeRemainingMs = timeoutMs - elapsedTimeMs;
            }
        }
        // Pass interruption information along.
        if (wasInterrupted) {
            Thread.currentThread().interrupt();
        }
        return !thread.isAlive();
    }
    public static void joinUninterruptibly(final Thread thread) {
        executeUninterruptibly(new BlockingOperation() {
            @Override
            public void run() throws InterruptedException {
                thread.join();
            }
        });
    }
    public static void awaitUninterruptibly(final CountDownLatch latch) {
        executeUninterruptibly(new BlockingOperation() {
            @Override
            public void run() throws InterruptedException {
                latch.await();
            }
        });
    }
    public static boolean awaitUninterruptibly(CountDownLatch barrier, long timeoutMs) {
        final long startTimeMs = SystemClock.elapsedRealtime();
        long timeRemainingMs = timeoutMs;
        boolean wasInterrupted = false;
        boolean result = false;
        do {
            try {
                result = barrier.await(timeRemainingMs, TimeUnit.MILLISECONDS);
                break;
            } catch (InterruptedException e) {
                // Someone is asking us to return early at our convenience. We can't cancel this operation,
                // but we should preserve the information and pass it along.
                wasInterrupted = true;
                final long elapsedTimeMs = SystemClock.elapsedRealtime() - startTimeMs;
                timeRemainingMs = timeoutMs - elapsedTimeMs;
            }
        } while (timeRemainingMs > 0);
        // Pass interruption information along.
        if (wasInterrupted) {
            Thread.currentThread().interrupt();
        }
        return result;
    }
    /**
     * Post |callable| to |handler| and wait for the result.
     */
    public static <V> V invokeUninterruptibly(final Handler handler, final Callable<V> callable) {
        class Result {
            public V value;
        }
        final Result result = new Result();
        final CountDownLatch barrier = new CountDownLatch(1);
        handler.post(new Runnable() {
            @Override public void run() {
                try {
                    result.value = callable.call();
                } catch (Exception e) {
                    throw new RuntimeException("Callable threw exception: " + e);
                }
                barrier.countDown();
            }
        });
        awaitUninterruptibly(barrier);
        return result.value;
    }
    /**
     * Post |runner| to |handler| and wait for the result.
     */
    public static void invokeUninterruptibly(final Handler handler, final Runnable runner) {
        final CountDownLatch barrier = new CountDownLatch(1);
        handler.post(new Runnable() {
            @Override public void run() {
                runner.run();
                barrier.countDown();
            }
        });
        awaitUninterruptibly(barrier);
    }
}
