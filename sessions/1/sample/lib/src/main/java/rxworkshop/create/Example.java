package rxworkshop.create;

import rx.AsyncEmitter;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;


// Exercise 1: Use Obsevable.create() to make an obsevable that returns  current time (System.currentTimeMillis()).
// Exercise 2: Implement observable that keeps emitting time using the Timer class below.
// Exercise 3: Use exmaple with observeOn() and a short interval to trigger a MissingBackpressureException
// Exercise 4: Convert 1, to use fromCallable().
// Exercise 5: Convert 2, to use fromEmittable().
// Exercise 6: Repteat 3 with fromEmittable();

public class Example {

    public static void main(String ... args) {
        Subscription s = open()
            .observeOn(Schedulers.io(), 10)
            .subscribe(value -> {
                System.out.print(", " + value/10%100);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

            });

//        try {
//            Thread.sleep(800);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        s.unsubscribe();
    }

    static Observable<Long> open() {

        Action1<AsyncEmitter<Long>> action = new Action1<AsyncEmitter<Long>>() {

            @Override
            public void call(AsyncEmitter<Long> longAsyncEmitter) {
                Timer timer = new Timer(10);

                TimerListner listner = new TimerListner() {
                    @Override
                    public void onNewTime(long time) {
                        longAsyncEmitter.onNext(time);
                    }
                };

                timer.setListener(listner);

                AsyncEmitter.Cancellable cancellable = new AsyncEmitter.Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        timer.clearListener();
                    }
                };
                longAsyncEmitter.setCancellation(cancellable);
            }
        };

        return Observable.fromEmitter(action, AsyncEmitter.BackpressureMode.DROP);
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
    }

    void setListener(TimerListner listener) {
        listenerRef.set(listener);
        new Thread(() -> {
            while (listenerRef.get() != null) {
                listener.onNewTime(System.currentTimeMillis());
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();
    }

    void clearListener() {
        listenerRef.set(null);
    }

}