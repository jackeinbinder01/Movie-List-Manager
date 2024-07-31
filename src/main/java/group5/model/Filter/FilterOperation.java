package group5.model.Filter;

import java.util.List;

import group5.model.MovieData;
import group5.model.beans.MBeans;

public class FilterOperation {

    public static boolean getFilter(MBeans movie, MovieData filterOn, Operations op, String val) {
        switch (filterOn) {
            case TITLE:
                return filterString(movie.getTitle(), op, val);
            case RELEASED:
                return filterInt(movie.getYear(), op, val);
            case MPA:
                return filterDouble(movie.getMetascore(), op, val);
            case GENRE:
                return filterList(movie.getGenre(), op, val);
            case RUNTIME:
                return filterInt(movie.getRuntime(), op, val);
            case DIRECTOR:
                return filterList(movie.getDirector(), op, val);
            case ACTOR:
                return filterList(movie.getActors(), op, val);
            case LANGUAGE:
                return filterList(movie.getLanguage(), op, val);
            case IMDB:
                return filterDouble(movie.getImdbRating(), op, val);
            case USER:
                return filterDouble(movie.getMyRating(), op, val);
            case HASWATCHED:
                return filterBoolean(movie.getWatched(), op, val);
            default:
                return true;
        }
    }

    private static boolean filterList(List<String> strList, Operations op, String val) {
        boolean returnBool = false;
        for (int index = 0; index < strList.size(); index++) {
            switch (op) {
                case CONTAINS:
                    if (strList.get(index).toLowerCase().contains(val.toLowerCase())) {
                        returnBool = true;
                    }
                    break;
                case EQUALS:
                    if (strList.get(index).equalsIgnoreCase(val)) {
                        returnBool = true;
                    }
                    break;
                default:
                    returnBool = false;
                    break;
            }
        }
        return returnBool;
    }

    private static boolean filterString(String field, Operations op, String val) {
        switch (op) {
            case EQUALS:
                return field.equalsIgnoreCase(val);
            case CONTAINS:
                return field.toLowerCase().contains(val.toLowerCase());
            default:
                return false;
        }
    }

    private static boolean filterInt(int field, Operations op, String val) {
        int intVal = Integer.parseInt(val);
        switch (op) {
            case EQUALS:
                return field == intVal;
            case GREATERTHAN:
                return field > intVal;
            case GREATEROREQUAL:
                return field >= intVal;
            case LESSTHAN:
                return field < intVal;
            case LESSOREQUAL:
                return field <= intVal;
            default:
                return false;
        }
    }

    private static boolean filterDouble(double field, Operations op, String val) {
        double doubleVal = Double.parseDouble(val);
        switch (op) {
            case EQUALS:
                return field == doubleVal;
            case GREATERTHAN:
                return field > doubleVal;
            case GREATEROREQUAL:
                return field >= doubleVal;
            case LESSTHAN:
                return field < doubleVal;
            case LESSOREQUAL:
                return field <= doubleVal;
            default:
                return false;
        }
    }

    private static boolean filterBoolean(boolean field, Operations op, String val) {
        boolean boolVal = Boolean.parseBoolean(val);
        switch (op) {
            case EQUALS:
                return field == boolVal;
            default:
                return false;
        }
    }
}
