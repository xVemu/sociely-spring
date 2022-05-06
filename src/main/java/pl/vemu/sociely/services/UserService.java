package pl.vemu.sociely.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.vemu.sociely.dtos.request.UserDtoRequest;
import pl.vemu.sociely.dtos.response.UserDtoResponse;
import pl.vemu.sociely.exceptions.user.UserByIdNotFound;
import pl.vemu.sociely.exceptions.user.UserWithEmailAlreadyExist;
import pl.vemu.sociely.mappers.UserMapper;
import pl.vemu.sociely.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    public Page<UserDtoResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toUserDto);
    }

    public UserDtoResponse getById(Long id) throws UserByIdNotFound {
        return repository.findById(id).map(mapper::toUserDto).orElseThrow(() -> new UserByIdNotFound(id));
    }

    public UserDtoResponse add(UserDtoRequest userDto) throws UserWithEmailAlreadyExist {
        var userByEmail = repository.findByEmail(userDto.getEmail());
        if (userByEmail.isPresent()) throw new UserWithEmailAlreadyExist(userDto.getEmail());
        var mappedUser = mapper.toUser(userDto);
        return mapper.toUserDto(repository.save(mappedUser));
    }

    public UserDtoResponse updateUser(
            Long id,
            UserDtoRequest userDto
    ) throws UserByIdNotFound, UserWithEmailAlreadyExist {
        var userFromDb = repository.findById(id).orElseThrow(() -> new UserByIdNotFound(id));

        // check if email doesn't belong to other user
        var userByEmail = repository.findByEmail(userDto.getEmail());
        if (userByEmail.isPresent() && !userByEmail.get().equals(userFromDb))
            throw new UserWithEmailAlreadyExist(userDto.getEmail());

        var updatedUser = mapper.updateUserFromUserDto(userDto, userFromDb);
        var user = repository.save(updatedUser);
        return mapper.toUserDto(user);
    }

    public void deleteById(Long id) throws UserByIdNotFound {
        repository.findById(id).orElseThrow(() -> new UserByIdNotFound(id));
        repository.deleteById(id);
    }

}
