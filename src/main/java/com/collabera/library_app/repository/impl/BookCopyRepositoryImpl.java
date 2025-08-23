package com.collabera.library_app.repository.impl;

import com.collabera.library_app.model.BookCopy;
import com.collabera.library_app.repository.BookCopyRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class BookCopyRepositoryImpl extends SimpleJpaRepository<BookCopy, String> implements BookCopyRepository {

    private final EntityManager entityManager;

    public BookCopyRepositoryImpl(EntityManager entityManager) {
        super(BookCopy.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookCopy> findByIsbn(String isbn) {
        return Optional.ofNullable(
                entityManager.find(BookCopy.class, isbn)
        );
    }

    @Override
    public List<BookCopy> findByTitleContainingIgnoreCase(String titlePart) {
        return entityManager.createQuery(
                        "select bc from BookCopy bc " +
                                "where lower(bc.title) like lower(concat('%', :titlePart, '%'))",
                        BookCopy.class)
                .setParameter("titlePart", titlePart)
                .getResultList();
    }

    @Override
    public List<BookCopy> findByAuthorContainingIgnoreCase(String authorPart) {
        return entityManager.createQuery(
                        "select bc from BookCopy bc " +
                                "where lower(bc.author) like lower(concat('%', :authorPart, '%'))",
                        BookCopy.class)
                .setParameter("authorPart", authorPart)
                .getResultList();
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        Long count = entityManager.createQuery(
                        "select count(bc) from BookCopy bc where bc.isbn = :isbn",
                        Long.class)
                .setParameter("isbn", isbn)
                .getSingleResult();
        return count != null && count > 0;
    }
}
