package backend.checkmate.service;

import backend.checkmate.domain.Member;
import backend.checkmate.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void join(Member member) {
        memberRepository.save(member);
    }

    public String login(String email, String password) {
        Optional<Member> found = memberRepository.findByEmail(email);
        if (found.isEmpty()) {
            return null;
        }
        Member member = found.get();
        if (Objects.equals(member.getPassword(), password)) {
            String sessionId = UUID.randomUUID().toString();
            member.setSessionId(sessionId);
            memberRepository.save(member);
            return sessionId;
        }
        return null;
    }

    public Member checkLogin(String sessionId) {
        Optional<Member> found = memberRepository.findBySessionId(sessionId);
        if (found.isEmpty()) {
            return null;
        }
        return found.get();
    }
}
