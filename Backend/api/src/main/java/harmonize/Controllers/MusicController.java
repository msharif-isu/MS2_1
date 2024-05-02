package harmonize.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import harmonize.DTOs.ArtistDTO;
import harmonize.DTOs.SearchDTO;
import harmonize.DTOs.SongDTO;
import harmonize.Services.MusicService;

@RestController
@RequestMapping("/music")
public class MusicController {
    private MusicService musicService;

    @Autowired
    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @PostMapping(path = "")
    public ResponseEntity<JsonNode> search(@RequestBody SearchDTO search) {
        return ResponseEntity.ok(musicService.search(search));
    }

    @GetMapping(path = "/songs/{id}")
    public ResponseEntity<SongDTO> getSong(@PathVariable String id) {
        return ResponseEntity.ok(musicService.getSong(id));
    }

    @GetMapping(path = "/artists/{id}")
    public ResponseEntity<ArtistDTO> getArtist(@PathVariable String id) {
        return ResponseEntity.ok(musicService.getArtist(id));
    }

    @GetMapping(path = "/artists/albums")
    public ResponseEntity<JsonNode> getArtistAlbums(@RequestBody SearchDTO search) {
        return ResponseEntity.ok(musicService.getArtistAlbums(search));
    }
}
