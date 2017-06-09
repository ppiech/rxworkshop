package rxworkshop.create;

import rx.Observable;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;


// Exercise 1: Use Obsevable.create() to make an obsevable that returns  current time (System.currentTimeMillis()).
// Exercise 2: Implement observable that keeps emitting time using the Timer class below.
// Exercise 3: Use exmaple with observeOn() and a short interval to trigger a MissingBackpressureException
// Exercise 4: Convert 1, to use fromCallable().
// Exercise 5: Convert 2, to use fromEmittable().
// Exercise 6: Repteat 3 with fromEmittable();

public class Example {

    public static void main(String ... args) {
        series().subscribe(value -> System.out.print(", " + value));

        // TODO: start work here


        // Keep the main thread alive to avoid killing our process
        try {
               Thread.sleep(10000);
            } catch (InterruptedException e) {
        }
    }

    static Observable<Long> series() {
        return Observable.from(Arrays.asList(1l, 2l, 3l));
    }

}

interface TimerListner {
    public void onNewTime(long time);
}

class Timer {
    final long interval;
    AtomicReference<TimerListner> listenerRef = new AtomicReference<>();

    Timer(long interval) {
        this.interval = interval;
        new Thread(() -> {
            while (true) {
                TimerListner listener = listenerRef.get();
                if (listener != null) {
                    listener.onNewTime(System.currentTimeMillis());
                }
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();
    }

    void setListener(TimerListner listener) {
        listenerRef.set(listener);
    }

    void clearListener() {
        listenerRef.set(null);
    }

}