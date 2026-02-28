package com.harshil_infotech.hire_genie.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "educations")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationId;

    @Column(nullable = false)
    private String educationTitle;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String fieldOfStudy;

    @Column(nullable = false)
    private YearMonth startDate;

    @Column(nullable = false)
    private Boolean isEducationInProgress = false;

    private YearMonth endDate;

    private String gradeTitle;

    private Double grades;

    private Boolean isEducationDeleted = false;

}
