package com.project_managament.services.impl;

import com.project_managament.models.Comment;
import com.project_managament.repositories.CommentRepository;
import com.project_managament.services.CommentService;
import com.project_managament.utils.ValidationUtil;

import java.util.List;
import java.util.Optional;

public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public int addComment(Comment comment) {
        validateComment(comment);
        return commentRepository.insert(comment);
    }

    @Override
    public boolean updateComment(Comment comment) {
        validateComment(comment);
        return commentRepository.update(comment);
    }

    @Override
    public boolean deleteComment(int id) {
        ValidationUtil.requirePositive(id, "Invalid comment ID!");
        return commentRepository.delete(id);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.getAll();
    }

    @Override
    public Optional<Comment> getCommentById(int id) {
        ValidationUtil.requirePositive(id, "Invalid comment ID!");
        return Optional.ofNullable(commentRepository.getById(id));
    }

    private void validateComment(Comment comment) {
        ValidationUtil.requireNonNull(comment, "Comment cannot be null!");
        ValidationUtil.validateComment(comment.getContent(), comment.getUserId(), comment.getTaskId());
    }


}
