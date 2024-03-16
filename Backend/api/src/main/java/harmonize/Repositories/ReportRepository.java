package harmonize.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import harmonize.Entities.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findReferenceById(int id);
}
