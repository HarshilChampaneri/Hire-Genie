package com.harshil_infotech.hire_genie.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "experiences")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceId;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private YearMonth startDate;

    @Column(nullable = false)
    private Boolean isWorkingInCompany = false;

    private YearMonth endDate;

    @Column(nullable = false)
    private List<String> description;

    @Column(nullable = false)
    private Boolean isExperienceDeleted = false;

}
