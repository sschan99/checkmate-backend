package backend.checkmate.service;

import backend.checkmate.domain.Checklist;
import backend.checkmate.domain.Member;
import backend.checkmate.repository.ChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChecklistService {

    private final ChecklistRepository checklistRepository;

    @Autowired
    public ChecklistService(ChecklistRepository checklistRepository) {
        this.checklistRepository = checklistRepository;
    }

    @Transactional
    public List<Checklist> findCheckLists() {
        return checklistRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public List<Checklist> findAllByMember(Member member) {
        return checklistRepository.findAllByMemberOrderByCreatedAtDesc(member);
    }

    public Checklist findOne(Long id) {
        return checklistRepository.findById(id).get();
    }

    public void save(Checklist checklist) {
        checklistRepository.save(checklist);
    }

    public Checklist findById(Long id) {
        Optional<Checklist> checklist = checklistRepository.findById(id);
        return checklist.orElse(null);
    }

    public void delete(Checklist checklist) {
        checklistRepository.delete(checklist);
    }
}
