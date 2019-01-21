package com.github.evan.common_library.utils;

import com.github.evan.common_library.genericity.ParameterizedTypeImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2017/12/10.
 */
public class GsonUtil {

    private static GsonUtil mInstance = null;

    public static GsonUtil getInstance() {
        if (null == mInstance) {
            synchronized (GsonUtil.class) {
                if (null == mInstance) {
                    mInstance = new GsonUtil();
                }
            }
        }
        return mInstance;
    }

    private Gson mGson;

    private GsonUtil() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();   //序列化null值
        builder.setDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);    //统一序列化日期格式
        builder.setPrettyPrinting();    //输出格式化
        mGson = builder.create();
    }

    /** Json反序列化为对象 */
    public <Model> Model json2Model(String json, Class<Model> modelClass){
        return mGson.fromJson(json, modelClass);
    }

    /** 对象序列化为Json */
    public <Model> String model2Json(Model model){
        return mGson.toJson(model);
    }

    /** Json反序列化为List<对象> */
    public <Model> List<Model> json2ListModel(String json, Class<Model> modelClass){
        ParameterizedType listType = new ParameterizedTypeImpl(List.class, new Class[]{modelClass});
        return mGson.fromJson(json, listType);
    }

    /** List<对象>序列化为Json */
    public <Model> String ListModel2Json(List<Model> models, Class<Model> modelClass){
        ParameterizedType listType = new ParameterizedTypeImpl(List.class, new Class[]{modelClass});
        return mGson.toJson(models, listType);
    }

    /** Map<Key, Value>序列化为json */
    public <Key, Value> String mapModel2Json(Map<Key, Value> models, Class<Key> keyClass, Class<Value> valueClass){
        ParameterizedType mapType = new ParameterizedTypeImpl(Map.class, new Class[]{keyClass, valueClass});
        return mGson.toJson(models, mapType);
    }

    /** Json反序列化为Map */
    public <Key, Value> Map<Key, Value> json2MapModel(String json, Class<Key> keyClass, Class<Value> valueClass){
        ParameterizedType type = new ParameterizedTypeImpl(Map.class, new Class[]{keyClass, valueClass});
        return mGson.fromJson(json, type);
    }

}
