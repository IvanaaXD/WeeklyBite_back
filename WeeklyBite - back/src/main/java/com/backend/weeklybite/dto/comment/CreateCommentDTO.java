package com.backend.weeklybite.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDTO {

    private String content;
    private String userEmail;
    private Long recipeId;
    private Integer rating;
}