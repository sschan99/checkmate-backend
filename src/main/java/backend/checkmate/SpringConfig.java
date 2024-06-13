package backend.checkmate;

import backend.checkmate.repository.ChecklistRepository;
import backend.checkmate.repository.MemberRepository;
import backend.checkmate.service.ChecklistService;
import backend.checkmate.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;
    private final ChecklistRepository checklistRepository;

    public SpringConfig(MemberRepository memberRepository, ChecklistRepository checklistRepository) {
        this.memberRepository = memberRepository;
        this.checklistRepository = checklistRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

    @Bean
    public ChecklistService checklistService() {
        return new ChecklistService(checklistRepository);
    }

}
