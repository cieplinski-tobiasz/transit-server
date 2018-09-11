package application.services;

import application.repositories.RouteRepository;
import application.domain.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RouteService {
    private RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<Route> getAllRoutes() {
        List<Route> routeList = new LinkedList<>();
        routeRepository.findAll().forEach(routeList::add);
        return routeList;
    }

    public Optional<Route> getRouteById(Long id) {
        Assert.notNull(id, "ID cannot be null.");
        return routeRepository.findById(id);
    }

    public boolean addRoute(Route route) {
        Assert.notNull(route, "Route cannot be null.");
        if (!routeRepository.existsById(route.getId())) {
            routeRepository.save(route);
            return true;
        } else {
            return false;
        }
    }
}
