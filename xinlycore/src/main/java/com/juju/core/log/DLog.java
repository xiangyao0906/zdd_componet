package com.juju.core.log;


import com.juju.core.utils.SystemUtil;
import sj.mblog.L;

/**
 * 全局Log入口
 * 在当前工程中请不要直接使用 系统的 Log.xxx() 进行log打印. 所有Log请使用当前Log控制器JLog 进行输出
 * JLog的输出引擎为MBLog
 * <p>
 * Created by xiangyao on 2019/8/20.
 */

public class DLog {
    static {
        L.setLastMethodClassName("com.juju.core.log.DLog");
    }

    public static void closeLogXmlParse(){
        L.getPrinter().getLogBuilder().setPrint(L.PRINT.SYSTEM);
    }

    private static boolean check(Object... args) {
        if(args == null || args.length == 0) {
            return false;
        }
        if(!SystemUtil.isCanLog()) {
            return false;
        }
        return true;
    }

    public static void d(Object... args) {
        if(!check(args)) {
            return;
        }
        L.d(args);
    }

    public static void e(Object... args) {
        if(!check(args)) {
            return;
        }
        L.e(args);
    }

    public static void e(Throwable throwable, Object... args) {
        if(!check(args)) {
            return;
        }
        L.e(throwable, args);
    }

    public static void w(Object... args) {
        if(!check(args)) {
            return;
        }
        L.w(args);
    }

    public static void i(Object... args) {
        if(!check(args)) {
            return;
        }
        L.i(args);
    }

    public static void v(Object... args) {
        if(!check(args)) {
            return;
        }
        L.v(args);
    }

    public static void wtf(Object... args) {
        if(!check(args)) {
            return;
        }
        L.wtf(args);
    }
}
