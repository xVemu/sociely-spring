package pl.vemu.sociely.managers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.entities.UserDTO;
import pl.vemu.sociely.mappers.UserMapper;
import pl.vemu.sociely.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManager {

    private final UserRepository userRepository;

    private final UserMapper mapper;

    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAllAsDTO(pageable);
    }

    public Optional<UserDTO> findById(Long id) {
        return userRepository.findByIdAsDTO(id);
    }

    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmailAsDTO(email);
    }

    public UserDTO save(User user) {
        return mapper.toUserDTO(userRepository.save(user));
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
