package pl.vemu.sociely.views;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.vemu.sociely.dtos.request.CommentDtoRequest;
import pl.vemu.sociely.dtos.request.PostDtoRequest;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.exceptions.post.PostByIdNotFound;
import pl.vemu.sociely.services.CommentService;
import pl.vemu.sociely.services.PostService;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PostView {

    private final PostService service;
    private final CommentService commentService;

    @GetMapping
    public String getPosts(Model model) {
        model.addAttribute("newPost", new PostDtoRequest());
        return "posts";
    }

    @GetMapping("{id}")
    public String getPost(@PathVariable Long id, Model model) {
        try {
            var post = service.getById(id);
            model.addAttribute("post", post);
            model.addAttribute("newComment", new CommentDtoRequest());
            return "post";
        } catch (PostByIdNotFound e) {
            return "404";
        }
    }

    @PostMapping("{id}")
    public String addComment(
            @Valid @ModelAttribute CommentDtoRequest comment,
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            BindingResult result
    ) {
        if (result.hasErrors()) return "posts";
        try {
            commentService.add(comment, id, user);
        } catch (PostByIdNotFound e) {
            return "404";
        }
        return "redirect:/{id}";
    }

    @PostMapping
    public String addPost(
            @Valid @ModelAttribute PostDtoRequest post,
            @AuthenticationPrincipal User user,
            BindingResult result
    ) {
        if (result.hasErrors()) return "posts";
        service.add(post, user);
        return "redirect:/";
    }

}
