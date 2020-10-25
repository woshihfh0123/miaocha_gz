package com.bs.xyplibs.utils;

import java.util.LinkedList;

/**
 * Created by Administrator on 2018/12/8.
 */

public class MyQueueUtils {
    private static LinkedList list=new LinkedList();
    public void clear(){
        list.clear();
    }
    public static boolean QueueEmpty(){
        return list.isEmpty();
    }
    public static void enQueue(Object o){
        list.addLast(o);
    }
    public static Object deQueue(){
        if(!list.isEmpty()){
            return list.removeFirst();
        }
        return "";
    }
    public static int QueueLength(){
        return list.size();
    }
    public static Object QueuePeek(){
        return list.getFirst();
    }

}
