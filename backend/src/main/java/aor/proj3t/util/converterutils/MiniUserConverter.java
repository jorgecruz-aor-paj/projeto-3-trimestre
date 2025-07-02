package aor.proj3t.util.converterutils;

import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MiniUserConverter {

    public MiniUserDTO toMiniUserDTO(User user) {
        return new MiniUserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public List<MiniUserDTO> toMiniUserDTOList(List<User> users) {
        List<MiniUserDTO> miniUserDTOList = new ArrayList<>(users.size());
        for (User user : users) {
            miniUserDTOList.add(toMiniUserDTO(user));
        }
        return miniUserDTOList;
    }
}
