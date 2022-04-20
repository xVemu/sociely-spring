package pl.vemu.sociely.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.vemu.sociely.dtos.PostDTO;
import pl.vemu.sociely.entities.Post;
import pl.vemu.sociely.mappers.PostMapper;
import pl.vemu.sociely.repositories.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;
    private final PostMapper mapper;

    public Page<PostDTO> getPageablePosts(Pageable pageable) {
        return repository.getAllAsDTOs(pageable);
    }

    public PostDTO save(Post post) {
        return mapper.toPostDTO(repository.save(post));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
