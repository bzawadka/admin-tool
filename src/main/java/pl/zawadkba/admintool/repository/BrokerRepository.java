package pl.zawadkba.admintool.repository;

import pl.zawadkba.admintool.domain.Broker;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Broker entity.
 */
public interface BrokerRepository extends JpaRepository<Broker,Long> {

}
