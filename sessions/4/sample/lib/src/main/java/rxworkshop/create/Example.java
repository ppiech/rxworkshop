package rxworkshop.create;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


public class Example {

    public static void main(String ... args) {

        withRetry();

        //withFlatMap();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static AtomicLong atomicInteger = new AtomicLong(1);

    static Observable<Long> threeTimesACharm() {

        return Observable.fromCallable(() -> {
            System.out.println("trying: " + atomicInteger.get() + ", in: " + Thread.currentThread().getId());

            Thread.sleep(3000);
            if (atomicInteger.getAndIncrement()%3 == 0){
                System.out.println("Increment and check " + atomicInteger.get());
                return atomicInteger.get();
            }
            else {
                System.out.println("Error here!!! :"+System.currentTimeMillis());
                throw new IllegalArgumentException(); }
        });
    }

    static void withRetry() {
        threeTimesACharm()
            .retryWhen(errorObservable -> errorObservable
                .delay(500, TimeUnit.MILLISECONDS, Schedulers.io())
                .take(3))
            .subscribeOn(Schedulers.io())
            .doOnCompleted(() -> System.out.println("Compl"))
            .subscribe(s -> {
                System.out.println("Subscriber "+s);
            });
    }
}
