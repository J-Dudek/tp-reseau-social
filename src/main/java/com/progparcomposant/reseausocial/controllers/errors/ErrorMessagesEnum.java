package com.progparcomposant.reseausocial.controllers.errors;

public enum ErrorMessagesEnum {

    // User entity error messages
    USER_NOT_FOUND("aucun user trouvé"),
    USER_NO_USERS_IN_DATABASE("aucun users dans la base de données"),
    USER_NO_USER_WITH_THAT_NAME("aucun user avec ce nom"),

    // Post entity error messages
    POST_NOT_FOUND("aucun post trouvé"),
    POST_NO_POSTS_IN_DATABASE("aucun posts dans la base de données"),
    POST_NO_POST_YET("ce user n'a ecrit aucun post"),

    // Invitation Entity error messages
    INVITATION_NOT_FOUND("aucune invitation trouvée"),
    INVITATION_NO_INVITATION_YET("ce user n'a pas reçu d'invitation"),

    // Friendship Entity error messages
    FRIENDSHIP_NOT_FOUND("aucune amitiée trouvée"),

    // Main Controller error messages
    AUTH_EMAIL_ALREADY_ASSIGNED("Email already assigned to an account"),
    AUTH_ERROR("authentification error");

    private final String errorMessage;

    ErrorMessagesEnum(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}