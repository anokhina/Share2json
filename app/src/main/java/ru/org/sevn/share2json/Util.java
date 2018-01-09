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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Util {
    public static String is2String(InputStream inputStream) throws IOException {
        return is2String(inputStream, "UTF-8");
    }
    public static String is2String(InputStream inputStream, String encoding) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, encoding);
        for (int rsz = 0; rsz >= 0; rsz = in.read(buffer, 0, buffer.length)) {
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    public static byte[] is2byte(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        ByteArrayOutputStream ret = new ByteArrayOutputStream();
        final byte[] buffer = new byte[bufferSize];
        int rsz = 0;
        do {
            ret.write(buffer, 0, rsz);
            rsz = inputStream.read(buffer, 0, buffer.length);
        } while (rsz > 0);
        return ret.toByteArray();
    }
}
