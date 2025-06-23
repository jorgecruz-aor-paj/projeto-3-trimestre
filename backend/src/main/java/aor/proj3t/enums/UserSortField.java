package aor.proj3t.enums;

public enum UserSortField {

    ID("id"),
    WORKPLACE("workplace"),
    MANAGER("manager"),
    NAME("firstName"),
    DATE_OF_BIRTH("birthDate"),
    DATE("createdAt");



    private final String fieldName;

    UserSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
