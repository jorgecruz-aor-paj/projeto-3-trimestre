package aor.proj3t.util.converterutils;

import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

/**
 * Converter class for transforming {@link User} entities into compact DTOs.
 * <p>
 * Provides utilities to convert individual and collections of {@link User} objects
 * into {@link MiniUserDTO}, which contain only essential user details such as ID, name,
 * email, and profile photograph. These are typically used for lightweight views,
 * dropdowns, and contexts where full user data isn't necessary.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class MiniUserConverter {

    /**
     * Converts a single {@link User} entity into a {@link MiniUserDTO}.
     *
     * @param user the user entity to convert
     * @return a compact DTO with basic user info, or null if input is null
     */
    public MiniUserDTO toMiniUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new MiniUserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhotograph()
        );
    }

    /**
     * Converts a list of {@link User} entities into a list of {@link MiniUserDTO}.
     *
     * @param users the list of users to convert
     * @return a list of compact user DTOs; empty list if input is null
     */
    public List<MiniUserDTO> toMiniUserDTOList(List<User> users) {
        if (users == null) {
            return new ArrayList<>();
        }
        List<MiniUserDTO> miniUserDTOList = new ArrayList<>(users.size());
        for (User user : users) {
            miniUserDTOList.add(toMiniUserDTO(user));
        }
        return miniUserDTOList;
    }
}

