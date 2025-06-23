package aor.proj3t.filters;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.entities.User;
import aor.proj3t.entities.Workplace;
import aor.proj3t.enums.UserSortField;
import aor.proj3t.exceptions.NotFoundException;
import jakarta.ws.rs.QueryParam;

public class UserFilter {
    @QueryParam("workplace")
    private Long workplaceId;
    @QueryParam("manager")
    private Long managerId;
    @QueryParam("id")
    private Long id;
    @QueryParam("searchText")
    private String searchText;
    @QueryParam("noManager")
    private boolean noManager;
    @QueryParam("sortField")
    private UserSortField sortField = UserSortField.NAME;
    @QueryParam("ascending")
    private boolean ascending = true;

    private Workplace workplace;
    private User manager;


    public UserFilter() {
    };

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

    public Long getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(Long workplaceId) {
        this.workplaceId = workplaceId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public void hydrate(DAOFactory daoFactory) {
        if (workplaceId != null) {
            this.workplace = daoFactory.forWorkplace()
                    .findById(workplaceId)
                    .orElseThrow(() -> new NotFoundException("Workplace not found with ID " + workplaceId));
        }

        if (managerId != null) {
            this.manager = daoFactory.forUser()
                    .findById(managerId)
                    .orElseThrow(() -> new NotFoundException("Manager not found with ID " + managerId));
        }
    }

}
