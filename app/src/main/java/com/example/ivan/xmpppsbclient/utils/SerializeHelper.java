package com.example.ivan.xmpppsbclient.utils;

import com.example.ivan.xmpppsbclient.enrities.RosterEntryDecorator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by I.Laukhin on 26.01.2017.
 */

public class SerializeHelper {

    private static SerializeHelper instance;

    private Gson gson;

    private SerializeHelper() {
        gson = new Gson();
    }

    public static SerializeHelper getInstance() {
        if (instance == null) {
            instance = new SerializeHelper();
        }
        return instance;
    }

    public String serializeWithGson(List<RosterEntryDecorator> serializeObject) {
        return gson.toJson(serializeObject, new TypeToken<List<RosterEntryDecorator>>() {}.getType());
    }

    public List<RosterEntryDecorator> deserializeUsersGroup(String deserializeString) {
        return gson.fromJson(deserializeString, new TypeToken<List<RosterEntryDecorator>>() {}.getType());
    }
}
