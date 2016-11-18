# RxJava Workshop

## Session 1: Obsevable.create()
Exercise:

1. Use Obsevable.create() to make an obsevable that returns  current time (System.currentTimeMillis()).
2. Implement observable that keeps emitting time using the Timer class below.
3. Use exmaple with observeOn() and a short interval to trigger a MissingBackpressureException
4. Convert 1, to use fromCallable().
5. Convert 2, to use fromEmitter().
6. Repteat 3 with fromEmittable();

Use following project: [Sample](sessions/1/sample) for the excercises.

* [Solution for 1, 2] (https://github.com/ppiech/rxworkshop/commit/f5fa1c75660ed239f8d23f274938b9ba373567ca)
* [Solution for 3] (https://github.com/ppiech/rxworkshop/commit/5d6fdbbe08bcc9722140432c43428f14fdff8fd1)

## Session 2: State feedback pattern
For background, see video: https://skillsmatter.com/skillscasts/8678-making-fully-reactive-apps-using-advanced-rxjava.

Exercise: Convert the SearchPresenter to use a BehaviorSubject that holds the item list: i.e. the view state.

Use following project: [MyApplication](sessions/2/MyApplication) for the excercises.


