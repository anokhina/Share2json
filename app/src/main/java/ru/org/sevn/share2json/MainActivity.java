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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private UrlActivityHandler ahandler = new UrlActivityHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentHandler.handleIntent(getIntent(), this, ahandler);

        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView);
        try {
            JSONObject json = ahandler.getExtraData();
            Iterator<String> it = json.keys();
            if ( it.hasNext() ) {
                setText(textView, json.toString(2));
            } else {
                setText(textView, ClipboardUtil.fromClipboard(this));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        TextView textView = (TextView) findViewById(R.id.textView);
        setText(textView, ClipboardUtil.fromClipboard(this));
    }

    private void setText(TextView textView, String txt) {
        if (txt != null) {
            textView.setText(txt.substring(0, Math.min(txt.length(), 2048)));
        } else {
            textView.setText("");
        }
    }
}
