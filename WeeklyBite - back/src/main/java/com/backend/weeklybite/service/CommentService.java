package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.Comment;
import com.backend.weeklybite.domain.Person;
import com.backend.weeklybite.domain.enums.CommentStatus;
import com.backend.weeklybite.dto.comment.*;
import com.backend.weeklybite.repository.AccountRepository;
import com.backend.weeklybite.repository.CommentRepository;
import com.backend.weeklybite.repository.PersonRepository;
import com.backend.weeklybite.repository.RecipeRepository;
import com.backend.weeklybite.service.interfaces.ICommentService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private CommentRepository allComments;

    @Autowired
    private PersonRepository allPersons;

    @Autowired
    private RecipeRepository allRecipes;

    @Autowired
    private AccountRepository allAccounts;

    @Autowired
    private ModelMapper modelMapper;

    public CommentService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<GetCommentDTO> getAll() {
        List<Comment> comments = allComments.findAllPending();

        return comments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GetCommentDTO getCommentById(Long id) {
        Comment comment = allComments.findById(id).orElse(null);
        return mapToDTO(comment);
    }

    @Override
    public CreatedCommentDTO create(CreateCommentDTO createCommentDTO, Long userId) {

        Comment newComment = new Comment();

        newComment.setContent(createCommentDTO.getContent());
        newComment.setCommentStatus(CommentStatus.PENDING);
        newComment.setDateCreated(LocalDate.now());
        newComment.setRating(createCommentDTO.getRating());
        newComment.setUser(allAccounts.findById(userId).orElse(null));

        if (createCommentDTO.getRecipeId() != null) {
            allRecipes.findById(createCommentDTO.getRecipeId())
                    .ifPresent(newComment::setRecipe);
        }

        Comment createdComment = allComments.save(newComment);
        return modelMapper.map(createdComment, CreatedCommentDTO.class);
    }

    @Override
    public UpdatedCommentDTO update(Long id, UpdateCommentDTO comment) {
        Comment updateComment = allComments.findById(id).orElse(null);

        if (updateComment == null) {
            return null;
        }

        updateComment.setCommentStatus(CommentStatus.valueOf(comment.getCommentStatus()));

        Comment updatedComment = allComments.save(updateComment);
        return modelMapper.map(updatedComment, UpdatedCommentDTO.class);
    }

    @Override
    public void delete(Long id) {
        Comment comment = allComments.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        comment.setCommentStatus(CommentStatus.DELETED);
        allComments.save(comment);
    }
    public GetCommentDTO mapToDTO(Comment comment) {
        GetCommentDTO dto = new GetCommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setDateCreated(comment.getDateCreated());
        dto.setCommentStatus(comment.getCommentStatus());

        dto.setUserEmail(comment.getUser().getEmail());
        dto.setRecipeName(comment.getRecipe() != null ? comment.getRecipe().getName() : null);

        Person person = allPersons.findByUserAccountEmail(comment.getUser().getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Person not found for email: " + comment.getUser().getEmail()));

        dto.setUserFullName(person.getFirstName() + " " + person.getLastName());

        return dto;
    }

    @Override
    public Collection<GetCommentDTO> getCommentsByRecipeId(Long serviceId) {
        Collection<Comment> comments = allComments.findCommentByRecipeId(serviceId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, GetCommentDTO.class))
                .collect(Collectors.toList());
    }
}
