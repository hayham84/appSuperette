package AMSuperette;

public enum fieldType {
    NUMERIC, VARCHAR, FLOAT8, INT4;

    // Méthode pour mapper les types SQL PostgreSQL à l'énumération fieldType
    public static fieldType fromSqlType(String sqlType) {
        switch (sqlType.toUpperCase()) {
            case "INTEGER":
            case "INT4":
                return INT4;
            case "DOUBLE PRECISION":
            case "FLOAT8":
                return FLOAT8;
            case "NUMERIC":
                return NUMERIC;
            case "VARCHAR":
            case "CHARACTER VARYING":
                return VARCHAR;
            default:
                throw new IllegalArgumentException("Type SQL non supporté : " + sqlType);
        }
    }
}