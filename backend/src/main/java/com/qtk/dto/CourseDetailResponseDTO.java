package com.qtk.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDetailResponseDTO {
    private Integer id;
    private String title; // Map từ name
    private String description;
    private String shortDescription;
    private Integer price;
    private String thumbnail;
    
    private String categoryName; // Map từ subject name
    private String instructorName; // Map từ lecturer name
    
    private Date createdAt;
    private Date updatedAt;
    
    private Boolean isEnrolled;
    private Integer totalChapters;
    private Integer totalLessons;
    
    private List<String> objectives;
    private List<ChapterDTO> chapters;

    // DTO con bên trong để biểu diễn cấu trúc Chương -> Bài học
    @Getter
    @Setter
    @Builder
    public static class ChapterDTO {
        private Integer id;
        private String title;
        private List<LessonDTO> lessons;
    }

    @Getter
    @Setter
    @Builder
    public static class LessonDTO {
        private Integer id;
        private String title;
        private String duration;
    }
}