package com.eastcom.harup.utils;

import android.content.Context;
import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class FixUtil {
    static HashSet<File> fileSets = new HashSet<>();
    static String optPath = "opt";

    static {
        fileSets.clear();
    }

    public static void fix(Context context) {
        loadDex(context);
        File optFile = new File(context.getDir("opt", Context.MODE_PRIVATE), "opt");
        optFile.mkdirs();
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();

        for (File f : fileSets) {
            DexClassLoader dexClassLoader = new DexClassLoader(f.getPath(), optFile.getPath(), null, pathClassLoader);
            Object dexList = null;
            try {
                dexList = getPathList(dexClassLoader);
                Object pathList = getPathList(pathClassLoader);
                Object dexElements = getFiled(dexList.getClass(), "dexElements", dexList);
                Object pathElements = getFiled(pathList.getClass(), "dexElements", dexList);
                Object o = combineArray(dexElements, pathElements);
                setFiled("dexElements", pathList, o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadDex(Context context) {
        File file = context.getDir("odex", Context.MODE_PRIVATE);
        if (file.exists()) {
            file.delete();
        }
        file.mkdir();

        File outer = new File(Environment.getExternalStorageDirectory().getPath(), "dex");
        if (!outer.exists()) {
            Toast.makeText(context, "未找到修复包", Toast.LENGTH_SHORT).show();
            return;
        }

        FileUtil.copyDir(outer.getPath(), file.getPath());
    }

    private static Object getPathList(Object baseDexClassLoader) throws Exception {
        return getFiled(Class.forName("dalvik.system.BaseDexClassLoader"), "pathList", baseDexClassLoader);
    }

    public static Object getFiled(Class<?> cls, String fieldName, Object obj) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFiled(String fieldName, Object obj, Object value) {
        try {
            Field field = obj.getClass().getField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 两个数组合并
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }

        return result;
    }
}
