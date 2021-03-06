package nets.tnm.DQF_Analyser_Completeness;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class AnalyserController {

    Logger logger = LoggerFactory.getLogger(AnalyserController.class);

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
        int numberOfFields = object.size()+1;
        int numberOfFieldsWithNull = numberOfFields - completenessAnalyser.getNumberOfnullValues(object.get("analysisObjectData"));
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
                objectNode.put("id", subObject.get("id").asInt());
                objectNode.put("group", subObject.get("group").asText());
                objectNode.put("message", GetMessage(subObject));

                arrayNode.add(objectNode);
            }
        }

        new Thread(() -> SendListToRepo(arrayNode)).start();

        return arrayNode;
    }

    private JsonNode SendListToRepo(ArrayNode arrayNode) {
            return restTemplate.postForObject(
                "http://DQF-Analysis-Repo/analysis/save/message/list",
                arrayNode,
                JsonNode.class);
    }
}
