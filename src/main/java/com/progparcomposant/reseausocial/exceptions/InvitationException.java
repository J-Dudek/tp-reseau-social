package com.progparcomposant.reseausocial.exceptions;

public class InvitationException extends SocialNetworkException {

    public InvitationException() {
    }

    public InvitationException(String message) {
        super(message);
    }

    public InvitationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvitationException(Throwable cause) {
        super(cause);
    }

    public InvitationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
