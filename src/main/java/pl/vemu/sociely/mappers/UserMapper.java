package pl.vemu.sociely.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.vemu.sociely.dtos.UserDtoPatch;
import pl.vemu.sociely.dtos.UserDtoRequest;
import pl.vemu.sociely.dtos.UserDtoResponse;
import pl.vemu.sociely.entities.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    @Autowired
    protected BCryptPasswordEncoder encoder;

    public abstract User toUser(UserDtoRequest userDtoRequest);

    public abstract UserDtoResponse toUserDto(User user);

    @AfterMapping
    protected void encryptPassword(@MappingTarget User user) {
        user.setPassword(encoder.encode(user.getPassword()));
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract User updateUserDtoFromUserDto(UserDtoPatch userDtoPatch, @MappingTarget User user);

}
