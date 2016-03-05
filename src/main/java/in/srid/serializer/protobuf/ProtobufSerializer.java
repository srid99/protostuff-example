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

package in.srid.serializer.protobuf;

import in.srid.serializer.AbstractProtoSerializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;

public class ProtobufSerializer extends AbstractProtoSerializer {

    @Override
    public <T> byte[] serializeInternal(final T source, final Schema<T> schema, final LinkedBuffer buffer) {
        return ProtobufIOUtil.toByteArray(source, schema, buffer);
    }

    @Override
    public <T> T deserializeInternal(final byte[] bytes, final T result, final Schema<T> schema) {
        ProtobufIOUtil.mergeFrom(bytes, result, schema);
        return result;
    }
}
