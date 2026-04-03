package com.hire_genie.employee_recommendation_engine.vectorMappings;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "email_vector_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVectorMapping {

    @Id
    private String email;

    private String vectorDocumentId;

}
