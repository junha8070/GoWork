package com.example.gowork.dto;

import com.example.gowork.view_Community.PostViewType;

import java.util.ArrayList;

public class Post_View_DTO {

    PostDTO postDTO;
    CommentDTO commentDTO;
    int postViewType;

    public PostDTO getPostDTO() {
        return postDTO;
    }

    public void setPostDTO(PostDTO postDTO) {
        this.postDTO = postDTO;
    }

    public CommentDTO getCommentDTO() {
        return commentDTO;
    }

    public void setCommentDTO(CommentDTO commentDTO) {
        this.commentDTO = commentDTO;
    }

    public int getPostViewType() {
        return postViewType;
    }

    public void setPostViewType(int postViewType) {
        this.postViewType = postViewType;
    }

    public Post_View_DTO(PostDTO postDTO, CommentDTO commentDTO, int postViewType) {
        this.postDTO = postDTO;
        this.commentDTO = commentDTO;
        this.postViewType = postViewType;
    }
}
