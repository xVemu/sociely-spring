package pl.vemu.sociely.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.vemu.sociely.dtos.PostDTO;
import pl.vemu.sociely.entities.Post;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    @Query("SELECT new pl.vemu.sociely.dtos.PostDTO(" +
            "p.id, p.text, p.creationDate, u.id, u.name, u.surname, u.email, u.role" +
            ") from Post p join p.user u")
    Page<PostDTO> getAllAsDTOs(Pageable pageable);
}
