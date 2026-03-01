package com.harshil_infotech.hire_genie.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "others")
public class Other {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otherId;

    @Column(nullable = false)
    private List<String> description;

    @Column(nullable = false)
    private Boolean isDeleted = false;

}
