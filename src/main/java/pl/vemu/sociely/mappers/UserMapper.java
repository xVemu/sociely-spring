package pl.vemu.sociely.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.vemu.sociely.dtos.UserDTO;
import pl.vemu.sociely.entities.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected BCryptPasswordEncoder encoder;

    //    java: Unmapped target property: "posts". TODO
    public abstract User toUser(UserDTO userDTO);

    public abstract UserDTO toUserDTO(User user);

    @AfterMapping
    protected void encryptPassword(@MappingTarget User user) {
        user.setPassword(encoder.encode(user.getPassword()));
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract User updateUserDtoFromUserDto(UserDTO userDTO, @MappingTarget User user);

}
