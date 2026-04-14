package com.hire_genie.resume_builder.model;

import com.hire_genie.resume_builder.config.YearMonthDateConverter;
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
    @Convert(converter = YearMonthDateConverter.class)
    private YearMonth startDate;

    @Column(nullable = false)
    private Boolean isWorkingInCompany = false;

    @Convert(converter = YearMonthDateConverter.class)
    private YearMonth endDate;

    @Column(nullable = false, columnDefinition = "text[]")
    private List<String> description;

    @Column(nullable = false)
    private Boolean isExperienceDeleted = false;

    @Column(nullable = false)
    private String userEmail;

}
