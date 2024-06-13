package backend.checkmate.controller;

import backend.checkmate.domain.Member;
import backend.checkmate.dto.CreateMemberForm;
import backend.checkmate.dto.LoginMemberForm;
import backend.checkmate.dto.MemberNameDto;
import backend.checkmate.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Controller
@RequestMapping("member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("join")
    public String create(CreateMemberForm form) {
        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setName(form.getName());
        member.setPassword(form.getPassword());
        memberService.join(member);
        return "redirect:/login.html";
    }

    @PostMapping("login")
    public ResponseEntity<Void> login(LoginMemberForm form) {
        String sessionId = memberService.login(form.getEmail(), form.getPassword());
        if (sessionId == null) {
            return ResponseEntity.status(302)
                    .header(LOCATION, "/login-failed.html")
                    .body(null);
        }
        ResponseCookie cookie = ResponseCookie.from("sessionId", sessionId)
                .path("/").httpOnly(true).build();
        return ResponseEntity.status(302)
                .header(SET_COOKIE, cookie.toString())
                .header(LOCATION, "/")
                .body(null);
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("sessionId", "")
                .maxAge(0).path("/").httpOnly(true).build();
        return ResponseEntity.status(302)
                .header(SET_COOKIE, cookie.toString())
                .header(LOCATION, "/")
                .body(null);
    }

    @GetMapping("check-login")
    @ResponseBody
    public ResponseEntity<MemberNameDto> checkLogin(@CookieValue("sessionId") Optional<String> sessionId) {
        MemberNameDto body = new MemberNameDto();
        if (sessionId.isEmpty()) {
            body.setName("");
            return ResponseEntity.ok(body);
        }
        Member member = memberService.checkLogin(sessionId.get());
        body.setName(member != null ? member.getName() : "");
        return ResponseEntity.ok(body);
    }
}
