package group5.controller;

public enum ErrorMessage {

    ERROR("Error"),
    DELETE_WATCHLIST("<html><body style='width: 200px'>" + "Failed to delete watchlist.</body></html>"),
    CREATE_WATCHLIST("<html><body style='width: 200px'>" + "Failed to create new watchlist.</body></html>"),
    NAME_CLASH("<html><body style='width: 200px'>" + "Watchlist '%s' already exists. " +
            "Please choose another name.</body></html>"),
    IMPORT_WATCHLIST("<html><body style='width: 200px'>" + "Failed to import watchlist from '%s'.</body></html>");

    private final String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(String filepath) {
        return String.format(errorMessage, filepath);
    }

}

