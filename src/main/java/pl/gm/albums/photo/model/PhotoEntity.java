package pl.gm.albums.photo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.gm.albums.album.model.AlbumEntity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "photo")
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;

    private String fileName;
    /** This field represents the many-to-one relationship between the PhotoEntity and AlbumEntity classes */
    @ManyToOne
    private AlbumEntity album;

}
