package pl.gm.albums.album.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import pl.gm.albums.album.dto.AlbumDto;
import pl.gm.albums.album.model.AlbumEntity;
import pl.gm.albums.album.repository.AlbumRepository;
import pl.gm.albums.photo.repository.PhotoRepository;
import pl.gm.albums.photo.service.PhotoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private PhotoService photoService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AlbumService albumService;

    @Test
    void testListAll() {
        // given
        List<AlbumEntity> albumEntities = new ArrayList<>();
        albumEntities.add(new AlbumEntity(1L, "Album 1", null, null));
        albumEntities.add(new AlbumEntity(2L, "Album 2", null, null));
        when(albumRepository.findAll()).thenReturn(albumEntities);

        List<AlbumDto> expectedAlbumDtos = new ArrayList<>();
        expectedAlbumDtos.add(new AlbumDto(1L, "Album 1", null, new ArrayList<>()));
        expectedAlbumDtos.add(new AlbumDto(2L, "Album 2", null, new ArrayList<>()));
        when(modelMapper.map(albumEntities, new TypeToken<List<AlbumDto>>() {
        }.getType())).thenReturn(expectedAlbumDtos);

        // when
        List<AlbumDto> actualAlbumDtos = albumService.listAll();

        // then
        assertEquals(expectedAlbumDtos, actualAlbumDtos);
        verify(albumRepository).findAll();
        verify(modelMapper).map(albumEntities, new TypeToken<List<AlbumDto>>() {
        }.getType());
    }

    @Test
    void testSave() {
        // given
        AlbumDto albumDto = new AlbumDto(null, "New Album", null, new ArrayList<>());
        AlbumEntity expectedAlbumEntity = new AlbumEntity(null, "New Album", null, new ArrayList<>());
        when(modelMapper.map(albumDto, AlbumEntity.class)).thenReturn(expectedAlbumEntity);

        // when
        albumService.save(albumDto);

        // then
        verify(albumRepository).save(expectedAlbumEntity);
    }

    @Test
    void testGetById() {
        // given
        Long albumId = 1L;
        AlbumEntity expectedAlbumEntity = new AlbumEntity(albumId, "Album 1", null, null);
        when(albumRepository.findById(albumId)).thenReturn(Optional.of(expectedAlbumEntity));

        AlbumDto expectedAlbumDto = new AlbumDto(albumId, "Album 1", null, new ArrayList<>());
        when(modelMapper.map(expectedAlbumEntity, AlbumDto.class)).thenReturn(expectedAlbumDto);

        // when
        AlbumDto actualAlbumDto = albumService.getById(albumId);

        // then
        assertEquals(expectedAlbumDto, actualAlbumDto);
        verify(albumRepository).findById(albumId);
        verify(modelMapper).map(expectedAlbumEntity, AlbumDto.class);
    }

    @Test
    void testGetByIdNotFound() {
        // given
        Long albumId = 1L;
        when(albumRepository.findById(albumId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class, () -> albumService.getById(albumId));
        verify(albumRepository).findById(albumId);
    }

    @Test
    void testUpdate() {
        // given
        AlbumDto albumDto = new AlbumDto(1L, "Album 1 Updated", null, new ArrayList<>());
        AlbumEntity expectedAlbumEntity = new AlbumEntity(1L, "Album 1 Updated", null, new ArrayList<>());
        when(modelMapper.map(albumDto, AlbumEntity.class)).thenReturn(expectedAlbumEntity);

        // when
        albumService.update(albumDto);

        // then
        verify(albumRepository).save(expectedAlbumEntity);
    }
}