/*
 * Copyright 2018 Veronica Anokhina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.org.sevn.share2json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class JsonUtil {
    public static final Object NULL = new Object() {
        @Override public boolean equals(Object o) {
            return o == this || o == null; // API specifies this broken equals implementation
        }
        @Override public String toString() {
            return "null";
        }
    };

    public static Object wrap(Object o) {
        if (o == null) {
            return NULL;
        }
        if (o instanceof JSONArray || o instanceof JSONObject) {
            return o;
        }
        if (o.equals(NULL)) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return new JSONArray((Collection) o);
            } else if (o.getClass().isArray()) {
                return new ExtJSONArray(o);
            }
            if (o instanceof Map) {
                return new JSONObject((Map) o);
            }
            if (o instanceof Boolean ||
                    o instanceof Byte ||
                    o instanceof Character ||
                    o instanceof Double ||
                    o instanceof Float ||
                    o instanceof Integer ||
                    o instanceof Long ||
                    o instanceof Short ||
                    o instanceof String) {
                return o;
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return o.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public static void putAny(JSONObject json, String k, Object o, boolean putNull) {
        try {
            put(json, k, o, putNull);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public static void put(JSONObject json, String k, Object o, boolean putNull) throws JSONException {
        //Log.e(LOG_TAG,"[" + k + "=" + po+"]");
        Object po = wrapAny(o);
        if ( !po.equals(NULL) || putNull ) {
            json.put(k, po);
        }
    }

    public static Object wrapAny(Object o) {
        Object po = wrap(o);
        if (po == null && o != null) {
            po = o.toString();
        }
        if (po == null) {
            po = NULL;
        }
        return po;
    }

    public static class ExtJSONArray extends JSONArray {
        public ExtJSONArray(Object array) throws JSONException {
            if (!array.getClass().isArray()) {
                throw new JSONException("Not a primitive array: " + array.getClass());
            }
            final int length = Array.getLength(array);
            for (int i = 0; i < length; ++i) {
                put(wrap(Array.get(array, i)));
            }
        }

    }
}
