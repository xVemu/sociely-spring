package pl.vemu.sociely.views;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.vemu.sociely.dtos.PostDTO;
import pl.vemu.sociely.entities.Post;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.mappers.PostMapper;
import pl.vemu.sociely.services.PostService;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostView {

    private final PostService service;
    private final PostMapper mapper;

    @GetMapping()
    public String getPosts(
            @PageableDefault(size = 3)
            @SortDefault.SortDefaults(
                    @SortDefault(sort = "creationDate", direction = Sort.Direction.ASC)
            ) Pageable pageable,
            Model model
    ) {
        model.addAttribute("posts", service.getPageablePosts(pageable));
        model.addAttribute("post", new PostDTO());
        return "posts";
    }

    @PostMapping()
    public String addPost(@ModelAttribute PostDTO postDTO, @AuthenticationPrincipal User principal, BindingResult errors, Model model, HttpSession session) {
        Post newPost = mapper.toPost(postDTO);
        newPost.setUser(principal);
        newPost.setCreationDate(new Date());
        service.save(newPost);
        return "redirect:/posts";
    }

}
