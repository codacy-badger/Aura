package com.bleadingsmile.util.template;

import com.bleadingsmile.util.Observable;

import java.util.ArrayList;
import java.util.List;

public class LoadingRegistry extends Observable<Boolean> {
    private static LoadingRegistry loadingRegistry;

    public static LoadingRegistry getLoadingRegistry() {
        if (loadingRegistry == null) {
            loadingRegistry = new LoadingRegistry();
        }
        return loadingRegistry;
    }

    private final List<Object> loadingObjects;

    private LoadingRegistry() {
        this.loadingObjects = new ArrayList<>();
    }

    public void registerLoading(Object object) {
        if (object == null){
            return;
        }
        loadingObjects.add(object);
        if (loadingObjects.size() == 1) {
            notifyLaodingStarts();
        }
    }

    public void unRegisterLoading(Object object) {
        if (!loadingObjects.contains(object)){
            return;
        }
        loadingObjects.remove(object);
        if (loadingObjects.size() == 0){
            notifyLoadingFinish();
        }
    }

    public void clearLoading(){
        if (loadingObjects.size() == 0){
            return;
        }
        loadingObjects.clear();
        notifyLoadingFinish();
    }

    private void notifyLoadingFinish() {
        setChanged();
        notifyObservers(false);
    }

    private void notifyLaodingStarts() {
        setChanged();
        notifyObservers(true);
    }

    public boolean hasLoading() {
        return loadingObjects.size() > 0;
    }
}