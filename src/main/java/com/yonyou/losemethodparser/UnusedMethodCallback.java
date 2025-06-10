package com.yonyou.losemethodparser;

import java.lang.reflect.InvocationTargetException;

/**
 * @author jinchenj
 * @description 未引用方法渲染
 * @create:2025-06-0909:22:28
 */
public interface UnusedMethodCallback {
    void onUnusedMethodFound(String classMethodName) ;
}