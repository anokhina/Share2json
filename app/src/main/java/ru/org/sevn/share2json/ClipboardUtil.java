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

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

//see http://www.androhub.com/android-clipboard-tutorial/
public class ClipboardUtil {

    public static void toClipboard(final Context context, final String str) {
        toClipboard(context, "shared data", str);
    }

    public static void toClipboard(Context mContext, String label, String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            toClipboardOld(mContext, text);
        } else {
            toClipboardHoney(mContext, label, text);
        }
        Toast.makeText(mContext, "Data copied to Clipboard.", Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void toClipboardHoney(Context context, String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

    @SuppressWarnings("deprecation")
    private static void toClipboardOld(Context context, String text) {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(text);
    }

    public static String fromClipboard(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return fromClipboardOld(context);
        } else {
            return fromClipboardHoney(context);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static String fromClipboardHoney(final Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData primaryClip = clipboard.getPrimaryClip();
        ClipData.Item item = primaryClip.getItemAt(0);

        return item.getText().toString();
    }

    @SuppressWarnings("deprecation")
    private static String fromClipboardOld(final Context context) {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return clipboard.getText().toString();

    }
}
