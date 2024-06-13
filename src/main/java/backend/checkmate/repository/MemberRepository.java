package backend.checkmate.repository;

import backend.checkmate.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findBySessionId(String sessionId);
}
