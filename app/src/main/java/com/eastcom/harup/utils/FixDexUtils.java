package com.eastcom.harup.utils;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;


import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class FixDexUtils {
	private static HashSet<File> loadedDex = new HashSet<File>();
	
	static{
		loadedDex.clear();
	}

	public static void loadFixedDex(Context context){
		if(context == null){
			return ;
		}
		//遍历所有的修复的dex
		File fileDir = context.getDir("odex",Context.MODE_PRIVATE);
		File[] listFiles = fileDir.listFiles();
		for(File file:listFiles){
			if(file.getName().startsWith("classes2")&&file.getName().endsWith(".dex")){
				loadedDex.add(file);//存入集合
			}
		}
		//dex合并之前的dex
		doDexInject(context,fileDir,loadedDex);
	}

	private static void setField(Object obj,Class<?> cl, String field, Object value) throws Exception {
		Field localField = cl.getDeclaredField(field);
		localField.setAccessible(true);
		localField.set(obj,value);
	}

	private static void doDexInject(final Context appContext, File filesDir,HashSet<File> loadedDex) {
		String optimizeDir = filesDir.getAbsolutePath()+File.separator+"optimize_dex";

		File fopt = new File(optimizeDir);
		if(!fopt.exists()){
			fopt.mkdirs();
		}
		//1.加载应用程序的dex
		try {
			PathClassLoader pathLoader = (PathClassLoader) appContext.getClassLoader();

			for (File dex : loadedDex) {
				//2.加载指定的修复的dex文件。
				DexClassLoader classLoader = new DexClassLoader(
						dex.getAbsolutePath(),//String dexPath,
						fopt.getAbsolutePath(),//String optimizedDirectory,
						null,//String libraryPath,
						pathLoader//ClassLoader parent
				);
				//3.合并
				Object dexObj = getPathList(classLoader);
				Object pathObj = getPathList(pathLoader);
				Object mDexElementsList = getDexElements(dexObj);
				Object pathDexElementsList = getDexElements(pathObj);
				//合并完成
				Object dexElements = combineArray(mDexElementsList,pathDexElementsList);
				//重写给PathList里面的lement[] dexElements;赋值
				Object pathList = getPathList(pathLoader);
				setField(pathList,pathList.getClass(),"dexElements",dexElements);
                Toast.makeText(appContext,"修复成功",Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private static Object getField(Object obj, Class<?> cl, String field)
			throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field localField = cl.getDeclaredField(field);
		localField.setAccessible(true);
		return localField.get(obj);
	}
	private static Object getPathList(Object baseDexClassLoader) throws Exception {
			return getField(baseDexClassLoader,Class.forName("dalvik.system.BaseDexClassLoader"),"pathList");
	}

	private static Object getDexElements(Object obj) throws Exception {
			return getField(obj,obj.getClass(),"dexElements");
	}

	/**
	 * 两个数组合并
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
//	[12345] [9876]
//	[9876  12345]

}
