package pl.vemu.socialapp.mappers;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.vemu.socialapp.entities.User;
import pl.vemu.socialapp.entities.UserDTO;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    private BCryptPasswordEncoder encoder;

    public abstract User toUser(UserDTO userDTO);

    public abstract UserDTO toUserDTO(User user);

    public User toUserAndCopyNonNullFields(UserDTO userDTO, UserDTO user) {
        User userToReturn = new User(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getPassword());
        if (userDTO.getPassword() != null) userToReturn.setPassword(encoder.encode(userDTO.getPassword()));
        if (userDTO.getEmail() != null) userToReturn.setEmail(userDTO.getEmail());
        if (userDTO.getName() != null) userToReturn.setName(userDTO.getName());
        if (userDTO.getSurname() != null) userToReturn.setSurname(userDTO.getSurname());
        return userToReturn;
    }

}
