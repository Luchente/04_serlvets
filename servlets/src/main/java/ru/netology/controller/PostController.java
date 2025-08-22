package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.service.PostService;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;

@Component
public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private final PostService service;
  private final Gson gson = new Gson();

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    response.getWriter().print(gson.toJson(service.all()));
  }

  public void getById(long id, HttpServletResponse response) throws IOException {
    try {
      response.setContentType(APPLICATION_JSON);
      response.getWriter().print(gson.toJson(service.getById(id)));
    } catch (NotFoundException e) {
      response.setStatus(SC_NOT_FOUND);
      response.getWriter().print("{\"error\":\"not found\"}");
    }
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var post = gson.fromJson(body, Post.class);
    final var saved = service.save(post);
    response.getWriter().print(gson.toJson(saved));
  }

  public void removeById(long id, HttpServletResponse response) throws IOException {
    service.removeById(id);
    response.setStatus(SC_NO_CONTENT);
  }
}
