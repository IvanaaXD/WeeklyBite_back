package com.backend.weeklybite.dto.comment;

import com.backend.weeklybite.domain.enums.CommentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatedCommentDTO {

    private Long id;
    private String content;
    private Date date;
    private String userEmail;
    private CommentStatus commentStatus;
    private Long recipeId;
    private Integer rating;
}
