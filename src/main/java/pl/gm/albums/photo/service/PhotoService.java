package pl.gm.albums.photo.service;

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
import java.util.List;
import java.util.Optional;

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
     * @return List of PhotoEntity objects.
     */
    public List<PhotoEntity> listAll() {
        return photoRepository.findAll();
    }

    /**
     * Saves a photo to the repository.
     * @param photo PhotoEntity object to be saved.
     */
    public void save(PhotoEntity photo) {
        photoRepository.save(photo);
    }

    /**
     * Retrieves an optional photo with the given ID from the repository.
     * @param id ID of the photo to be retrieved.
     * @return Optional containing the PhotoEntity object with the specified ID, or empty if not found.
     */
    public Optional<PhotoEntity> getById(long id) {
        return photoRepository.findById(id);
    }

    /**
     * Updates an existing photo in the repository.
     * @param photo PhotoEntity object to be updated.
     */
    public void update(PhotoEntity photo) {
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
