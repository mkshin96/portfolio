package com.dblab.dto;

import com.dblab.domain.Introduction;
import com.dblab.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class IntroductionDto {

    @NotBlank(message = "필수 항목입니다.")
    private String title;

    private String growth;

    private String reason;

    private String strength;

    private String weakness;

    private String aspiration;

    private LocalDateTime registeredDate;

    private User user;

    @Builder
    public IntroductionDto(@NotBlank(message = "필수 항목입니다.") String title, String growth, String reason, String strength, String weakness, String aspiration, LocalDateTime registeredDate, User user) {
        this.title = title;
        this.growth = growth;
        this.reason = reason;
        this.strength = strength;
        this.weakness = weakness;
        this.aspiration = aspiration;
        this.registeredDate = registeredDate;
        this.user = user;
    }

    public Introduction setIntroduction(User currentUser) {
        Introduction introduction = new Introduction();

        introduction.setTitle(this.title);
        introduction.setGrowth(this.growth);
        introduction.setReason(this.reason);
        introduction.setStrength(this.strength);
        introduction.setWeakness(this.weakness);
        introduction.setAspiration(this.aspiration);
        introduction.setRegisteredDate(LocalDateTime.now());
        introduction.setUsers(currentUser);

        return introduction;
    }
}
