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
@Table(name = "projects")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false)
    private String projectName;

    @Column(nullable = false)
    private String projectUrl;

    @Column(nullable = false)
    private List<String> projectTechStacks;

    @Column(nullable = false)
    private YearMonth projectStartDate;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isProjectInProgress;

    private YearMonth projectEndDate;

    @Column(nullable = false)
    private List<String> projectDescription;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isProjectDeleted;

}
