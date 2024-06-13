package backend.checkmate.repository;

import backend.checkmate.domain.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaChecklistRepository extends JpaRepository<Checklist, Long>, ChecklistRepository {
}
