package aor.proj3t.enums;

public enum UserSortField {

    ID("id"),
    WORKPLACE("workplace"),
    MANAGER("manager"),
    NAME("name"),
    DATE_OF_BIRTH("birth_date"),
    DATE("created_at");



    private final String fieldName;

    UserSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
