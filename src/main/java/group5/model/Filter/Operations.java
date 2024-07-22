package group5.model.Filter;

public enum Operations {
    /**
     * the equals operator.
     */

    EQUALS("=="),
    /**
     * the contains op.
     */
    CONTAINS("~="),
    /**
     * the not equal op.
     */
    NOTEQUAL("!="),
    /**
     * the greaterthan op.
     */
    GREATERTHAN(">"),
    /**
     * lessthan op.
     */
    LESSTHAN("<"),
    /**
     * greater or equal op.
     */
    GREATEROREQUAL(">="),
    /**
     * less or equal op.
     */
    LESSOREQUAL("<=");
    /**
     * the string of an op.
     */
    private final String operator;

    /**
     * Handles the operations.
     *
     * @param value
     */
    Operations(String value) {
        this.operator = value;
    }

    /**
     * gets the operator.
     *
     * @return operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     *
     * @param str
     * @return Operations type
     */
    public static Operations getOperatorFromStr(String str) {

        if (str.contains(">=")) {
            return Operations.GREATEROREQUAL;
        } else if (str.contains("<=")) {
            return Operations.LESSOREQUAL;
        } else if (str.contains(">")) {
            return Operations.GREATERTHAN;
        } else if (str.contains("<")) {
            return Operations.LESSTHAN;
        } else if (str.contains("==")) {
            return Operations.EQUALS;
        } else if (str.contains("!")) {
            return Operations.NOTEQUAL;
        } else if (str.contains("~")) {
            return Operations.CONTAINS;
        } else {
            throw new IllegalArgumentException("unknown operator");
        }

    }
}
