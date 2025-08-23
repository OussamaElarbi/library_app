package com.collabera.library_app.service.impl;

import com.collabera.library_app.dto.BookDto;
import com.collabera.library_app.model.BookCopy;
import com.collabera.library_app.repository.BookCopyRepository;
import com.collabera.library_app.repository.BookRepository;
import com.collabera.library_app.service.BookService;
import mapper.BookCopyMapper;
import mapper.BookMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookCopyRepository bookCopyRepository;

    public BookServiceImpl(BookRepository bookRepository, BookCopyRepository bookCopyRepository) {
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    @Override
    @Transactional
    public BookDto registerBook(BookDto bookDto) {
        BookCopy bookDto1 = bookCopyRepository.save(BookCopyMapper.toEntity(bookDto));
        return BookDto.fromEntity(bookRepository.save(BookMapper.toEntity(bookDto)));
    }
}
