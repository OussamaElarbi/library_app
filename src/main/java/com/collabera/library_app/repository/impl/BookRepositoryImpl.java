package com.collabera.library_app.repository.impl;

import com.collabera.library_app.model.Book;
import com.collabera.library_app.repository.BookRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class BookRepositoryImpl extends SimpleJpaRepository<Book, Long> implements BookRepository {

    private final EntityManager em;

    public BookRepositoryImpl(EntityManager em) {
        super(Book.class, em);
        this.em = em;
    }

    @Override
    public List<Book> findAllByBookCopyIsbn(String isbn) {
        return em.createQuery(
                        "select b from Book b where b.bookCopy.isbn = :isbn", Book.class)
                .setParameter("isbn", isbn)
                .getResultList();
    }

    @Override
    public long countByBookCopyIsbn(String isbn) {
        return em.createQuery(
                        "select count(b) from Book b where b.bookCopy.isbn = :isbn", Long.class)
                .setParameter("isbn", isbn)
                .getSingleResult();
    }

    @Override
    public boolean existsByBookId(Long bookId) {
        Long count = em.createQuery(
                        "select count(b) from Book b where b.bookId = :id", Long.class)
                .setParameter("id", bookId)
                .getSingleResult();
        return count != null && count > 0;
    }
}

