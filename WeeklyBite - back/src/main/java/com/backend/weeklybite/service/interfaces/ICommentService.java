package com.backend.weeklybite.service.interfaces;

import com.backend.weeklybite.dto.comment.*;

import java.util.Collection;
import java.util.List;

public interface ICommentService {
    List<GetCommentDTO> getAll();

    GetCommentDTO getCommentById(Long id);

    CreatedCommentDTO create(CreateCommentDTO createCommentDTO, Long userId);

    UpdatedCommentDTO update(Long id, UpdateCommentDTO comment);

    void delete(Long id);

    Collection<GetCommentDTO> getCommentsByRecipeId(Long serviceId);
}
