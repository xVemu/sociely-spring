package pl.vemu.sociely.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.vemu.sociely.dtos.UserDTO;
import pl.vemu.sociely.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @Query("SELECT new pl.vemu.sociely.dtos.UserDTO(id, name, surname, email, password, role) FROM User WHERE email = ?1")
    Optional<UserDTO> findByEmailAsDTO(String email);

    @Query("SELECT new pl.vemu.sociely.dtos.UserDTO(id, name, surname, email, password, role) FROM User WHERE id = ?1")
    Optional<UserDTO> findByIdAsDTO(Long id);

    @Query("SELECT new pl.vemu.sociely.dtos.UserDTO(id, name, surname, email, password, role) FROM User")
    Page<UserDTO> findAllAsDTO(Pageable pageable);

    Optional<User> findByEmail(String email);
}
