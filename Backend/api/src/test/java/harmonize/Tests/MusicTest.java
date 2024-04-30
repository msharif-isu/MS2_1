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
        SearchDTO search = new SearchDTO("Future", "track", "1", "0");
        ResponseEntity<JsonNode> responseEntity = musicTestService.getSearch(search);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        search = new SearchDTO("Future", "trac", "1", "0");
        responseEntity = musicTestService.getSearch(search);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        search = new SearchDTO("Future", "track", "-1", "0");
        responseEntity = musicTestService.getSearch(search);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        String badSearch = "Future";
        responseEntity = musicTestService.getSearch(badSearch);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void getSongTest() throws Exception {
        String song = "273dCMFseLcVsoSWx59IoE";
        ResponseEntity<JsonNode> responseEntity = musicTestService.getSong(song);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        song = "badblood";
        responseEntity = musicTestService.getSong(song);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
