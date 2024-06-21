package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public List<Post> findAll(
            @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(value = "sort", defaultValue = "desc", required = false) String sort) {
        return postService.findAllPosts(size, sort, from);
    }

    @GetMapping("/post/{id}")
    public Post getPostById(@PathVariable("id") long id) {
        return postService.getPostById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.updatePost(newPost);
    }

}
