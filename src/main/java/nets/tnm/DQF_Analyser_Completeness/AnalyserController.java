package nets.tnm.DQF_Analyser_Completeness;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class AnalyserController {

    @Autowired
    CompletenessAnalyser completenessAnalyser;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/analyse")
    public String Analyse(@RequestBody JsonNode object){

        String message = GetMessage(object);

        restTemplate.postForObject(
                "http://DQF-Analysis-Repo/analysis/save/message/" + object.get("id") + "/" + object.get("group"),
                message,
                JsonNode.class
                );

        return message;
    }

    private String GetMessage(JsonNode object) {
        int numberOfFields = object.size();
        int numberOfFieldsWithNull = completenessAnalyser.getNumberOfnullValues(object.get("analysisObjectData"));
        String message = "DQF_Analyser_Completeness:" + numberOfFieldsWithNull + ":" + numberOfFields;
        return message;
    }

    @PostMapping("/analyse/list")
    public ArrayNode AnalyseList(@RequestBody JsonNode object){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();

        if (object.isArray()){
            for (final JsonNode subObject : object){
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("id", subObject.get("id"));
                objectNode.put("group", subObject.get("group"));
                objectNode.put("message", GetMessage(subObject));

                arrayNode.add(objectNode);
            }
        }

        restTemplate.postForObject(
                "http://DQF-Analysis-Repo/analysis/save/message/" + object.get("id") + "/" + object.get("group"),
                arrayNode,
                JsonNode.class
        );

        return arrayNode;
    }
}
