package pl.gm.albums.album.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.gm.albums.album.dto.AlbumDto;
import pl.gm.albums.album.model.AlbumEntity;
import pl.gm.albums.album.service.AlbumService;
import pl.gm.albums.photo.dto.PhotoDto;
import pl.gm.albums.photo.model.PhotoEntity;
import pl.gm.albums.photo.service.PhotoService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/album")
public class AlbumController {

    private final AlbumService albumService;
    private final PhotoService photoService;

    public AlbumController(AlbumService albumService, PhotoService photoService) {
        this.albumService = albumService;
        this.photoService = photoService;
    }

    /**
     * Display the form to create a new album.
     */
    @GetMapping("/create")
    public String albumForm(Model model) {
        model.addAttribute("albumDto", new AlbumDto());
        return "/album/create";
    }

    /**
     * Handle the submission of the form to create a new album.
     * Creates a new album entity and a main photo entity, then saves them to the database.
     */
    @PostMapping("/create")
    @Transactional
    public String saveAlbum(@Valid AlbumDto albumDto, BindingResult bindingResult,
                            @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        if (bindingResult.hasErrors()) {
            return "/album/create";
        }

        if (imageFile.isEmpty()) {
            model.addAttribute("emptyPhotoMessage", "Main photo album is needed.");
            return "/album/create";
        }

        String photosDirectoryPath = "./src/main/resources/static/photos/";
        try {
            PhotoDto mainPhoto = photoService.createPhotoEntity(imageFile, photosDirectoryPath);
            AlbumDto newAlbum = albumService.createAlbumEntity(albumDto, mainPhoto);
            return "index";
        } catch (Exception ex) {
            return "/album/create";
        }
    }

    /**
     * Display the form to add a new photo to an album.
     */
    @GetMapping("/photo-add/{albumId}")
    public String getPhotoForm(@PathVariable Long albumId, Model model) {
        model.addAttribute("albumId", albumId);
        return "photo/create";
    }

    /**
     * Handle the submission of the form to add a new photo to an album.
     * Creates a new photo entity and adds it to the album, then saves the album to the database.
     */
    @PostMapping("/photo-add")
    public String addPhoto(@RequestParam("albumId") Long albumId,
                           @RequestParam("imageFile") MultipartFile imageFile,
                           Model model) {
        if (imageFile.isEmpty()) {
            model.addAttribute("emptyPhotoMessage", "Photo is needed.");
            return "/photo/create";
        }

        AlbumDto albumDto = albumService.getById(albumId);

        String photosDirectoryPath = "./src/main/resources/static/photos/";
        PhotoDto newPhoto = photoService.createPhotoEntity(imageFile, photosDirectoryPath);
        albumDto.getPhotos().add(newPhoto);
        albumService.save(albumDto);

        return "redirect:/album/list";
    }

    /**
     * Display all photos in an album.
     */
    @GetMapping("/photo-all/{albumId}")
    public String getAlbumPhotos(@PathVariable Long albumId, Model model) {
        List<PhotoDto> photos = albumService.getById(albumId).getPhotos();
        model.addAttribute("albumPhotos", photos);
        return "photo/list";
    }

    /**
     * Display a list of all albums.
     */
    @GetMapping("/list")
    public String getAlbums(Model model) {
        model.addAttribute("albums", albumService.listAll());
        return "/album/list";
    }

    /**
     * Delete an album by its ID.
     */
    @GetMapping("/delete/{id}")
    public String deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return "redirect:/";
    }
}
