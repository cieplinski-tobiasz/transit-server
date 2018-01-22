package application.dao;

import application.domain.Leg;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegRepository extends CrudRepository<Leg, Long> {
}
