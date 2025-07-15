package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;
/**
 * Data Transfer Object for manager-user assignment operations.
 * <p>
 * Represents the payload required to assign one manager to a group of users.
 * Commonly used in administrative endpoints for organizational linking,
 * hierarchy updates, or role delegation. JAXB annotations enable XML-based integration.
 * </p>
 *
 * Attributes:
 * - {@code managerId}: Unique identifier of the manager being assigned
 * - {@code userIds}: List of user IDs that will be assigned to this manager
 *
 * Example usage:
 * <pre>{@code
 *     ManagerAssignDTO dto = new ManagerAssignDTO();
 *     dto.setManagerId(42L);
 *     dto.setUserIds(Arrays.asList(101L, 102L, 103L));
 * }</pre>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "managerAssign")
@XmlAccessorType(XmlAccessType.FIELD)
public class ManagerAssignDTO {

    /** Unique identifier of the manager to be assigned */
    @XmlElement(name = "managerId")
    private Long managerId;

    /** List of user IDs to assign to the manager */
    @XmlElement(name = "userIds")
    private List<Long> userIds;

    public ManagerAssignDTO() {}

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}

