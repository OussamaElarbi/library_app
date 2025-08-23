package mapper;

import com.collabera.library_app.dto.BookDto;
import com.collabera.library_app.model.Book;
import com.collabera.library_app.model.BookCopy;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class BookCopyMapper {

    public static BookCopy toEntity(BookDto dto) {
        if (dto == null) {
            return null;
        }
        Book book = Book.builder().id(dto.bookId()).build();
        return BookCopy.builder().isbn(dto.isbn())
                .title(dto.title()).author(dto.author())
                .build();
    }
}
