package harmonize.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import harmonize.DTOs.SearchDTO;
import harmonize.Services.MusicService;

@RestController
@RequestMapping("/music")
public class MusicController {
    private MusicService musicService;

    @Autowired
    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping(path = "/search")
    public ResponseEntity<JsonNode> search(@RequestBody SearchDTO search) {
        return ResponseEntity.ok(musicService.search(search));
    }

    @GetMapping(path = "/songs/{id}")
    public ResponseEntity<JsonNode> getSong(@PathVariable String id) {
        return ResponseEntity.ok(musicService.getSong(id));
    }

    @GetMapping(path = "/artists/{id}")
    public ResponseEntity<JsonNode> getArtist(@PathVariable String id) {
        return ResponseEntity.ok(musicService.getArtist(id));
    }
}
