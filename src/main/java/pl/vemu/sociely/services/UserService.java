package pl.vemu.sociely.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.vemu.sociely.dtos.UserDtoRequest;
import pl.vemu.sociely.dtos.UserDtoResponse;
import pl.vemu.sociely.exceptions.user.UserByIdNotFoundException;
import pl.vemu.sociely.exceptions.user.UserWithEmailAlreadyExistException;
import pl.vemu.sociely.mappers.UserMapper;
import pl.vemu.sociely.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    public Page<UserDtoResponse> getAll(Pageable pageable) {
        return repository.findAllAsDTO(pageable);
    }

    public UserDtoResponse getById(Long id) throws UserByIdNotFoundException {
        return repository.findByIdAsDTO(id).orElseThrow(() -> new UserByIdNotFoundException(id));
    }

    public UserDtoResponse add(UserDtoRequest userDto) throws UserWithEmailAlreadyExistException {
        var userByEmail = repository.findByEmail(userDto.getEmail());
        if (userByEmail.isPresent()) throw new UserWithEmailAlreadyExistException(userDto.getEmail());
        var mappedUser = mapper.toUser(userDto);
        return mapper.toUserDto(repository.save(mappedUser));
    }

    public UserDtoResponse updateUser(Long id, UserDtoRequest userDto) throws UserByIdNotFoundException, UserWithEmailAlreadyExistException {
        var userFromDb = repository.findById(id).orElseThrow(() -> new UserByIdNotFoundException(id));

        // check if email doesn't belong to other user
        var userByEmail = repository.findByEmail(userDto.getEmail());
        if (userByEmail.isPresent() && !userByEmail.get().equals(userFromDb))
            throw new UserWithEmailAlreadyExistException(userDto.getEmail());

        var updatedUser = mapper.updateUserDtoFromUserDto(userDto, userFromDb);
        var user = repository.save(updatedUser);
        return mapper.toUserDto(user);
    }

    public void delete(Long id) throws UserByIdNotFoundException {
        repository.findById(id).orElseThrow(() -> new UserByIdNotFoundException(id));
        repository.deleteById(id);
    }

}
