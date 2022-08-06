package pl.vemu.sociely.views;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.vemu.sociely.dtos.request.PostDtoRequest;
import pl.vemu.sociely.entities.User;
import pl.vemu.sociely.services.PostService;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PostView {

    private final PostService service;

    @GetMapping
    public String getPosts(Model model) {
        model.addAttribute("newPost", new PostDtoRequest());
        return "posts";
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
