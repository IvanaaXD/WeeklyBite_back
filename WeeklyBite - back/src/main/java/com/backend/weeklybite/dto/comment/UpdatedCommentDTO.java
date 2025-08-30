package com.backend.weeklybite.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedCommentDTO {

    private Long id;
    private String content;
    private String commentStatus;
}
