package com.bleadingsmile.util.template;

import com.bleadingsmile.util.Observable;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Larry Hsiao on 2016/12/7.
 */
public class LoadingRegistryTest {
    @Test
    public void registerLoading_initialize() throws Exception {
        LoadingRegistry loadingRegistry = LoadingRegistry.getLoadingRegistry();
        assertFalse(loadingRegistry.hasLoading());
    }

    @Test
    public void registerLoading() throws Exception {
        Object object = "This is Loading object";
        LoadingRegistry loadingRegistry = LoadingRegistry.getLoadingRegistry();
        loadingRegistry.clearLoading();
        loadingRegistry.registerLoading(object);
        assertTrue(loadingRegistry.hasLoading());
    }

    @Test
    public void registerLoading_clear() throws Exception {
        Object object= "This is loading object";
        LoadingRegistry loadingRegistry = LoadingRegistry.getLoadingRegistry();
        loadingRegistry.registerLoading(object);
        loadingRegistry.clearLoading();
        assertFalse(loadingRegistry.hasLoading());
    }

    @Test
    public void unRegisterLoading() throws Exception {
        Object object = "This is loading object";
        LoadingRegistry loadingRegistry = LoadingRegistry.getLoadingRegistry();
        loadingRegistry.clearLoading();
        loadingRegistry.registerLoading(object);
        loadingRegistry.unRegisterLoading(object);
        assertFalse(loadingRegistry.hasLoading());
    }

    @Test
    public void notifyLoadingStarts() throws Exception {
        final AtomicBoolean notifiesLoadingStarts = new AtomicBoolean(false);
        LoadingRegistry loadingRegistry = LoadingRegistry.getLoadingRegistry();
        loadingRegistry.clearLoading();
        loadingRegistry.addObserver(new Observable.Observer<Boolean>() {
            @Override
            public void update(Observable<? extends Boolean> observer, Boolean arg) {
                notifiesLoadingStarts.set(arg);
            }
        });

        Object loadingObject = "This is loading object";
        loadingRegistry.registerLoading(loadingObject);
        assertTrue(notifiesLoadingStarts.get());
    }

    @Test
    public void notifyLoadingFinish() throws Exception {
        Object loadingObject= "This is LoadingObject";
        final AtomicBoolean notifyLoadingFinish = new AtomicBoolean(true);
        LoadingRegistry loadingRegistry= LoadingRegistry.getLoadingRegistry();
        loadingRegistry.clearLoading();
        loadingRegistry.registerLoading(loadingObject);
        loadingRegistry.addObserver(new Observable.Observer<Boolean>() {
            @Override
            public void update(Observable<? extends Boolean> observer, Boolean arg) {
                notifyLoadingFinish.set(arg);
            }
        });
        loadingRegistry.unRegisterLoading(loadingObject);
        assertFalse(notifyLoadingFinish.get());
    }
}