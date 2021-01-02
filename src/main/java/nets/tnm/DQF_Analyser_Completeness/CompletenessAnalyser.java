package nets.tnm.DQF_Analyser_Completeness;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

@Service
public class CompletenessAnalyser {


    public int getNumberOfnullValues(JsonNode object) {
        int returnValue = 0;
        Iterator<String> fields = object.fieldNames();
        while (fields.hasNext()){
            String field = fields.next();
            if (object.get(field).isNull()) returnValue++;
        }
        return returnValue;
    }
}
