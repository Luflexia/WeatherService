package com.app.weather.component;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class CacheComponent {

    private final Map<String, Object> hashMap = new HashMap<>();
    private final int MAX_SIZE = 100;

    public void put(String key, Object value) {
        hashMap.put(key, value);
        if (hashMap.size() > MAX_SIZE) {
            String oldestKey = hashMap.keySet().iterator().next();
            hashMap.remove(oldestKey);
        }
    }

    public Object get(String key) {
        return hashMap.get(key);
    }

    public void remove(String key) {
        hashMap.remove(key);
    }

}