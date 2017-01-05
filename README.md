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
* [Solution for 5] (https://github.com/ppiech/rxworkshop/commit/0a24956908944286ec9e2409491c7bc2fa40043d)
* [Solution for 6] (https://github.com/ppiech/rxworkshop/commit/228c3d7b1200c114fb8176144e05db338f8ce8a5)

## Session 2: State feedback pattern
For background, see video: https://skillsmatter.com/skillscasts/8678-making-fully-reactive-apps-using-advanced-rxjava.

Exercise: Convert the SearchPresenter to use a BehaviorSubject that holds the item list: i.e. the view state.

Use following project: [MyApplication](sessions/2/MyApplication) for the excercises.

## Session 3: map, flatMap, switchMap, concatMap
Use differnt variants of transformation operators to manipulate data streams

## Session 4: retryWhen
retryWhen operator allows a caller to specify how many times to retry subscribing to a stream upon error, and what to do when retries fail.  As an excercise implement retryWhen logic using a flatMap operator

[Solution] (https://github.com/ppiech/rxworkshop/commit/f4a11085fd2d6b2eb208fb4f6cc9ab40ecc5a0dc)

# Session ideas
* Connectable observables
* Observable.combineLatest()
* Subjects antipatterns (http://tomstechnicalblog.blogspot.com/2016/03/rxjava-problem-with-subjects.html) 
* doOnNext()
* Operators that are not compatible with hot observables: reduce(), max(), etc.
* Rxjava 2
* Writing Tests
* Practical work-related examples: 
 * need specifics

