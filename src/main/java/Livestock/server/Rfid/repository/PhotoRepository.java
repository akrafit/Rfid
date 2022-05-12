package Livestock.server.Rfid.repository;

import Livestock.server.Rfid.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}