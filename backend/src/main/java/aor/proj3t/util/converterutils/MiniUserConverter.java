package aor.proj3t.util.converterutils;

import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MiniUserConverter {

    public MiniUserDTO toMiniUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new MiniUserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhotograph());
    }

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
