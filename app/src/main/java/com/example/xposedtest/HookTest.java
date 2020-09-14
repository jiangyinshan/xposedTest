package com.example.xposedtest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;


import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;


import android.widget.TextView;
import dalvik.system.DexFile;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class HookTest implements IXposedHookLoadPackage {
    String yinshan = "com.yinshan.hooktest";
    String kika = "kika.emoji.keyboard.teclados.clavier";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
//        Class<?> a = XposedHelpers.findClass("com.yinshan.hooktest.MainActivity", lpparam.classLoader);
//        System.out.println("xxxxhook开始执行获取包名"+lpparam.packageName);
        if (lpparam.packageName.equals(kika)) {
            System.out.println("xxxxhook开始执行equals");
            XposedHelpers.findAndHookMethod(Application.class, "attach",
                    Context.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ClassLoader cl = ((Context) param.args[0]).getClassLoader();
                            Class<?> hookclass = null;
                            try {
                                hookclass = cl.loadClass("com.qisi.manager.impl.TagMatcherImpl");
                            } catch (Exception e) {
                                Log.e("DEBUG", "xxxxhook load class error", e);
                                return;
                            }
                            Log.i("DEBUG", "xxxxhook load success");
                            XposedHelpers.findAndHookMethod(hookclass, "updateAfterSendTag",String.class,
                                    new XC_MethodHook() {
                                        @Override
                                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                            super.afterHookedMethod(param);
                                            Log.i("DEBUG", "xxxxhook 执行afterHookedMethod");
                                            param.setResult("xxxxhook 成功");
                                        }
                                    });
                        }
                    });
        }
    }
}