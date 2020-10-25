package com.bs.xyplibs.utils;



import com.bs.xyplibs.bean.EventBean;
import com.bs.xyplibs.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2018/6/8.
 */

public  class EventBusUtil {
    public  static void register(Object subscriber) {
        EventBus eventBus=EventBus.getDefault();
        if(!eventBus.isRegistered(subscriber)){
            eventBus.register(subscriber);
        }
    }

    public static void unregister(Object subscriber) {
        EventBus eventBus=EventBus.getDefault();
        if(eventBus.isRegistered(subscriber)){
            eventBus.unregister(subscriber);
        }
    }

    /**
     * sendEvent
     * sendStickyEvent
     * 已废弃，建议使用
     * postEvent
     * postStickyEvent
     * @param event
     */
    @Deprecated
    public static void sendEvent(MessageEvent event) {

        EventBus.getDefault().post(event);
    }
    @Deprecated
    public static void sendStickyEvent(MessageEvent event) {
        EventBus.getDefault().postSticky(event);
    }
    public static void postEvent(EventBean event) {
        EventBus.getDefault().post(event);
    }
    public static void postStickyEvent(EventBean event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     *
     * @param event
     * EventBus3.0有四种线程模型，分别是：

    POSTING (默认) 表示事件处理函数的线程跟发布事件的线程在同一个线程。
    MAIN 表示事件处理函数的线程在主线程(UI)线程，因此在这里不能进行耗时操作。
    BACKGROUND 表示事件处理函数的线程在后台线程，因此不能进行UI操作。如果发布事件的线程是主线程(UI线程)，那么事件处理函数将会开启一个后台线程，如果果发布事件的线程是在后台线程，那么事件处理函数就使用该线程。
    ASYNC 表示无论事件发布的线程是哪一个，事件处理函数始终会新建一个子线程运行，同样不能进行UI操作。

    作者：唱着歌的猫
    链接：https://www.jianshu.com/p/f9ae5691e1bb
    來源：简书
    简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
     */
    /*
    @Subscribe
    @Subscribe(threadMode = ThreadMode.MAIN)
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    @Subscribe(threadMode = ThreadMode.ASYNC)
//注意处理时间的线程选择
    @Subscribe
    public void getEvent(EventBean event){
        switch(event.getCode()){
            case "class_method_msg":
                String firstObj = (String)event.getFirstObject();
                UserBean secondObj = (UserBean) event.getSecondObject();
                String thirdObj = (String)event.getThirdObject();
                String fourthObj = (String)event.getFourthObject();

                break;
            default:

                break;
        }
    };
 @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    */

}

