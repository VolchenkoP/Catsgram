package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;

    public List<Post> findAllPosts(int size, String sort, int from) {
        if (size <= 0) {
            throw new ConditionsNotMetException("Параметр" + size + " должен быть больше 0");
        }
        if (from < 0) {
            throw new ConditionsNotMetException("Параметр from должен быть больше или равено 0");
        }
        if (!(SortOrder.from(sort).equals(SortOrder.ASCENDING) || SortOrder.from(sort).equals(SortOrder.DESCENDING))) {
            throw new ConditionsNotMetException(sort + "не может быть параметром сортировки, задайте asc или desc");
        }
        return posts.values()
                .stream()
                .sorted((p0, p1) -> {
                    int comp = p0.getPostDate().compareTo(p1.getPostDate());
                    if (SortOrder.from(sort).equals(SortOrder.DESCENDING)) {
                        comp = -1 * comp;
                    }
                    return comp;
                }).skip(from).limit(size).collect(Collectors.toList());
    }

    public Post createPost(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        /* if (userService.findUserById(post.getAuthorId()) == null) {
            throw new ConditionsNotMetException("Автор с Id = " + post.getAuthorId() + " не найден");
        } */
        userService.findUserById(post.getAuthorId());
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post updatePost(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    public Post getPostById(long id) {
        return posts.values()
                .stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Post с id = " + id + " не найден"));
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
