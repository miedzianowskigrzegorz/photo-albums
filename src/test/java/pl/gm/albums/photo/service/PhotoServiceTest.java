package pl.gm.albums.photo.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import pl.gm.albums.photo.dto.PhotoDto;
import pl.gm.albums.photo.model.PhotoEntity;
import pl.gm.albums.photo.repository.PhotoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    private PhotoService photoService;

    private ModelMapper modelMapper = new ModelMapper();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        photoService = new PhotoService(photoRepository, modelMapper);
    }

    @Test
    void testListAll() {
        List<PhotoEntity> photoEntities = new ArrayList<>();
        PhotoEntity photo1 = new PhotoEntity(1L, "/path/to/photo1", "photo1.jpg", null);
        PhotoEntity photo2 = new PhotoEntity(2L, "/path/to/photo2", "photo2.jpg", null);
        photoEntities.add(photo1);
        photoEntities.add(photo2);

        when(photoRepository.findAll()).thenReturn(photoEntities);

        List<PhotoDto> result = photoService.listAll();

        assertEquals(photoEntities.size(), result.size());
        assertEquals(photoEntities.get(0).getId(), result.get(0).getId());
        assertEquals(photoEntities.get(1).getId(), result.get(1).getId());

        verify(photoRepository, times(1)).findAll();
        verifyNoMoreInteractions(photoRepository);
    }

    @Test
    void testSave() {
        PhotoDto photoDto = new PhotoDto(null, "/path/to/photo1", "photo1.jpg", null);
        PhotoEntity photoEntity = new PhotoEntity(null, "/path/to/photo1", "photo1.jpg", null);

        when(photoRepository.save(any(PhotoEntity.class))).thenReturn(photoEntity);

        photoService.save(photoDto);

        verify(photoRepository, times(1)).save(any(PhotoEntity.class));
        verifyNoMoreInteractions(photoRepository);
    }

    @Test
    void testGetById() {
        PhotoEntity photoEntity = new PhotoEntity(1L, "/path/to/photo1", "photo1.jpg", null);
        PhotoDto expected = new PhotoDto(1L, "/path/to/photo1", "photo1.jpg", null);

        when(photoRepository.findById(photoEntity.getId())).thenReturn(Optional.of(photoEntity));

        PhotoDto result = photoService.getById(photoEntity.getId());

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getPath(), result.getPath());
        assertEquals(expected.getFileName(), result.getFileName());

        verify(photoRepository, times(1)).findById(photoEntity.getId());
        verifyNoMoreInteractions(photoRepository);
    }

    @Test
    void testGetByIdNotFoundException() {
        long nonExistingId = 100L;

        when(photoRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> photoService.getById(nonExistingId));

        verify(photoRepository, times(1)).findById(nonExistingId);
        verifyNoMoreInteractions(photoRepository);
    }

    @Test
    void updateTest() {
        PhotoDto photoDto = new PhotoDto();
        // given
        PhotoDto updatedPhoto = new PhotoDto();
        photoDto.setId(1L);
        photoDto.setPath("Test path");

        // when
        photoService.update(updatedPhoto);

        // then
        verify(photoRepository).save(any(PhotoEntity.class));
    }

    @Test
    void deleteTest() {
        photoService.delete(1L);
        Mockito.verify(photoRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void createPhotoEntityTest() throws IOException {
        // given
        MockMultipartFile imageFile = new MockMultipartFile("test-image.jpg", "test-image.jpg", "image/jpeg", "test data".getBytes());

        String mockFileName = imageFile.getOriginalFilename();
        String path = "./src/main/resources/static/photos/";
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setId(1L);
        when(photoRepository.save(any(PhotoEntity.class))).thenReturn(photoEntity);

// when
        PhotoDto result = photoService.createPhotoEntity(imageFile, path);

// then
        assertEquals("test-image.jpg", result.getFileName());
        assertEquals(path + "test-image.jpg", result.getPath() + result.getFileName());
        verify(photoRepository, times(1)).save(any(PhotoEntity.class));
    }

    @Test
    void shouldSaveFileSuccessfully() throws Exception {
        // given
        Path path = Paths.get("./src/main/resources/static/photos/");
        MockMultipartFile file = new MockMultipartFile("testfile.jpg", "testfile.jpg", "image/jpeg", "test data".getBytes());

        // when
        PhotoService.savePhotoFile(file, path.toString());

        // then
        assertTrue(Files.exists(path.resolve(file.getOriginalFilename())));
    }
}