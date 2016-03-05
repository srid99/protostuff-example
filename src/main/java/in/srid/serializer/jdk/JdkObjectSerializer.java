/*
 * Copyright 2014-2015 Sridhar Gnanasekaran
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License
 */

package in.srid.serializer.jdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import in.srid.serializer.Deserializer;
import in.srid.serializer.Serializer;

public class JdkObjectSerializer implements Serializer, Deserializer {

    @Override
    public <T> T deserialize(final byte[] bytes, final Class<T> clazz) {
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        try (final ObjectInputStream ois = new ObjectInputStream(in)) {
            Object obj = ois.readObject();
            return clazz.cast(obj);
        } catch (final ClassNotFoundException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public <T> byte[] serialize(final T source) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (final ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(source);
            oos.flush();
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }

        return out.toByteArray();
    }
}
