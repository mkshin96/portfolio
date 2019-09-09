package com.dblab.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table
@NoArgsConstructor
@Setter
@Getter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idx")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime registeredDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Introduction> introductions;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Project> projects;

    @Builder
    public User(String username, String password, String email, LocalDateTime registeredDate, Set<Introduction> introductions) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.registeredDate = registeredDate;
        this.introductions = introductions;
    }
}
