package pl.zawadkba.admintool.repository;

import pl.zawadkba.admintool.domain.BrokerStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BrokerStatus entity.
 */
public interface BrokerStatusRepository extends JpaRepository<BrokerStatus,Long> {

}
