package com.careernexus.common.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ResumeDTO {
    private Long id;
    private Long userId;
    private String fileName;
    private String fileUrl;
    private String fileType; // PDF, DOCX
    private Long fileSizeBytes;

    // Parsed content
    private String fullText;
    private List<String> skills;
    private List<ExperienceDTO> experiences;
    private List<EducationDTO> education;
    private List<String> certifications;
    private String summary;

    // Metadata
    private boolean isParsed;
    private String parseStatus; // PENDING, IN_PROGRESS, COMPLETED, FAILED
    private String parseErrorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastParsedAt;

    public ResumeDTO() {
    }

    public ResumeDTO(Long id, Long userId, String fileName) {
        this.id = id;
        this.userId = userId;
        this.fileName = fileName;
    }

    // Nested DTOs
    public static class ExperienceDTO {
        private String company;
        private String position;
        private String duration;
        private String location;
        private List<String> responsibilities;
        private String startDate;
        private String endDate;

        // Getters and Setters
        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public List<String> getResponsibilities() {
            return responsibilities;
        }

        public void setResponsibilities(List<String> responsibilities) {
            this.responsibilities = responsibilities;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }

    public static class EducationDTO {
        private String institution;
        private String degree;
        private String field;
        private String grade;
        private String startDate;
        private String endDate;

        // Getters and Setters
        public String getInstitution() {
            return institution;
        }

        public void setInstitution(String institution) {
            this.institution = institution;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<ExperienceDTO> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperienceDTO> experiences) {
        this.experiences = experiences;
    }

    public List<EducationDTO> getEducation() {
        return education;
    }

    public void setEducation(List<EducationDTO> education) {
        this.education = education;
    }

    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<String> certifications) {
        this.certifications = certifications;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public boolean isParsed() {
        return isParsed;
    }

    public void setParsed(boolean parsed) {
        isParsed = parsed;
    }

    public String getParseStatus() {
        return parseStatus;
    }

    public void setParseStatus(String parseStatus) {
        this.parseStatus = parseStatus;
    }

    public String getParseErrorMessage() {
        return parseErrorMessage;
    }

    public void setParseErrorMessage(String parseErrorMessage) {
        this.parseErrorMessage = parseErrorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastParsedAt() {
        return lastParsedAt;
    }

    public void setLastParsedAt(LocalDateTime lastParsedAt) {
        this.lastParsedAt = lastParsedAt;
    }

    @Override
    public String toString() {
        return "ResumeDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", fileName='" + fileName + '\'' +
                ", isParsed=" + isParsed +
                ", parseStatus='" + parseStatus + '\'' +
                '}';
    }
}