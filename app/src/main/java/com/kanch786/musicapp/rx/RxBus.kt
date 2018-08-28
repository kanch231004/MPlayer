package com.kanch786.musicapp.rx
import android.util.Log
import com.kanch786.musicapp.extensions.d
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject

/**
 * Created by root on 9/7/18.
 */
/**
 * Simple Rx Event Bus
 */
object RxBus {
    private val TAG = javaClass.simpleName

    /**
     * Used to hold all subscriptions for Bus events and unsubscribe properly when needed.
     */
    private val subscriptionsMap: HashMap<Any, CompositeDisposable?> by lazy {
        HashMap<Any, CompositeDisposable?>()
    }

    /**
     * Avoid using this property directly, exposed only because it's used in inline fun
     */
    val bus = ReplaySubject.create<Any>()

    val stickyBus = BehaviorSubject.create<Any>()

    /**
     * Sends an event to Bus. Can be called from any thread
     */
    fun send(event: Any) {
        d("event sent ${event.javaClass.simpleName}")
        bus.onNext(event)
    }

    /**
     * Sends sticky event to Bus. Can be called from any thread
     */
    fun sendSticky(event: Any) {
        d("event sent ${event.javaClass.simpleName}")
        stickyBus.onNext(event)
    }

    /**
     * Subscribes for events of certain type T. Can be called from any thread
     */
    inline fun <reified T : Any> observe(): Observable<T> {
        return bus.ofType(T::class.java)
    }


    /**
     * Subscribes for sticky events of certain type T. Can be called from any thread
     */
    inline fun <reified T : Any> observeSticky(): Observable<T> {
        return stickyBus.ofType(T::class.java)
    }
    /**
     * Unregisters subscriber from Bus events.
     * Calls unsubscribe method of your subscriptions
     * @param subscriber subscriber to unregister
     */
    fun unregister(subscriber: Any) {
        d("unregister ${subscriber.javaClass.simpleName}")
        val compositeSubscription = subscriptionsMap[subscriber]
        if (compositeSubscription == null) {
            Log.w(TAG, "Trying to unregister subscriber that wasn't registered")
        } else {
            compositeSubscription.clear()
            subscriptionsMap.remove(subscriber)
        }
    }

    internal fun register(subscriber: Any, subscription: Disposable) {
        var compositeSubscription = subscriptionsMap[subscriber]
        if (compositeSubscription == null) {
            compositeSubscription = CompositeDisposable()
        }
        compositeSubscription.add(subscription)
        subscriptionsMap[subscriber] = compositeSubscription
    }
}

/**
 * Registers the subscription to correctly unregister it later to avoid memory leaks
 * @param subscriber subscriber object that owns this subscription
 */
fun Disposable.registerInBus(subscriber: Any) {
    d("register ${subscriber.javaClass.simpleName}")
    RxBus.register(subscriber, this)
}