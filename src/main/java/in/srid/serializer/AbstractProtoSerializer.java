/*
 * Copyright 2014-2015 Sridhar Gnanasekaran
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package in.srid.serializer;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Schema;

public abstract class AbstractProtoSerializer implements Serializer, Deserializer {

    private final Objenesis objenesis = new ObjenesisStd( true );

    @Override
    public <T> byte[] serialize( final T source ) {
        @SuppressWarnings( "unchecked" )
        final Class<T> clazz = (Class<T>) source.getClass();
        final LinkedBuffer buffer = LinkedBuffer.allocate( LinkedBuffer.DEFAULT_BUFFER_SIZE );
        try {
            final Schema<T> schema = SchemaUtils.getSchema( clazz );
            return serializeInternal( source, schema, buffer );
        }
        catch ( final Exception e ) {
            throw new IllegalStateException( e );
        }
        finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T deserialize( final byte[] bytes , final Class<T> clazz ) {
        try {
            @SuppressWarnings( "unchecked" )
            final T result = (T) objenesis.newInstance( clazz );
            return deserializeInternal( bytes, result, SchemaUtils.getSchema( clazz ) );
        }
        catch ( final Exception e ) {
            throw new IllegalStateException( e );
        }
    }

    public abstract <T> byte[] serializeInternal( final T source , final Schema<T> schema , final LinkedBuffer buffer );

    public abstract <T> T deserializeInternal( final byte[] bytes , final T result , final Schema<T> schema );
}
