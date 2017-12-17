/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.support;

import framework.storage.StorageManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 * @author krb
 */
public class ExecutionManager extends AbstractManager {

    private StorageManager storageManager;

    private Boolean success;

    public ExecutionManager() {
        this.storageManager = new StorageManager();
    }

    public Boolean hasSuccess() {
        return this.success;
    }

    public static Object createObject(String className, Object[] arguments) {
        Object object = null;

        try {
            int numberOfArguments = arguments.length;
            Class[] parameterTypes = new Class[numberOfArguments];
            for (int i = 0; i < numberOfArguments; i++) {
                parameterTypes[i] = arguments[i].getClass();
            }

            Class c = Class.forName(className);
            Constructor constructor = c.getConstructor(parameterTypes);
            object = constructor.newInstance(arguments);
            return object;

        } catch (Exception e) {
            e.getCause().printStackTrace();
            System.out.println("Exception class: " + e.getClass());
            System.out.println("Exception message: " + e.getMessage());
        }
        return object;
    }

    public static Object createObject(Constructor constructor, Object[] arguments) {
//        System.out.println("Constructor: " + constructor.toString());
        Object object = null;

        try {
            object = constructor.newInstance(arguments);
//            System.out.println("Object: " + object.toString());
            return object;
        } catch (InstantiationException e) {
            //handle it
        } catch (IllegalAccessException e) {
            //handle it
        } catch (IllegalArgumentException e) {
            //handle it
        } catch (InvocationTargetException e) {
            //handle it
        }
        return object;
    }

    public Boolean execute(String command, Object data) {
        Method method;

        try {
            method = this.storageManager.getClass().getMethod(command, Object.class);
//    Method method = obj.getClass().getMethod(methodName, param1.class, param2.class, ..);
//        } catch (SecurityException e) {
//            return false;
//        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
            Class thisClass = this.getClass();
            String methodName = thisClass.getEnclosingMethod().getName();
            System.out.println("Error in " + thisClass.toString() + "." + methodName + ": " + e.getMessage());
            return false;
        }

        this.success = false;
        try {
            method.invoke(this.storageManager, data);
            this.success = this.storageManager.hasSuccess();
//        } catch (IllegalArgumentException e) {
//            return false;
//        } catch (IllegalAccessException e) {
//            return false;
//        } catch (InvocationTargetException e) {
        } catch (Exception e) {
            Class thisClass = this.getClass();
            String methodName = thisClass.getEnclosingMethod().getName();
            System.out.println("Error in " + thisClass.toString() + "." + methodName + ": " + e.getMessage());
            return false;
        }

        return this.success;
    }

    public static Object executeMethodOnObject(Object object, String methodName, Object[] parameterTypes) {
        Method method;

        Object value = null;
        try {
            Class objectClass = object.getClass();

            Class[] classTypes = new Class[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                classTypes[i] = parameterTypes[i].getClass();
            }

            method = objectClass.getMethod(methodName, classTypes);

            if (parameterTypes.length == 0) {
                value = method.invoke(object);
            } else {
                value = method.invoke(object, parameterTypes);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return value;
    }
}
