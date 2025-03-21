package com.project_managament.services;

import com.project_managament.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    int addComment(Comment comment);
    boolean updateComment(Comment comment);
    boolean deleteComment(int id);
    List<Comment> getAllComments();
    Optional<Comment> getCommentById(int id);


}
