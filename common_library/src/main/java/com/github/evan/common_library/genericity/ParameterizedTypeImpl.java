package com.github.evan.common_library.genericity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型类型类
 *
 * Created by wangjialiang at 2018/12/28
 *
 * 目的:优化封装Gson序列化和反序列化,避免泛型擦除
 *
 * 举例: List<String> getRawType为List.class, getActualTypeArguments应为Type[String.class]</>
 *
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    private Class raw;
    private Type[] args;

    public ParameterizedTypeImpl(Class raw, Type[] args) {
        this.raw = raw;
        this.args = args;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
