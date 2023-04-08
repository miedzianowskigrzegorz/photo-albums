package pl.gm.albums.album.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.gm.albums.photo.model.PhotoEntity;

import java.util.ArrayList;
import java.util.List;

/**

 Entity representing an album in the database.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Album")
public class AlbumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    /** Define a one-to-one relationship with the PhotoEntity table and specify the cascade type to remove */
    @OneToOne(cascade = CascadeType.REMOVE)
    private PhotoEntity mainPhoto;
    /** Define a one-to-many relationship with the PhotoEntity table, using FetchType.EAGER to load all photos at once */
    @OneToMany(fetch = FetchType.EAGER)
    private List<PhotoEntity> photos = new ArrayList<>();

}
