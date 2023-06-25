package guru.learningjournal.kafka.examples.serde;

import guru.learningjournal.kafka.examples.types.PosInvoice;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import java.util.HashMap;
import java.util.Map;

public class AppSerdes extends Serdes {
    static public  final class POSInvoiceSerdes extends WrapperSerde<PosInvoice>{
        public POSInvoiceSerdes(){
            super(new JsonSerializer<>(),new JsonDeserializer<>());
        }

        static public Serde<PosInvoice> posInvoice(){
            POSInvoiceSerdes serde=new POSInvoiceSerdes();

            Map<String, Object> serConfig = new HashMap<>();
              serConfig.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG,PosInvoice.class);
              serde.configure(serConfig,false);

            serde.configure(serConfig,false);
            return serde;

        }
    }
}
