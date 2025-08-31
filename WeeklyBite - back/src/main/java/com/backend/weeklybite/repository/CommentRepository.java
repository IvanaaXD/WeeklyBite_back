package com.backend.weeklybite.repository;

import com.backend.weeklybite.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Collection<Comment> findCommentByRecipeId(Long recipeId);

    @Query("SELECT p FROM Comment p WHERE p.commentStatus = 'PENDING'")
    List<Comment> findAllPending();
}
