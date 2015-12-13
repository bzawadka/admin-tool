package pl.zawadkba.admintool.repository;

import pl.zawadkba.admintool.domain.MessageVersion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MessageVersion entity.
 */
public interface MessageVersionRepository extends JpaRepository<MessageVersion,Long> {

}
