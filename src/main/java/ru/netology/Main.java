package ru.netology;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Main {

    final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS ");
    static volatile Boolean volatileSwitch = false;

    private static final int USER_COUNT_ITERATIONS = 5;
    private static final long USER_SWITCH_INTERVAL = 2000;

    public static void main(String[] args) throws InterruptedException {

        Thread threadToy = new Thread(null, () -> {
            while (!Thread.interrupted()){
                while (volatileSwitch){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    volatileSwitch = false;
                    myLog(Thread.currentThread().getName() + ": Выключение тумблера");
                }
            }
        }, "Toy");

        Thread threadUser = new Thread(null, () -> {
            for (int i=1; i <= USER_COUNT_ITERATIONS; i++){
                try {
                    Thread.sleep(USER_SWITCH_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!volatileSwitch){

                    volatileSwitch = true;
                    myLog(Thread.currentThread().getName() + ": Включение тумблера");
                }
                else myLog(Thread.currentThread().getName() + ": тумблер еще включен");
            }
        }, "User");

        myLog("Ночальное положение тумблера: " + getState(volatileSwitch));
        threadToy.start();
        threadUser.start();

        threadUser.join();
        myLog("Поток-пользователя завершен");
        threadToy.interrupt();
        threadToy.join();
        myLog("Поток-игрушка завершен");
        myLog("Конечное положение тумблера: " + getState(volatileSwitch));
    }

    private static String getState(boolean volatileSwitch) {
        return volatileSwitch ? "Вкл" : "Выкл";
    }

    public static void myLog(String s){
        System.out.println(simpleDateFormat.format(new Date()) + s);
    }
}
