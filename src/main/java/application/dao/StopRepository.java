package application.dao;

import application.domain.Stop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends CrudRepository<Stop, Long> {
}
