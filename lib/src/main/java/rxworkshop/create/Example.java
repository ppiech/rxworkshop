package rxworkshop.create;

import rx.Observable;

import java.util.Arrays;

public class Example {

    public static void main(String ... args) {
        series().subscribe(value -> System.out.print(", " + value));
    }


    static Observable<Integer> series() {
        return Observable.from(Arrays.asList(1, 2, 3));
    }


}
