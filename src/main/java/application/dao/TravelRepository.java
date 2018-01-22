package application.dao;

import application.domain.Travel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRepository extends CrudRepository<Travel, Long> {
}
