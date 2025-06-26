package aor.proj3t.enums;

public enum UserSortField {

    ID("id"),
    WORKPLACE("workplace"),
    MANAGER("manager"),
    NAME("firstName"),
    DATE_OF_BIRTH("birthdate"),
    DATE("createdAt");



    private final String fieldName;

    UserSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
