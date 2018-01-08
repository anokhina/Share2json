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

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

class UrlActivityHandler extends ActivityHandler {

    private JSONObject extraData = new JSONObject();

    @Override
    public void handleSend(Intent intent, Activity activity) {
        Uri uri = (Uri) intent.getExtras().get(Intent.EXTRA_STREAM);
        if (uri != null) {
            handleUri(uri);
        } else {
            Bundle extras = intent.getExtras();
            JSONObject json = new JSONObject();
            for (String k : extras.keySet()) {
                Object o = extras.get(k);
                try {
                    json.put(k, wrap(extras.get(k)));
                } catch(JSONException e) {
                }
            }
            extraData = json;

            ClipboardManager clipboard = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
            try {
                ClipData clip = ClipData.newPlainText("shared data", extraData.toString(2));
                clipboard.setPrimaryClip(clip);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            activity.finish();
            //intent.getStringExtra(Intent.EXTRA_TEXT), intent.getStringExtra(Intent.EXTRA_SUBJECT)
        }
    }

    public JSONObject getExtraData() {
        return extraData;
    }

    public static final Object NULL = new Object() {
        @Override public boolean equals(Object o) {
            return o == this || o == null; // API specifies this broken equals implementation
        }
        @Override public String toString() {
            return "null";
        }
    };

    static class MyJSONArray extends JSONArray {
        public MyJSONArray(Object array) throws JSONException {
            if (!array.getClass().isArray()) {
                throw new JSONException("Not a primitive array: " + array.getClass());
            }
            final int length = Array.getLength(array);
            for (int i = 0; i < length; ++i) {
                put(wrap(Array.get(array, i)));
            }
        }

    }

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
                return new MyJSONArray(o);
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

    @Override
    public void handleMultipleSend(Intent intent, Activity activity) {
    }

    private void handleUri(Uri uri) {
    }

    @Override
    public void handleView(Intent intent, Activity activity) {
        Uri uri=intent.getData();
        handleUri(uri);
    }

}