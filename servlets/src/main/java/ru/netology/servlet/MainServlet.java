package ru.netology.servlet;

import ru.netology.controller.PostController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private AnnotationConfigApplicationContext context;

  @Override
  public void init() {
    context = new AnnotationConfigApplicationContext("ru.netology");
    controller = context.getBean(PostController.class);
  }

  @Override
  public void destroy() {
    if (context != null) context.close();
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      if ("GET".equals(method) && "/api/posts".equals(path)) {
        controller.all(resp);
        return;
      }
      if ("GET".equals(method) && path.matches("^/api/posts/\\d+$")) {
        controller.getById(parseId(path), resp);
        return;
      }
      if ("POST".equals(method) && "/api/posts".equals(path)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if ("DELETE".equals(method) && path.matches("^/api/posts/\\d+$")) {
        controller.removeById(parseId(path), resp);
        return;
      }

      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private long parseId(String path) {
    final var parts = path.split("/");
    return Long.parseLong(parts[parts.length - 1]);
  }
}