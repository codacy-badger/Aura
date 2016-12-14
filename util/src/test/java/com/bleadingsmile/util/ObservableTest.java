package com.bleadingsmile.util;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Larry Hsiao on 2016/11/20.
 */
public class ObservableTest {

    @Test
    public void countObservers() throws Exception {
        Observable<String> observable = new Observable<>();
        int observerCount = observable.countObservers();
        assertEquals(0, observerCount);
    }

    @Test
    public void addObserver() throws Exception {
        Observable<String> observable = new Observable<>();
        observable.addObserver(new Observable.Observer<String>() {
            @Override
            public void update(Observable<? extends String> observer, String arg) {
            }
        });
        assertEquals(1, observable.countObservers());
    }

    @Test
    public void removeObserver() throws Exception {
        Observable<String> observable = new Observable<>();
        Observable.Observer<String> observer = new Observable.Observer<String>() {
            @Override
            public void update(Observable<? extends String> observer1, String arg) {
            }
        };
        observable.addObserver(observer);
        observable.removeObserver(observer);
        assertEquals(0, observable.countObservers());
    }

    @Test
    public void clearObservers() throws Exception {
        Observable<String> observable = new Observable<>();
        Observable.Observer<String> observer = new Observable.Observer<String>() {
            @Override
            public void update(Observable<? extends String> observer1, String arg) {
            }
        };
        observable.addObserver(observer);
        observable.clearObservers();
        assertEquals(0, observable.countObservers());
    }

    @Test
    public void hasChanged() throws Exception {
        Observable<String> observable = new Observable<>();
        assertFalse(observable.hasChanged());
    }

    @Test
    public void setChanged() throws Exception {
        Observable<String> observable = new Observable<>();
        observable.setChanged();
        assertTrue(observable.hasChanged());
    }

    @Test
    public void clearChanged() throws Exception {
        Observable<String> observable = new Observable<>();
        observable.setChanged();
        observable.clearChanged();
        assertFalse(observable.hasChanged());
    }

    @Test
    public void notifyObserversNonParameter() throws Exception {
        Observable<String> observable = new Observable<>();
        final AtomicBoolean hasCallback = new AtomicBoolean(false);
        observable.addObserver(new Observable.Observer<String>() {
            @Override
            public void update(Observable<? extends String> observer, String arg) {
                hasCallback.set(true);
            }
        });
        observable.setChanged();
        observable.notifyObservers();
        assertTrue(hasCallback.get());
    }

    @Test
    public void notifyObserversObjectChange() throws Exception {
        final String expected = "NewString";
        final AtomicReference<String> actualStringReference = new AtomicReference<>("initialValue");
        Observable<String> observable = new Observable<>();
        observable.addObserver(new Observable.Observer<String>() {
            @Override
            public void update(Observable<? extends String> observer, String arg) {
                actualStringReference.set(arg);
            }
        });
        observable.setChanged();
        observable.notifyObservers(expected);
        assertEquals(expected, actualStringReference.get());
    }

    @Test
    public void notifyObservers_notChanged() throws Exception {
        final AtomicBoolean success = new AtomicBoolean(true);
        Observable<String> observable = new Observable<>();
        observable.addObserver((new Observable.Observer<String>() {
            @Override
            public void update(Observable<? extends String> observer, String arg) {
                success.set(false);
            }
        }));
        observable.notifyObservers();
        assertTrue(success.get());
    }

    @Test
    public void addObserver_addMultiTime() throws Exception {
        Observable<String> observable = new Observable<>();
        Observable.Observer<String> observer = new Observable.Observer<String>() {
            @Override
            public void update(Observable<? extends String> observer1, String arg) {
            }
        };
        observable.addObserver(observer);
        observable.addObserver(observer);
        assertEquals(1, observable.countObservers());
    }
}