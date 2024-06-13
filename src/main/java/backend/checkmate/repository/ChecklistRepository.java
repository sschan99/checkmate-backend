package backend.checkmate.repository;

import backend.checkmate.domain.Checklist;
import backend.checkmate.domain.Member;

import java.util.List;
import java.util.Optional;

public interface ChecklistRepository {

    Checklist save(Checklist checklist);

    Optional<Checklist> findById(Long id);

    List<Checklist> findAllByOrderByCreatedAtDesc();

    List<Checklist> findAllByMemberOrderByCreatedAtDesc(Member member);

}
