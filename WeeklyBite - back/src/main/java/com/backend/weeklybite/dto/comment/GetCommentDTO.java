package com.backend.weeklybite.dto.comment;

import com.backend.weeklybite.domain.enums.CommentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentDTO {

    private Long id;
    private String content;
    private LocalDate dateCreated;
    private CommentStatus commentStatus;
    private String userFullName;
    private String userEmail;
    private String recipeName;
}
