package pl.vemu.sociely.mappers;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.entities.UserDTO;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    private BCryptPasswordEncoder encoder;

    public User toUserWithPasswordEncryption(UserDTO userDTO) {
        if (userDTO == null) return null;

        return new User(
                userDTO.id(),
                userDTO.name(),
                userDTO.surname(),
                userDTO.email(),
                encoder.encode(userDTO.password())
        );
    }

    public abstract User toUser(UserDTO userDTO);

    public abstract UserDTO toUserDTO(User user);

    public User toUserAndCopyNonNullFields(UserDTO user, UserDTO userDTO) {
        User userToReturn = this.toUser(user);
        if (userDTO.password() != null) userToReturn.setPassword(encoder.encode(userDTO.password()));
        if (userDTO.name() != null) userToReturn.setName(userDTO.name());
        if (userDTO.surname() != null) userToReturn.setSurname(userDTO.surname());
        return userToReturn;
    }

}
