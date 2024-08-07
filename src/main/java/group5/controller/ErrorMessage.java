package group5.controller;

/**
 * Enum containing error messages
 */
public enum ErrorMessage {

    /** ErrorMessage Enums. */
    ERROR("Error"),
    DELETE_WATCHLIST("<html><body style='width: 200px'>" + "Failed to delete watchlist.</body></html>"),
    CREATE_WATCHLIST("<html><body style='width: 200px'>" + "Failed to create new watchlist.</body></html>"),
    NAME_CLASH("<html><body style='width: 200px'>" + "Watchlist '%s' already exists. " +
            "Please choose another name.</body></html>"),
    IMPORT_WATCHLIST("<html><body style='width: 200px'>" + "Failed to import watchlist from '%s'.</body></html>");

    /** String containing the content of an ErrorMessage. */
    private final String errorMessage;

    /** Public constructor. */
    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Formats ErrorMessage content to include the name of a file
     * @param fileName The name of the file triggering an ErrorMessage
     * @return The formatted content of an ErrorMessage, including the fileName
     */
    public String getErrorMessage(String fileName) {
        return String.format(errorMessage, fileName);
    }

}

