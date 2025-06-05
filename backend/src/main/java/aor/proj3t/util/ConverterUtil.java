package aor.proj3t.util;

import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.User;

import java.util.ArrayList;
import java.util.List;

public class ConverterUtil {


    public static UserDTO userEntityToUserDTO(User user) {
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
        userDTO.setManagerId(user.getManager().getId());
        userDTO.setWorkplaceId(user.getWorkplace().getId());
        userDTO.setPhotograph(user.getPhotograph());
        return userDTO;
    }

    public static List<UserDTO> userEntityListToUserDTOList(List<User> userList) {
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            userDTOList.add(userEntityToUserDTO(user));
        }
        return userDTOList;
    }
}
