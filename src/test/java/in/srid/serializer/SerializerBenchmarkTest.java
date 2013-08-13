package in.srid.serializer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import in.srid.serializer.fasterxml.FasterxmlSerializer;
import in.srid.serializer.jackson.JacksonSerializer;
import in.srid.serializer.jdk.JdkObjectSerializer;
import in.srid.serializer.kryo.KryoSerializer;
import in.srid.serializer.model.ImmutableModel;
import in.srid.serializer.protobuf.ProtobufSerializer;
import in.srid.serializer.protostuff.ProtostuffSerializer;

import org.junit.Test;

import com.google.common.base.Stopwatch;

public class SerializerBenchmarkTest {

    private static final int BENCHMARK_LIMIT = 1000000;

    private static final JdkObjectSerializer jdk = new JdkObjectSerializer();
    private static final JacksonSerializer jackson = new JacksonSerializer();
    private static final FasterxmlSerializer fastxml = new FasterxmlSerializer();
    private static final ProtobufSerializer protobuf = new ProtobufSerializer();
    private static final ProtostuffSerializer protostuff = new ProtostuffSerializer();
    private static final KryoSerializer kryo = new KryoSerializer();

    private final Stopwatch stopwatch = new Stopwatch();

    enum SerializationType {
        JDK(jdk, jdk), JACKSON(jackson, jackson), FASTERXML(fastxml, fastxml), PROTOBUF(protobuf, protobuf), PROTOSTUFF(protostuff, protostuff), KRYO(kryo, kryo);

        private final Serializer serializer;
        private final Deserializer deserializer;

        private SerializationType(final Serializer serializer , final Deserializer deserializer) {
            this.serializer = serializer;
            this.deserializer = deserializer;
        }
    }

    @Test
    public void benchmark() {
        final SerializationType[] types = SerializationType.values();

        newLine();
        System.out.println( String.format( "Benchmark for %s objects (includes both serialization and deserialization)", BENCHMARK_LIMIT ) );
        newLine();

        for ( final SerializationType type : types ) {
            runTestcaseFor( type );
            System.out.println( type.toString().toLowerCase() + " : " + stopwatch );
            stopwatch.reset();
        }

        newLine();
    }

    private void runTestcaseFor( final SerializationType type ) {

        final Serializer serializer = type.serializer;
        final Deserializer deserializer = type.deserializer;

        for ( int i = 0; i < BENCHMARK_LIMIT; i++ ) {
            final String someValue = "some random value " + i;
            final ImmutableModel expected = new ImmutableModel( someValue );

            stopwatch.start();

            final byte[] bytes = serializer.serialize( expected );
            final ImmutableModel actual = deserializer.deserialize( bytes, ImmutableModel.class );

            stopwatch.stop();

            assertThat( expected, is( actual ) );
        }
    }

    private void newLine() {
        System.out.println( "\n" );
    }
}
