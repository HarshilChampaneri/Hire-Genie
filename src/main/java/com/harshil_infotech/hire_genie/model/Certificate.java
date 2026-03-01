package com.harshil_infotech.hire_genie.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    @Column(nullable = false)
    private String certificateTitle;

    @Column(nullable = false)
    private String certificateUrl;

    @Column(nullable = false)
    private Boolean isCertificateDeleted = false;

}
