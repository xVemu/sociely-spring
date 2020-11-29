package pl.vemu.socialApp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.vemu.socialApp.entities.User;
import pl.vemu.socialApp.entities.UserDTO;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @Query("SELECT new pl.vemu.socialApp.entities.UserDTO(u.id, u.name, u.surname, u.email, u.password) FROM User u WHERE u.email = ?1")
    Optional<UserDTO> findByEmailAsDTO(String email);

    @Query("SELECT new pl.vemu.socialApp.entities.UserDTO(u.id, u.name, u.surname, u.email, u.password) FROM User u WHERE u.id = ?1")
    Optional<UserDTO> findByIdAsDTO(Long id);

    @Query("SELECT new pl.vemu.socialApp.entities.UserDTO(u.id, u.name, u.surname, u.email, u.password) FROM User u")
    Page<UserDTO> findAllAsDTO(Pageable pageable);
}
