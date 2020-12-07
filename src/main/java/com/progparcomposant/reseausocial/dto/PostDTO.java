package com.progparcomposant.reseausocial.dto;

import java.util.Date;

public class PostDTO {

    private Long idPost;
    private String message;
    private Date publicationDate;
    private boolean isPublic;

    public PostDTO() {
    }

    public PostDTO(Long idPost, String message, Date publicationDate, boolean isPublic) {
        this.idPost = idPost;
        this.message = message;
        this.publicationDate = publicationDate;
        this.isPublic = isPublic;
    }

    public Long getIdPost() {
        return idPost;
    }

    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
