package backend.checkmate.controller;

import backend.checkmate.domain.Checklist;
import backend.checkmate.domain.Member;
import backend.checkmate.dto.CreateChecklistForm;
import backend.checkmate.service.ChecklistService;
import backend.checkmate.service.MemberService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("checklist")
public class ChecklistController {

    private final ChecklistService checklistService;
    private final MemberService memberService;

    @Autowired
    public ChecklistController(ChecklistService checklistService, MemberService memberService) {
        this.checklistService = checklistService;
        this.memberService = memberService;
    }

    @GetMapping("all")
    @ResponseBody
    public ResponseEntity<List<Checklist>> getAllLists() {
        return ResponseEntity.ok(checklistService.findCheckLists());
    }

    @GetMapping("my")
    @ResponseBody
    public ResponseEntity<List<Checklist>> getMyLists(@CookieValue("sessionId") Optional<String> sessionId) {
        Member member = memberService.checkLogin(sessionId.get());
        return ResponseEntity.ok(checklistService.findAllByMember(member));
    }

    @GetMapping("{id}")
    public String getChecklistDetail(@PathVariable("id") Long id, Model model) {
        Checklist checklist = checklistService.findById(id);
        if (checklist == null) {
            return "error"; // or handle error in a different way
        }
        checklist.setViews(checklist.getViews() + 1);
        checklistService.save(checklist);
        model.addAttribute("checklist", checklist);
        return "checklist";
    }

    @GetMapping("update/{id}")
    public String getUpdatePage(@PathVariable("id") Long id, Model model) {
        Checklist checklist = checklistService.findById(id);
        if (checklist == null) {
            return "error"; // or handle error in a different way
        }
        model.addAttribute("checklist", checklist);
        return "update-checklist";
    }

    @PostMapping("update/{id}")
    public String updateChecklist(@PathVariable("id") Long id, @RequestBody CreateChecklistForm form) {
        Gson gson = new Gson();
        Checklist checklist = checklistService.findById(id);
        if (checklist == null) {
            return "error"; // or handle error in a different way
        }
        checklist.setTitle(form.getTitle());
        checklist.setContent(gson.toJson(form.getContent()));
        checklistService.save(checklist);
        return "redirect:/checklist/" + id;
    }

    @GetMapping("delete/{id}")
    public String deleteChecklist(@PathVariable("id") Long id) {
        Checklist checklist = checklistService.findById(id);
        if (checklist == null) {
            return "error"; // or handle error in a different way
        }
        checklistService.delete(checklist);
        return "redirect:/";
    }

    @PostMapping("create")
    public String create(@CookieValue("sessionId") Optional<String> sessionId, @RequestBody CreateChecklistForm form) {
        Gson gson = new Gson();
        if (sessionId.isPresent()) {
            Member member = memberService.checkLogin(sessionId.get());
            Checklist checklist = new Checklist();
            checklist.setTitle(form.getTitle());
            checklist.setContent(gson.toJson(form.getContent()));
            checklist.setMember(member);
            checklist.setViews(0L);
            checklistService.save(checklist);
        }
        return "redirect:/";
    }

}
