package mapper;

import com.collabera.library_app.dto.BookMetaDataDto;
import com.collabera.library_app.model.BookMetaData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BookMetaDataMapper {

    public static BookMetaData toEntity(BookMetaDataDto dto) {
        if (dto == null) {
            return null;
        }
        return BookMetaData.builder().isbn(dto.isbn())
                .title(dto.title()).author(dto.author())
                .build();
    }
}
