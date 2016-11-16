package rxworkshop.create;

import rx.Observable;
import rx.schedulers.Schedulers;

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
        single().subscribe(
            value -> System.out.print(", " + value),
            error -> System.out.print(", " + error)
        );

        open()
            .observeOn(Schedulers.io())
            .subscribe(value -> {
                System.out.print(", " + value);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
    }

    static Observable<Long> series() {
        return Observable.from(Arrays.asList(1l, 2l, 3l));
    }

    static Observable<Long> single() {
        return Observable.create(subscriber -> {
            subscriber.onNext(System.currentTimeMillis());
            subscriber.onCompleted();
        });
    }

    static Observable<Long> open() {
        return Observable.create(subscriber -> {
            Timer timer = new Timer(30);
            timer.setListener(new TimerListner() {
                @Override
                public void onNewTime(long time) {
                    subscriber.onNext(time);
                }
            });
        });
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