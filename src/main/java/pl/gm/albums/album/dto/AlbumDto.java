package pl.gm.albums.album.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.gm.albums.photo.dto.PhotoDto;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an Album DTO (Data Transfer Object).
 * It contains basic information about an album, including its ID, title, main photo, and list of photos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDto {

    /**
     * The ID of the album.
     */
    private Long id;

    /**
     * The title of the album. This field is required and cannot be blank.
     */
    @NotBlank(message = "Title is required")
    private String title;

    /**
     * The main photo of the album.
     */
    private PhotoDto mainPhoto;

    /**
     * The list of photos in the album.
     */
    private List<PhotoDto> photos = new ArrayList<>();
}
