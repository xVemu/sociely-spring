package pl.vemu.sociely.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.vemu.sociely.dtos.UserDtoResponse;
import pl.vemu.sociely.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @Query("SELECT new pl.vemu.sociely.dtos.UserDtoResponse(id, name, surname, email) FROM User WHERE id = :id")
    Optional<UserDtoResponse> findByIdAsDTO(Long id);

    @Query("SELECT new pl.vemu.sociely.dtos.UserDtoResponse(id, name, surname, email) FROM User")
    Page<UserDtoResponse> findAllAsDTO(Pageable pageable);

    Optional<User> findByEmail(String email);
}
