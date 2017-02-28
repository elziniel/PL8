package com.towerdefense.game.util;


public class Log {

    private static final String LOG = "[LOG]";
    private static final String DEBUG = "[DBG]";
    private static final String ERROR = "[ERR]";

    public static void l(String tag, String message){
        System.out.println(LOG+" ["+tag+"] "+message);
    }

    public static void e(String tag, String message, Throwable e){
            System.out.println(ERROR+" ["+tag+"] "+message+": "+((e.getMessage() != null)? e.getMessage() : e.getClass().toString()));
    }

    public static void e(String tag, String message){
        System.out.println(ERROR+" ["+tag+"] "+message);
    }

    public static void d(String tag, String message){
        if(Cst.DEBUG)
            System.out.println(DEBUG+" ["+tag+"] "+message);
    }
}
