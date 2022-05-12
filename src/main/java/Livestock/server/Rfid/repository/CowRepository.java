package Livestock.server.Rfid.repository;

import Livestock.server.Rfid.model.Cow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CowRepository extends JpaRepository<Cow, Long> {
}