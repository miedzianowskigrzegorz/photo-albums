package pl.gm.albums.photo.service;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.gm.albums.photo.dto.PhotoDto;
import pl.gm.albums.photo.model.PhotoEntity;
import pl.gm.albums.photo.repository.PhotoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final ModelMapper modelMapper;

    public PhotoService(PhotoRepository photoRepository, ModelMapper modelMapper) {
        this.photoRepository = photoRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Retrieves a list of all photos from the repository.
     * @return List of PhotoDto objects.
     */
    public List<PhotoDto> listAll() {
        List<PhotoDto> photos = modelMapper.map(photoRepository.findAll(), ArrayList.class);
        return photos;
    }

    /**
     * Saves a photo to the repository.
     * @param photoDto PhotoEntity object to be saved.
     */
    public void save(PhotoDto photoDto) {
        PhotoEntity photo = modelMapper.map(photoDto,PhotoEntity.class);
        photoRepository.save(photo);
    }

    /**
     * Retrieves an photo with the given ID from the repository and maps it to an PhotoDto object.
     * @param id ID of the photo to be retrieved.
     * @return PhotoDto object representing the photo entity with the specified ID.
     * @throws EntityNotFoundException if an photo with the specified ID is not found in the repository.
     */
    public PhotoDto getById(long id) {
        PhotoEntity photoEntity = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo with id " + id + " not found"));
        return modelMapper.map(photoEntity, PhotoDto.class);
    }

    /**
     * Maps the PhotoDto object to an PhotoEntity and updates it in the database.
     * @param photoDto PhotoDto object to be updated.
     */
    public void update(PhotoDto photoDto) {
        PhotoEntity photo = modelMapper.map(photoDto,PhotoEntity.class);
        photoRepository.save(photo);
    }

    /**
     * Deletes a photo from the repository with the given ID.
     * @param id ID of the photo to be deleted.
     */
    public void delete(long id) {
        photoRepository.deleteById(id);
    }

    /**
     * Creates a new photo entity based on the uploaded file and path.
     * @param imageFile MultipartFile containing the uploaded image file.
     * @param path Path where the file should be saved on the server.
     * @return PhotoDto object representing the new photo entity.
     */
    public PhotoDto createPhotoEntity(MultipartFile imageFile, String path) {
        PhotoDto photo = new PhotoDto();
        photo.setFileName(imageFile.getOriginalFilename());
        photo.setPath(path);
        saveFile(imageFile, path);
        PhotoEntity photoEntity = modelMapper.map(photo,PhotoEntity.class);
        photoRepository.save(photoEntity);
        return modelMapper.map(photoEntity,PhotoDto.class);
    }

    /**
     * Saves the uploaded file on the server.
     * @param file MultipartFile containing the uploaded file.
     * @param path Path where the file should be saved on the server.
     * @throws IOException if an I/O error occurs.
     */
    private void saveFile(MultipartFile file, String path) {
        try {
            Path filePath = Paths.get(path, file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Saving file error " + file.getOriginalFilename(), e);
        }
    }
}
