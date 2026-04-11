package com.hire_genie.job_recommendation_engine.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job_vector_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobVectorMapping {

    @Id
    private Long jobId;

    private String vectorDocumentId;

}
