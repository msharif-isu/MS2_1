package harmonize.Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

import harmonize.TestUtil;
import harmonize.DTOs.SearchDTO;
public class MusicTest extends TestUtil {
    @Test
    public void searchTest() throws Exception {
        SearchDTO search = new SearchDTO("Future", "track", "3", "0");
        ResponseEntity<JsonNode> responseEntity = musicTestService.getSearch(search);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
