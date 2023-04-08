package pl.gm.albums.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gm.albums.photo.model.PhotoEntity;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity,Long> {
}
