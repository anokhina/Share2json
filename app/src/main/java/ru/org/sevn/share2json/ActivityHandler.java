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

public abstract class ActivityHandler {
    public abstract void handleSend(Intent intent, Activity activity);
    public abstract void handleMultipleSend(Intent intent, Activity activity);
    public abstract void handleView(Intent intent, Activity activity);
}
