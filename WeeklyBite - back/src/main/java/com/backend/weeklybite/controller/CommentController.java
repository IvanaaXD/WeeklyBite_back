package com.backend.weeklybite.controller;

import com.backend.weeklybite.dto.account.GetAccountDTO;
import com.backend.weeklybite.dto.comment.*;
import com.backend.weeklybite.security.jwt.JwtTokenUtil;
import com.backend.weeklybite.service.AccountService;
import com.backend.weeklybite.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Collection<GetCommentDTO>> getAllComments() {
        Collection<GetCommentDTO> comments = commentService.getAll();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping(value = "/recipe/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GetCommentDTO>> getCommentsByRecipeId(@PathVariable("recipeId") Long recipeId) {
        Collection<GetCommentDTO> comments = commentService.getCommentsByRecipeId(recipeId);
        return new ResponseEntity<Collection<GetCommentDTO>>(comments, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreatedCommentDTO> createComment(@RequestBody CreateCommentDTO createCommentDTO, @RequestHeader("Authorization") String token) {
        GetAccountDTO user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = accountService.getUserAccountByEmail(tokenUtil.extractUsername(token));
            }
            catch (Exception ignored) {
            }
        }


        if (user != null) {
            CreatedCommentDTO createdComment = commentService.create(createCommentDTO, user.getId());
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UpdatedCommentDTO> updateComment(
            @PathVariable Long id,
            @RequestBody UpdateCommentDTO dto) {
        UpdatedCommentDTO updatedComment =  commentService.update(id, dto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
