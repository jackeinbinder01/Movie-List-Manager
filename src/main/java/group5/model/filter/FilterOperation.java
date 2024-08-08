package group5.model.filter;

import java.util.List;

import group5.model.MovieData;
import group5.model.beans.MBeans;

class FilterOperation {

    /**
     * a switch to manage how movies get filtered on a catagory.
     *
     * @param movie
     * @param filterOn
     * @param op
     * @param val
     * @return bool
     */
    public static boolean getFilter(MBeans movie, MovieData filterOn, Operations op, String val) {
        switch (filterOn) {
            case TITLE:
                return filterString(movie.getTitle(), op, val);
            case RELEASED:
                return filterInt(movie.getYear(), op, val);
            case MPA:
                return filterString(movie.getRated(), op, val);
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
            case WRITER:
                return filterList(movie.getWriter(), op, val);
            case BOXOFFICE:
                return filterInt(movie.getBoxOffice(), op, val);

            default:
                return true;
        }
    }

    /**
     * the filter operation for any cat with a a list.
     *
     * @param strList
     * @param op
     * @param val
     * @return bool
     */
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

    /**
     * the filter operation for any cat with type string.
     *
     * @param field
     * @param op
     * @param val
     * @return bool
     */
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

    /**
     * a filter operation for the catagpries with a intiger.
     *
     * @param field
     * @param op
     * @param val
     * @return bool
     */
    private static boolean filterInt(int field, Operations op, String val) {
        int intVal;
        try {
            intVal = Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return true;
        }

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

    /**
     * a filter operation for the catagories with a double.
     *
     * @param field
     * @param op
     * @param val
     * @return bool
     */
    private static boolean filterDouble(double field, Operations op, String val) {
        double doubleVal;
        try {
            doubleVal = Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return true;
        }

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

    /**
     * a filter operation for catagories with a double.
     *
     * @param field
     * @param op
     * @param val
     * @return bool
     */
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
