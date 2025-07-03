package aor.proj3t.util.converterutils;

import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.User;
import aor.proj3t.entities.Workplace;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Converter for User entities and DTOs.
 * 
 * This class handles the bidirectional conversion between {@link User} entities
 * and
 * {@link UserDTO} data transfer objects. It manages the complex relationships
 * between
 * users, such as manager-subordinate relationships and workplace associations.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * 
 * @version 1.0
 */
@ApplicationScoped
public class UserConverter extends AbstractConverter<User, UserDTO, Long> {

    @Inject
    WorkplaceConverter workplaceConverter;

    @Inject
    MiniUserConverter miniUserConverter;
    @Inject
    private ConverterFactory converterFactory;

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected User createEntityInstance() {
        return new User();
    }

    @Override
    protected UserDTO createDTOInstance() {
        return new UserDTO();
    }

    @Override
    protected Long getIdFromDTO(UserDTO dto) {
        return dto.getId() != 0 ? dto.getId() : null;
    }

    @Override
    protected void mapEntityToDTO(User user, UserDTO dto) {
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAddress(user.getAddress());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setBirthdate(user.getBirthdate());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setBiography(user.getBiography());
        dto.setPhotograph(user.getPhotograph());

        // Handle nullable relationships safely
        if (user.getManager() != null) {
            MiniUserDTO mini = new MiniUserDTO(
                    user.getManager().getId(),
                    user.getManager().getFirstName(),
                    user.getManager().getLastName(),
                    user.getManager().getEmail(),
                    user.getManager().getPhotograph());
            dto.setManager(mini);
        }

        if (user.getWorkplace() != null) {
            dto.setWorkplace(workplaceConverter.toDTO(user.getWorkplace()));
        }
    }

    @Override
    protected void mapDTOToEntity(UserDTO dto, User user) {
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(dto.getRole());
        user.setBirthdate(dto.getBirthdate());
        user.setBiography(dto.getBiography());
        user.setPhotograph(dto.getPhotograph());

        // Handle relationships using findSafely
        User manager = findSafely(User.class, dto.getManager());
        user.setManager(manager);

        Workplace workplace = findSafely(Workplace.class, dto.getWorkplace());
        user.setWorkplace(workplace);
    }

}
