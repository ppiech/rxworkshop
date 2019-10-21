package rxworkshop.create;


import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.Single;


// Exercise 1: Take series of numbers from series() and apply a simple math function on them
//   (e.g. make them negative).
// Exercise 2: Take the series of numbers emitted by numbers(), and map them as in ex 1.
// Exercise 3: Take the series from numbers() and flatMap them to series emitted by letter(),
//   concatenating the two so that you get output of: "1-a, 1-b, 1-c, ...etc."
// Exercise 4: In excercise above change flatMap to switchMap
// Exercise 5: In excercise above change flatMap to concatMap
// Exercise 6: Create an observable that wraps calls to CallbackApi using create() operator,
//   take series from numbers() and then apply square and squareRoot operations to them
//   using flatMap().

public class Example {

    public static void main(String ... args) {
//        Observable.combineLatest(
//            numbers(),
//            letters(),
//            (number, letter) -> "" + number + "-" + letter)
//            .zipWith(letters(), (number, letter) -> "" + number + "-" + letter)
        numbers()
            .concatMap(value ->
                letters().take(3)
                    .map(letter -> "" + value + "-" + letter))
            .subscribe(value -> System.out.print(", " + value));

        try {
            Thread.sleep(100_000);
        } catch (InterruptedException e) {
        }
    }

    static Observable<Long> series() {
        return Observable.fromIterable(Arrays.asList(1l, 2l, 3l));
    }

    static Single<Long> single() {
        return Single.just(1L);
    }

    static Observable<Long> delayed() {
        return Observable.timer(3, TimeUnit.SECONDS);
    }

    static Observable<Long> numbers() {
        return Observable.interval(1, TimeUnit.SECONDS);
    }

    static Observable<Character> letters() {
        return Observable.interval(1, TimeUnit.SECONDS).map(val -> (char)('a' + val));
    }
}

class CallbackApi {
    public interface Callback {
        void result(double output);
    }

    public void square(double input, Callback callback) {
        new Thread(() -> callback.result(input*input)).start();
    }

    public void squareRoot(double input, Callback callback) {
        new Thread(() -> callback.result(Math.sqrt(input))).start();
    }
}