package pl.vemu.socialApp.managers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.vemu.socialApp.entities.User;
import pl.vemu.socialApp.entities.UserDTO;
import pl.vemu.socialApp.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManager {

    private final UserRepository userRepository;

    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAllAsDTO(pageable);
    }

    public Optional<UserDTO> findById(Long id) {
        return userRepository.findByIdAsDTO(id);
    }

    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmailAsDTO(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
