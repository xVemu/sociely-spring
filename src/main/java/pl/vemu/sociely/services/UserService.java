package pl.vemu.sociely.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.entities.dtos.UserDTO;
import pl.vemu.sociely.mappers.UserMapper;
import pl.vemu.sociely.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    public Page<UserDTO> findAll(Pageable pageable) {
        return repository.findAllAsDTO(pageable);
    }

    public Optional<UserDTO> findById(Long id) {
        return repository.findByIdAsDTO(id);
    }

    public Optional<UserDTO> findByEmail(String email) {
        return repository.findByEmailAsDTO(email);
    }

    public UserDTO save(User user) {
        return mapper.toUserDTO(repository.save(user));
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
