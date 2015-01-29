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

package in.srid.serializer.kryo;

import in.srid.serializer.Deserializer;
import in.srid.serializer.Serializer;
import in.srid.serializer.model.ImmutableModel;

import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoSerializer implements Serializer, Deserializer {
    private static final Kryo kryo = new Kryo();

    static {
        kryo.register( ImmutableModel.class );
        kryo.setInstantiatorStrategy( new StdInstantiatorStrategy() );
    }

    @Override
    public <T> T deserialize( final byte[] bytes , final Class<T> clazz ) {
        final Object value = kryo.readClassAndObject( new Input( bytes ) );
        return clazz.cast( value );
    }

    @Override
    public <T> byte[] serialize( T source ) {
        final Output output = new Output( 512 , 1024 * 8 );
        kryo.writeClassAndObject( output, source );
        return output.getBuffer();
    }
}
