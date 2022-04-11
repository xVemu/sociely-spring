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
    @Mapping(target = "password", expression = "java(encoder.encode(userDTO.password()))")
    public abstract User toUserWithPasswordEncryption(UserDTO userDTO);

    public abstract User toUser(UserDTO userDTO);

    public abstract UserDTO toUserDTO(User user);

    //    TODO?
    @InheritConfiguration(name = "toUserWithPasswordEncryption")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateUserFromUserDto(UserDTO userDTO, @MappingTarget UserDTO user);

}
