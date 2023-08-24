package com.example.resthateoas.book;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private List<Book> bookList = new ArrayList<>();

    // Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookList;
    }

    // Get a specific book by ID
    @GetMapping("/{id}")
    public EntityModel<Book> getBook(@PathVariable int id) {
        Book book = findBookById(id);

        if (book == null) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }

        // Create HATEOAS links
        EntityModel<Book> resource = EntityModel.of(book);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getBook(id)).withSelfRel();
        resource.add(selfLink);

        return resource;
    }

    // Create a new book
    @PostMapping
    public void addBook(@RequestBody Book book) {
        bookList.add(book);
    }

    // Update a book
    @PutMapping("/{id}")
    public void updateBook(@PathVariable int id, @RequestBody Book updatedBook) {
        Book book = findBookById(id);

        if (book == null) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }

        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
    }

    // Delete a book
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable int id) {
        Book book = findBookById(id);

        if (book == null) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }

        bookList.remove(book);
    }

    // Helper method to find a book by ID
    private Book findBookById(int id) {
        return bookList.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
