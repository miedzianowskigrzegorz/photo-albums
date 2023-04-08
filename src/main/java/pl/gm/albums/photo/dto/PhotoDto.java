package pl.gm.albums.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.gm.albums.album.dto.AlbumDto;

/**
 * A DTO (Data Transfer Object) representing a photo entity.
 * This class is used to transfer photo data between the client and the server.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDto {

    /**
     * The ID of the photo.
     */
    private Long id;

    /**
     * The path to the photo file.
     */
    private String path;

    /**
     * The name of the photo file.
     */
    private String fileName;

    /**
     * The album that this photo belongs to.
     */
    private AlbumDto album;

}
