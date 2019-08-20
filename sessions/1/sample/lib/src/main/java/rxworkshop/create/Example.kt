package rxworkshop.create

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicReference

internal interface TimerListner {
    fun onNewTime(time: Long)
}

// Calls given listener on a background thread at given interval
internal class Timer(val interval: Long) {
    var listenerRef = AtomicReference<TimerListner>()

    var time = 0L

    init {
        Thread {
            while (true) {
                val listener = listenerRef.get()
                listener?.onNewTime(time++)
                while (true) {
                    val listener = listenerRef.get();
                    listener?.onNewTime(time++)
                    try {
                        Thread.sleep(interval);
                    } catch (e: InterruptedException) {
                        break;
                    }
                }
            }
        }.start()
    }

    fun setListener(listener: TimerListner) {
        listenerRef.set(listener)
    }

    fun clearListener() {
        listenerRef.set(null)
    }

}

object Example {
    @JvmStatic
    fun main(args: Array<String>) {
        series() // Read data from the observable
            .take(1000) // Change limit as needed to have enough time in the expriment
            .observeOn(Schedulers.io()) // Change threads to have the subscribe function called in its own thread
            .subscribe { value ->
                try {
                    // Force a delay in reading from the source.
                    // This blocks the calling thread, what happens to the producer?
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                print(", " + value!! % 1000)
            }
    }

    internal fun series(): Observable<Long> {
        // Super simple observable immediately emits 1, 2, 3
        return Observable.fromIterable(listOf(1L, 2L, 3L))
    }

//    internal fun series(): Observable<Long> {
//        return Observable.create<Long> { emitter ->
//            val timer = Timer(10)
//            timer.setListener(object : TimerListner {
//                override fun onNewTime(time: Long) {
//                    emitter.onNext(time)
//                }
//            })
//
//            emitter.setCancellable { timer.clearListener() }
//        }
//    }

//    internal fun series(): Flowable<Long> {
//        return Flowable.create<Long>(
//            { emitter ->
//                val timer = Timer(10)
//                timer.setListener(object : TimerListner {
//                    override fun onNewTime(time: Long) {
//                        emitter.onNext(time)
//                    }
//                })
//
//                emitter.setCancellable { timer.clearListener() }
//            },
//            BackpressureStrategy.DROP)
//    }


}

