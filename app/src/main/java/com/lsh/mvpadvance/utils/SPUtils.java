package com.lsh.mvpadvance.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * SharedPreferences辅助类
 * Created by LSH on 2015/12/2.
 */
public class SPUtils {

    public static final String TOKEN_ID = "tokenid";

    /**
     * 私有构造无法实例化
     */
    private SPUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static final String FILE_NAME = "zxgroup_data";

    /**
     * 保存数据，根据数据的类型调用不同方法保存
     *
     * @param context   上下文引用
     * @param key       数据存储的键
     * @param o         保存的数据
     * @param shareName 本组件的配置文件名
     */
    public static void put(Context context, String key, Object o, String shareName) {
        SharedPreferences sp = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (o instanceof String) {
            editor.putString(key, (String) o);
        } else if (o instanceof Integer) {
            editor.putInt(key, (Integer) o);
        } else if (o instanceof Boolean) {
            editor.putBoolean(key, (Boolean) o);
        } else if (o instanceof Float) {
            editor.putFloat(key, (Float) o);
        } else if (o instanceof Long) {
            editor.putLong(key, (Long) o);
        } else {
            editor.putString(key, (String) o);
        }
        /**
         * 在一个进程中，sharedPreference是单实例，一般不会出现并发冲突，
         * 如果对提交的结果不关心的话，建议使用apply（效率高），
         * 当然需要确保提交成功且有后续操作的话，
         * 还是需要用commit的（效率低）。
         */
        SharedPreferencesCompat.apply(editor);

    }

    /**
     * 获取保存的数据的方法 ，根据传入数据的类型，然后调用相关方法获取数据
     *
     * @param context   上下文对象
     * @param key       数据的键
     * @param o         数据类型
     * @param shareName 配置文件名
     * @return
     */
    public static Object get(Context context, String key, Object o, String shareName) {
        SharedPreferences sp = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        if (o instanceof String) {
            return sp.getString(key, (String) o);
        } else if (o instanceof Integer) {
            return sp.getInt(key, (Integer) o);
        } else if (o instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) o);
        } else if (o instanceof Float) {
            return sp.getFloat(key, (Float) o);
        } else if (o instanceof Long) {
            return sp.getLong(key, (Long) o);
        }
        return null;
    }

    /**
     * 保存对象,将对象以字节的形式保存,取出时在转换成相应对象
     * @param context
     * @param objectKeyName
     * @param o
     * @param shareName
     */
    public static void putObject(Context context, String objectKeyName, Object o, String shareName) {
        SharedPreferences sp = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            //强对象放到OutPutStream中
            //将对象转出byte数字,并对其进行Base64编码
            String data = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(objectKeyName, data);
            SharedPreferencesCompat.apply(editor);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(baos!=null){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(oos!=null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    /**
     * 读取本地保存对象
     * @param context
     * @param objectName
     * @param shareName
     * @return
     */
    public static Object getObject(Context context, String objectName, String shareName){
        SharedPreferences sp = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        Object object=null;
        ByteArrayInputStream bais=null;
        ObjectInputStream ois =null;
        String date =sp.getString(objectName,"");
        if(date==null){
            return null;
        }
        try {
        byte [] base64Bytes= Base64.decode(date.getBytes(), Base64.DEFAULT);
        bais=new ByteArrayInputStream(base64Bytes);
            ois=new ObjectInputStream(bais);
            object=ois.readObject();
            return object;

        } catch (IOException e) {
            e.printStackTrace();
            return object;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return object;
        }finally {
            if(bais!=null){
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(ois!=null){
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    /**
     * 移除key对应的值
     *
     * @param context   上下文对象
     * @param key       键
     * @param shareName 配置文件名
     */
    public static void remove(Context context, String key, String shareName) {
        SharedPreferences sp = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 删除所有数据
     *
     * @param context   上下文对象
     * @param shareName 配置文件名
     */
    public static void clear(Context context, String shareName) {
        SharedPreferences sp = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);

    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context   上下文对象
     * @param key       键
     * @param shareName 配置文件名
     * @return true:存在，false 不存在
     */
    public static boolean contains(Context context, String key, String shareName) {
        SharedPreferences sp = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 获取所有键值对
     *
     * @param context   上下文对象
     * @param shareName 配置文件名
     *                  Map<String, ?>只能是只读模式，不能增加，
     *                  因为增加的时候不知道该写入什么类型的值；
     *                  Map<String, Object>可以读和写
     * @return 返回所有键值对
     */
    public static Map<String, ?> getAll(Context context, String shareName) {
        SharedPreferences sp = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        return sp.getAll();

    }

    /**
     * 创建一个SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        /**
         * Method提供了关于类或借口上单独某个方法（以及如何访问该方法）的信息，
         * 所反映的方法是类方法或者实例方法（包括抽象方法）；
         */
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        private static Method findApplyMethod() {
            /**
             * JAVA反射机制是在运行状态中，对于任意一个类，
             * 都能够知道这个类的所有属性和方法；对于任意一个对象，
             * 都能够调用它的任意一个方法；
             * 这种动态获取的信息以及动态调用对象的方法的功能称为java语言的反射机制。
             */
            Class clz = SharedPreferences.Editor.class;
            try {
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 如果找到apply方法则用apply方法提交，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            if (sApplyMethod != null) {
                try {
                    sApplyMethod.invoke(editor);
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
            editor.commit();
        }
    }

}
