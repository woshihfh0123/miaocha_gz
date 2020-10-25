package com.picture.android.util;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;


/**
 *
 * @author WWL
 * 单例实现Retrofit对象
 */



public enum Api {
    INSTANCE;
    private Map<String ,Disposable> disposableMap;
    Api() {
        disposableMap=new HashMap<>();

    }



    public synchronized void addDisposable(String key, Disposable disposable){
        disposableMap.put(key,disposable);
    }



    public synchronized void removeDisposable(String key){
        if (disposableMap.containsKey(key)) {
            disposableMap.remove(key);
        }else {
//            Logger.e("移除Disposable失败，请检查key是否存在");
        }
    }

}
