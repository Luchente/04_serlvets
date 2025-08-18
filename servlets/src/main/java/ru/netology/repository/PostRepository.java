package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
  private final ConcurrentHashMap<Long, Post> storage = new ConcurrentHashMap<>();
  private final AtomicLong seq = new AtomicLong(0);

  public List<Post> all() {
    // копия, чтобы не «протекали» внутренности
    return new ArrayList<>(storage.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(storage.get(id));
  }

  public Post save(Post post) {
    final long id = post.getId();
    if (id == 0) {
      final long newId = seq.incrementAndGet();
      Post toSave = new Post(newId, post.getContent());
      storage.put(newId, toSave);
      return toSave;
    }
    // Атомарное обновление, если запись существует
    return storage.compute(id, (k, existing) -> {
      if (existing == null) {
        throw new NotFoundException("Post with id=" + id + " not found");
      }
      existing.setContent(post.getContent());
      return existing;
    });
  }

  public void removeById(long id) {
    storage.remove(id);
  }
}