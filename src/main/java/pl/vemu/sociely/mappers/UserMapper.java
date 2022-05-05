package pl.vemu.sociely.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.vemu.sociely.dtos.request.UserDtoRequest;
import pl.vemu.sociely.dtos.response.UserDtoResponse;
import pl.vemu.sociely.entities.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder encoder;

    public final void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public abstract User toUser(UserDtoRequest userDto);

    public abstract UserDtoResponse toUserDto(User user);

    @AfterMapping
    protected void encryptPassword(@MappingTarget User user) {
        user.setPassword(encoder.encode(user.getPassword()));
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract User updateUserFromUserDto(UserDtoRequest userDto, @MappingTarget User user);

}
