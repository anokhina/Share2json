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
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Set;

public class IntentUtil {

    private static String LOG_TAG = "IntentUtil";

    public static void dumpIntent(Intent i){

        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            Log.e(LOG_TAG,"Dumping Intent start");
            while (it.hasNext()) {
                String key = it.next();
                Log.e(LOG_TAG,"[" + key + "=" + bundle.get(key)+"]");
            }
            Log.e(LOG_TAG,"Dumping Intent end");
        }
    }

    public static String getTextContent(final ContentResolver cr, Uri uri) {
        String data = null;
        if (uri != null) {
            InputStream stream = null;
            try {
                stream = cr.openInputStream(uri);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                data = Util.is2String(stream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Log.i(LOG_TAG, "data: " + data);
        }
        return data;
    }

    public static String getBase64content(final ContentResolver cr, Uri uri) {
        String data = null;
        if (uri != null) {
            InputStream stream = null;
            try {
                stream = cr.openInputStream(uri);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                data = Base64.encodeToString(Util.is2byte(stream), Base64.URL_SAFE);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Log.i(LOG_TAG, "data: " + data);
        return data;
    }

    public static JSONObject toJson(final Intent intent, final Activity activity) {
        //dumpIntent(intent);
        JSONObject json = new JSONObject();
        Uri uri = (Uri) intent.getExtras().get(Intent.EXTRA_STREAM);

        Bundle extras = intent.getExtras();
        if (uri != null) {
            JsonUtil.putAny(json, PARAM_URI_PATH, uri.getPath(), false);
        }
        JsonUtil.putAny(json, PARAM_URI, uri, false);
        JsonUtil.putAny(json, PARAM_TYPE, intent.getType(), false);
        if (extras.keySet().size() > 0) {
            JSONObject extra = new JSONObject();
            for (String k : extras.keySet()) {
                JsonUtil.putAny(extra, k, extras.get(k), false);
            }
            JsonUtil.putAny(json, PARAM_EXTRA, extra, false);
        }

        JSONArray names = json.names();
        String[] namesArr = new String[names.length()];
        for (int i = 0; i < namesArr.length; i++) {
            try {
                namesArr[i] = names.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String content = null;
        if (intent.getType() != null && intent.getType().startsWith("text")) {
            content = getTextContent(activity.getContentResolver(), uri);
        } else {
            content = getBase64content(activity.getContentResolver(), uri);
        }
        int contentSize = -1;
        try {
            JSONObject jsonRet = new JSONObject(json, namesArr);
            JsonUtil.putAny(jsonRet, PARAM_CONTENT, content, false);
            byte[] ret = jsonRet.toString(2).getBytes("UTF-8");
            contentSize = ret.length;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (contentSize >=0) {
            if (contentSize < 1048576) {
                JsonUtil.putAny(json, PARAM_CONTENT, content, false);
            } else {
                JsonUtil.putAny(json, PARAM_CONTENT, PARAM_CONTENT_TO_LARGE + contentSize, false);
            }

        }

        return json;
    }

    public static String PARAM_EXTRA = "extra";
    public static String PARAM_URI = "uri";
    public static String PARAM_TYPE = "type";
    public static String PARAM_URI_PATH = "uriPath";
    public static String PARAM_CONTENT = "content";
    public static String PARAM_CONTENT_TO_LARGE = "ERROR: content is too big. (MAX SIZE is 1048576) SIZE=";
}
