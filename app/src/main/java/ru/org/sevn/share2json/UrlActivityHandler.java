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
import android.content.Intent;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

class UrlActivityHandler extends ActivityHandler {

    private JSONObject extraData = new JSONObject();

    private static void toClipboard(final Activity activity, final JSONObject o) {
        try {
            ClipboardUtil.toClipboard(activity, o.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void toExit(final Activity activity) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        }, 3000);
    }

    @Override
    public void handleSend(final Intent intent, final Activity activity) {
        if (intent.getType() != null) {
            //intent.getStringExtra(Intent.EXTRA_TEXT), intent.getStringExtra(Intent.EXTRA_SUBJECT)
            extraData = IntentUtil.toJson(intent, activity);
            toClipboard(activity, extraData);
            toExit(activity);
        } else {
        }
    }

    public JSONObject getExtraData() {
        return extraData;
    }

    @Override
    public void handleMultipleSend(Intent intent, Activity activity) {
    }

    @Override
    public void handleView(Intent intent, Activity activity) {
    }

}