package aor.proj3t.filters;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.entities.User;
import aor.proj3t.entities.Workplace;
import aor.proj3t.enums.UserSortField;
import aor.proj3t.exceptions.NotFoundException;
import jakarta.ws.rs.QueryParam;

/**
 * Filter class for user queries.
 * 
 * This class encapsulates all possible filter criteria for searching and
 * filtering
 * users in the system. It supports filtering by workplace, manager, text
 * search,
 * and various user attributes, as well as sorting options.
 * 
 * <p>
 * The filter provides options to include or exclude administrators and to
 * filter users without managers. It also supports entity hydration to resolve
 * workplace and manager references from their IDs.
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * GET /api/users?workplaceId=1&managerId=5&searchText=John&sortField=NAME&ascending=true
 * </pre>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class UserFilter {

    /**
     * Filter by workplace ID.
     */
    @QueryParam("workplaceId")
    private Long workplaceId;

    /**
     * Filter by manager ID.
     */
    @QueryParam("managerId")
    private Long managerId;

    /**
     * Filter by specific user ID.
     */
    @QueryParam("id")
    private Long id;

    /**
     * Text search across user information.
     */
    @QueryParam("searchText")
    private String searchText;

    /**
     * Filter to show only users without a manager.
     */
    @QueryParam("noManager")
    private boolean noManager;

    /**
     * Include administrators in the results. Defaults to false.
     */
    @QueryParam("includeAdmin")
    private boolean includeAdmin = false;

    /**
     * Field to sort users by. Defaults to NAME.
     */
    @QueryParam("sortField")
    private UserSortField sortField = UserSortField.NAME;

    /**
     * Sort order: true for ascending, false for descending. Defaults to true.
     */
    @QueryParam("ascending")
    private boolean ascending = true;

    /**
     * Resolved workplace entity from workplaceId.
     */
    private Workplace workplace;

    /**
     * Resolved manager entity from managerId.
     */
    private User manager;

    /**
     * Default constructor for JAX-RS parameter binding.
     */
    public UserFilter() {
    };

    /**
     * Gets the resolved workplace entity.
     * 
     * @return the workplace entity, or null if not resolved or specified
     */
    public Workplace getWorkplace() {
        return workplace;
    }

    /**
     * Sets the workplace entity.
     * 
     * @param workplace the workplace entity to set
     */
    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
    }

    /**
     * Gets the resolved manager entity.
     * 
     * @return the manager entity, or null if not resolved or specified
     */
    public User getManager() {
        return manager;
    }

    /**
     * Sets the manager entity.
     * 
     * @param manager the manager entity to set
     */
    public void setManager(User manager) {
        this.manager = manager;
    }

    /**
     * Gets the user ID filter.
     * 
     * @return the user ID, or null if not specified
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the user ID filter.
     * 
     * @param id the user ID to filter by
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the search text filter.
     * 
     * @return the search text, or null if not specified
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * Sets the search text filter.
     * 
     * @param searchText the text to search for in user information
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * Checks if the filter should show only users without a manager.
     * 
     * @return true to show only users without a manager, false otherwise
     */
    public boolean isNoManager() {
        return noManager;
    }

    /**
     * Sets the no manager filter.
     * 
     * @param noManager true to show only users without a manager, false otherwise
     */
    public void setNoManager(boolean noManager) {
        this.noManager = noManager;
    }

    /**
     * Gets the sort field for users.
     * 
     * @return the sort field, defaults to NAME
     */
    public UserSortField getSortField() {
        return sortField;
    }

    /**
     * Sets the sort field for users.
     * 
     * @param sortField the sort field to use
     */
    public void setSortField(UserSortField sortField) {
        this.sortField = sortField;
    }

    /**
     * Checks the sort order.
     * 
     * @return true for ascending order, false for descending
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * Sets the sort order.
     * 
     * @param ascending true for ascending order, false for descending
     */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * Gets the workplace ID filter.
     * 
     * @return the workplace ID, or null if not specified
     */
    public Long getWorkplaceId() {
        return workplaceId;
    }

    /**
     * Sets the workplace ID filter.
     * 
     * @param workplaceId the workplace ID to filter by
     */
    public void setWorkplaceId(Long workplaceId) {
        this.workplaceId = workplaceId;
    }

    /**
     * Gets the manager ID filter.
     * 
     * @return the manager ID, or null if not specified
     */
    public Long getManagerId() {
        return managerId;
    }

    /**
     * Sets the manager ID filter.
     * 
     * @param managerId the manager ID to filter by
     */
    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    /**
     * Checks if administrators should be included in results.
     * 
     * @return true to include administrators, false otherwise
     */
    public boolean isIncludeAdmin() {
        return includeAdmin;
    }

    /**
     * Sets whether to include administrators in results.
     * 
     * @param includeAdmin true to include administrators, false otherwise
     */
    public void setIncludeAdmin(boolean includeAdmin) {
        this.includeAdmin = includeAdmin;
    }

    /**
     * Resolves entity references from their IDs.
     * 
     * <p>
     * This method fetches the actual Workplace and User entities from the database
     * based on the provided workplaceId and managerId. This allows the filter to
     * work with full entity objects rather than just IDs.
     * </p>
     * 
     * @param daoFactory the DAO factory to use for database access
     * @throws NotFoundException if a specified workplace or manager ID doesn't
     *                           exist
     */
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

    @Override
    public String toString() {
        return "UserFilter{" +
                "workplaceId=" + workplaceId +
                ", managerId=" + managerId +
                ", id=" + id +
                ", searchText='" + searchText + '\'' +
                ", noManager=" + noManager +
                ", sortField=" + sortField +
                ", ascending=" + ascending +
                ", workplace=" + workplace +
                ", manager=" + manager +
                '}';
    }
}
