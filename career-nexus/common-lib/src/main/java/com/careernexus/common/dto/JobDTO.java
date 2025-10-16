package com.careernexus.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
public class JobDTO {
    private Long id;
    private String title;
    private String company;
    private String location;
    private String jobType; // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP
    private String experienceLevel; // ENTRY, MID, SENIOR, LEAD
    private String description;
    private List<String> requiredSkills;
    private List<String> preferredSkills;
    private String salaryRange;
    private String applicationUrl;
    private String companyLogoUrl;
    private boolean isActive;
    private LocalDateTime postedDate;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobDTO(Long id, String title, String company, String location) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
    }
}