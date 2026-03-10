package com.hire_genie.resume_builder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile_summaries")
public class ProfileSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileSummaryId;

    @Column(nullable = false, length = 350)
    private String profileSummary;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private Boolean isProfileSummaryDeleted = false;

}
