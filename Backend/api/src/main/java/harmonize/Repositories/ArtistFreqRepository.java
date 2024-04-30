package harmonize.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import harmonize.Entities.ArtistFreq;

public interface ArtistFreqRepository extends JpaRepository<ArtistFreq, Long> {
    ArtistFreq findReferenceById(int id);
}
