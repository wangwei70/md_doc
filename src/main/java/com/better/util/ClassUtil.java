package com.better.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * *********************************************************************************************
 * *
 *
 * @File Name : ClassUtil
 * *
 * @Author Name :  Sl
 * *
 * @Start Date : 2022/9/6 3:24 PM
 * *
 * @Last Update : 2022/9/6 3:24 PM
 * *
 * @Description : TODO
 * *
 * --------------------
 * --------------------------------------------------------------------------
 */
public class ClassUtil {



    public static Field[] fields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>(16);
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));
            clazz = clazz.getSuperclass();
        }
        return fieldList.toArray(new Field[]{});
    }




    public static Field getFiled(String fieldName, Class clazz) {
        Field field = null;
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (Exception e) {
                clazz = clazz.getSuperclass();
            }
        }
        if ( field == null) {
            return null;
        }
        field.setAccessible(true);
        return field;
    }



}
