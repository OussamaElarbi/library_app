package mapper;

import com.collabera.library_app.dto.BookDto;
import com.collabera.library_app.model.Book;
import com.collabera.library_app.model.BookMetaData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BookMapper {

    public static Book toEntity(BookDto dto) {
        if (dto == null) {
            return null;
        }

        return Book.builder()
                .bookMetaData(BookMetaData.builder().isbn(dto.isbn())
                        .title(dto.title()).author(dto.author()).build()).build();
    }
}
