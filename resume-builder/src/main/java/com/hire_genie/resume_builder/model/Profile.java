package com.hire_genie.resume_builder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String profession;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String mobileNo;

    @Column(nullable = false)
    private Set<String> urls;

    @Column(nullable = false, length = 350)
    private String profileSummary;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private Boolean isProfileDeleted = false;

}
