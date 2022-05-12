package Livestock.server.Rfid.security;

import Livestock.server.Rfid.model.Cow;
import Livestock.server.Rfid.model.Photo;
import Livestock.server.Rfid.repository.CowRepository;
import Livestock.server.Rfid.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class CowService {
    @Value("${upload}")
    private String uploadPath;
    private final CowRepository cowRepository;
    private final PhotoRepository photoRepository;

    public CowService(CowRepository cowRepository, PhotoRepository photoRepository) {
        this.cowRepository = cowRepository;
        this.photoRepository = photoRepository;
    }

    public Iterable<Cow> findAll() {

        return cowRepository.findAll();
    }

    public Cow getOne(Long id) {

        return cowRepository.getOne(id);
    }

    public ResponseEntity<Object> uploadImage(Long id, MultipartFile image) throws IOException {
        String tripleFolder = uploadPath + generateYearMonthFolder();
        Path path = Paths.get(tripleFolder);
        Files.createDirectories(path);
        File myFile = new File("/.." + tripleFolder + image.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(myFile);
        fos.write(image.getBytes());
        fos.close();
        String src = tripleFolder + image.getOriginalFilename();
        Cow cow = cowRepository.getOne(id);
        Photo photo = new Photo();
        photo.setCow(cow);
        photo.setSrc(src);
        photoRepository.save(photo);


        return new ResponseEntity<>(src, HttpStatus.OK);
    }
/*
    public ResponseEntity<Object> uploadImage(Long id, MultipartFile image) throws IOException {

        String tripleFolder = "/upload/" + generateYearMonthFolder();
        Path path = Paths.get(tripleFolder);
        Files.createDirectories(path);
        File myFile = new File("/.." + tripleFolder + image.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(myFile);
        fos.write(image.getBytes());
        fos.close();
        String src = tripleFolder + image.getOriginalFilename();
        Cow cow = cowRepository.getOne(id);
        Photo photo = new Photo();
        photo.setCow(cow);
        photo.setSrc(src);
        photoRepository.save(photo);


        return new ResponseEntity<>(src, HttpStatus.OK);
    }

 */

    private String generateTripleFolder() {
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String[] folder = new String[6];
        for (int i = 0; i < 6; i++) {
            Random random = new Random();
            int j = random.nextInt(letters.length());
            char ch = letters.charAt(j);
            folder[i] = String.valueOf(ch);
        }
        return folder[0] + folder[1] + "/" + folder[2] + folder[3] + "/" + folder[4] + folder[5] + "/";
    }

    private String generateYearMonthFolder() {
        LocalDateTime now = LocalDateTime.now();
        return now.getYear() + "/" + now.getMonth() + "/" + now.getDayOfMonth() + "/";
    }

    public void addCow(Cow cow) {
        cowRepository.save(cow);
    }
}
