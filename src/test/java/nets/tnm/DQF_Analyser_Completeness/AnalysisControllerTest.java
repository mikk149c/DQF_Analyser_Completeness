package nets.tnm.DQF_Analyser_Completeness;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class AnalysisControllerTest {

    @Autowired
    CompletenessAnalyser completenessAnalyser;

    @Test
    public void GetNullCount1() throws JsonProcessingException {
        String json = "{\"country\":\"PK\",\"name\":null,\"id\":\"100\",\"email\":\"atta@example.com\",\"age\":\"30\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode object = mapper.readTree(json);

        Assert.assertEquals(1 ,completenessAnalyser.getNumberOfnullValues(object));
    }
}
