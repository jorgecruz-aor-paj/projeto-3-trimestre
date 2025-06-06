package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.User;
import aor.proj3t.entities.Workplace;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class UserConverter implements Converter<User, UserDTO> {

    @Inject
    private EntityManager em;

    @Override
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setBiography(user.getBiography());
        userDTO.setPhotograph(user.getPhotograph());

        // Handle nullable relationships safely
        if (user.getManager() != null) {
            userDTO.setManagerId(user.getManager().getId());
        }

        if (user.getWorkplace() != null) {
            userDTO.setWorkplaceId(user.getWorkplace().getId());
        }

        return userDTO;
    }

    @Override
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        // For existing entities, we should load them first
        User user;
        if (dto.getId() != 0) {
            user = em.find(User.class, dto.getId());
            if (user == null) {
                user = new User();
            }
        } else {
            user = new User();
        }

        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(dto.getRole());
        user.setBirthDate(dto.getBirthDate());
        user.setBiography(dto.getBiography());
        user.setPhotograph(dto.getPhotograph());

        // Handle relationships
        if (dto.getManagerId() != null) {
            User manager = em.find(User.class, dto.getManagerId());
            user.setManager(manager);
        }

        if (dto.getWorkplaceId() != null) {
            Workplace workplace = em.find(Workplace.class, dto.getWorkplaceId());
            user.setWorkplace(workplace);
        }

        return user;
    }

}
