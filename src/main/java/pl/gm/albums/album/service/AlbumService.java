package pl.gm.albums.album.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import pl.gm.albums.album.dto.AlbumDto;
import pl.gm.albums.album.model.AlbumEntity;
import pl.gm.albums.album.repository.AlbumRepository;
import pl.gm.albums.photo.dto.PhotoDto;
import pl.gm.albums.photo.model.PhotoEntity;
import pl.gm.albums.photo.repository.PhotoRepository;
import pl.gm.albums.photo.service.PhotoService;

import java.util.List;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    private final PhotoService photoService;
    private final ModelMapper modelMapper;

    public AlbumService(AlbumRepository albumRepository, PhotoRepository photoRepository, PhotoService photoService, ModelMapper modelMapper) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
        this.photoService = photoService;
        this.modelMapper = modelMapper;
    }

    /**
     * Retrieves all albums from the database and maps them to AlbumDto objects.
     * @return List of AlbumDto objects.
     */
    public List<AlbumDto> listAll() {
        List<AlbumEntity> albumEntities = albumRepository.findAll();
        return modelMapper.map(albumEntities, new TypeToken<List<AlbumDto>>() {
        }.getType());
    }

    /**
     * Maps the AlbumDto object to an AlbumEntity and saves it in the database.
     * @param albumDto AlbumDto object to be saved.
     */
    public void save(AlbumDto albumDto) {
        AlbumEntity album = modelMapper.map(albumDto, AlbumEntity.class);
        albumRepository.save(album);
    }

    /**
     * Retrieves an album with the given ID from the repository and maps it to an AlbumDto object.
     * @param id ID of the album to be retrieved.
     * @return AlbumDto object representing the album entity with the specified ID.
     * @throws EntityNotFoundException if an album with the specified ID is not found in the repository.
     */
    public AlbumDto getById(long id) {
        AlbumEntity albumEntity = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album with id " + id + " not found"));
        return modelMapper.map(albumEntity, AlbumDto.class);
    }

    /**
     * Maps the AlbumDto object to an AlbumEntity and updates it in the database.
     * @param albumDto AlbumDto object to be updated.
     */
    public void update(AlbumDto albumDto) {
        AlbumEntity album = modelMapper.map(albumDto, AlbumEntity.class);
        albumRepository.save(album);
    }

    /**
     * Deletes an album with the given ID from the database.
     * @param albumId ID of the album to be deleted.
     */
    @Transactional
    public void deleteAlbum(Long albumId) {
        AlbumEntity album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + albumId));

        // Delete the main photo of the album
        PhotoEntity mainPhoto = album.getMainPhoto();
        if (mainPhoto != null) {
            photoRepository.delete(mainPhoto);
            photoService.deletePhotoFile(mainPhoto.getPath() + mainPhoto.getFileName());
        }

        // Delete the other photos of the album
        List<PhotoEntity> photos = album.getPhotos();
        if (!photos.isEmpty()) {
            for (PhotoEntity photo : photos) {
                photoRepository.delete(photo);
                photoService.deletePhotoFile(photo.getPath() + photo.getFileName());
            }
        }
        // Delete the album itself
        albumRepository.delete(album);
    }

    /**
     * Sets the main photo of the album and saves it in the database.
     * @param albumDto  AlbumDto object to be created.
     * @param mainPhoto Main photo of the album to be set.
     * @return AlbumDto object that was created.
     */
    public AlbumDto createAlbumEntity(AlbumDto albumDto, PhotoDto mainPhoto) {
        AlbumEntity album = modelMapper.map(albumDto, AlbumEntity.class);
        PhotoEntity photoEntity = modelMapper.map(mainPhoto, PhotoEntity.class);
        photoRepository.save(photoEntity);
        album.setMainPhoto(photoEntity);
        albumRepository.save(album);
        return modelMapper.map(album, AlbumDto.class);
    }

}