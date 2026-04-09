package com.hire_genie.job_service.model;

import com.hire_genie.job_service.enums.JobType;
import com.hire_genie.job_service.enums.WorkMode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Column(nullable = false)
    private String jobTitle;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    @Column(nullable = false)
    private String location;

    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private String currency;

    @Column(nullable = false)
    private Integer vacancies;

    @Column(nullable = false)
    private Boolean isJobDeleted = false;

    @Column(nullable = false)
    private String userEmail;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "company_id")
    private Company company;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant modifiedAt;

}
