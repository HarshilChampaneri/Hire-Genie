package com.hire_genie.job_service.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String candidateEmail;

    @Column(nullable = false)
    private String recruiterEmail;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private Boolean isJobApplicationAccepted;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(nullable = false)
    private Boolean isJobApplicationDeleted;

}
