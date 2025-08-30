package com.backend.weeklybite.repository;

import com.backend.weeklybite.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Collection<Comment> findCommentByRecipeId(Long recipeId);

}
