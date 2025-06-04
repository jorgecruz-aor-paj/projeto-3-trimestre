package aor.proj3t.filters;

import aor.proj3t.entities.User;
import aor.proj3t.entities.Workplace;
import aor.proj3t.enums.UserSortField;

public class UserFilter {
    private Workplace workplace;
    private User manager;
    private Long id;
    private String searchText;
    private boolean noManager;
    private UserSortField sortField = UserSortField.NAME;
    private boolean ascending = true;

    public UserFilter(){};

    public Workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public boolean isNoManager() {
        return noManager;
    }

    public void setNoManager(boolean noManager) {
        this.noManager = noManager;
    }

    public UserSortField getSortField() {
        return sortField;
    }

    public void setSortField(UserSortField sortField) {
        this.sortField = sortField;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }
}
