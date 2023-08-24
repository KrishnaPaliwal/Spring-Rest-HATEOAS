package com.example.resthateoas.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public EntityModel<User> getUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);

        Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withSelfRel();
        Link postsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserPosts(userId)).withRel("posts");

        EntityModel<User> resource = EntityModel.of(user, selfLink, postsLink);

        return resource;
    }

    @GetMapping("/{userId}/posts")
    public CollectionModel<Post> getUserPosts(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<Post> posts = user.getPosts();

        Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).slash("posts").withSelfRel();

        CollectionModel<Post> resource = CollectionModel.of(posts, selfLink);

        return resource;
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
}
