package pl.vemu.sociely.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.vemu.sociely.dtos.UserDTO;
import pl.vemu.sociely.exceptions.user.UserByIdNotFoundException;
import pl.vemu.sociely.exceptions.user.UserWithEmailAlreadyExistException;
import pl.vemu.sociely.mappers.UserMapper;
import pl.vemu.sociely.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    public Page<UserDTO> getAll(Pageable pageable) {
        return repository.findAllAsDTO(pageable);
    }

    public UserDTO getById(Long id) throws UserByIdNotFoundException {
        return repository.findByIdAsDTO(id).orElseThrow(() -> new UserByIdNotFoundException(id));
    }

    public UserDTO add(UserDTO userDTO) throws UserWithEmailAlreadyExistException {
        var userByEmail = repository.findByEmail(userDTO.email());
        if (userByEmail.isPresent()) throw new UserWithEmailAlreadyExistException(userDTO.email());
        var mappedUser = mapper.toUser(userDTO);
        return mapper.toUserDTO(repository.save(mappedUser));
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) throws UserByIdNotFoundException {
        var userFromDb = repository.findById(id).orElseThrow(() -> new UserByIdNotFoundException(id));
        var updatedUser = mapper.updateUserDtoFromUserDto(userDTO, userFromDb);
        var user = repository.save(updatedUser);
        return mapper.toUserDTO(user);
    }

    public void delete(Long id) throws UserByIdNotFoundException {
        repository.findById(id).orElseThrow(() -> new UserByIdNotFoundException(id));
        repository.deleteById(id);
    }

}
