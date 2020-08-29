package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.CosmoportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CosmoportServiceImpl implements CosmoportService {
    private final CosmoportRepository cosmoportRepository;

    @Autowired
    public CosmoportServiceImpl(CosmoportRepository cosmoportRepository) {
        this.cosmoportRepository = cosmoportRepository;
    }

    @Override
    public int count(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                     Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
                     Double maxRating) {
        return (int) shipFilter(
                name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating)
                .count();
    }

    @Override
    public List<Ship> getSome(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                              Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                              Double minRating, Double maxRating, ShipOrder order, Integer pageNumber, Integer pageSize) {
        List<Ship> ships =
                shipFilter(name, planet, shipType, after, before, isUsed, minSpeed,
                        maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating)
                .sorted((ship1, ship2) -> {
                    switch (order) {
                        case ID: return Long.compare(ship1.getId(), ship2.getId());
                        case SPEED: return Double.compare(ship1.getSpeed(), ship2.getSpeed());
                        case DATE: return Long.compare(ship1.getProdDate().getTime(), ship2.getProdDate().getTime());
                        case RATING: return Double.compare(ship1.getRating(), ship2.getRating());
                    }
                    return 0;
                })
                .collect(Collectors.toList());
        pageSize = pageSize == null ? 3 : pageSize;
        int startIndex = (pageNumber == null ? 0 : pageNumber) * pageSize;
        return ships.subList(startIndex, Math.min(startIndex + pageSize, ships.size()));
    }


    @Override
    public boolean create(Ship ship) {
        final long beforeDate = 26192246400000L;
        final long afterDate = 33103209600000L;
        String name = ship.getName();
        String planet = ship.getPlanet();
        ShipType shipType = ship.getShipType();
        Date prodDate = ship.getProdDate();
        Boolean isUsed = ship.getUsed();
        Double speed = ship.getSpeed();
        Integer crewSize = ship.getCrewSize();
        if ((name == null || name.equals("") || name.length() > 50) ||
                (planet == null || planet.equals("") || planet.length() > 50) ||
                shipType == null ||
                (prodDate == null || !(prodDate.getTime() >= beforeDate && prodDate.getTime() <= afterDate)) ||
                speed == null || !(speed >= 0.01 && speed <= 0.99) ||
                crewSize == null || !(crewSize >= 1 && crewSize <= 9999)) return false;
        if (isUsed == null) ship.setUsed(false);
        ship.addedInit();
        cosmoportRepository.save(ship);
        return true;
    }

    @Override
    public Ship get(long id) {
        try {
            return cosmoportRepository.findById(id).get();
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public Ship update(Ship ship, long id) {
        if (!cosmoportRepository.existsById(id)) return null;
        final Ship oldShip = get(id);
        String name = ship.getName();
        String planet = ship.getPlanet();
        ShipType shipType = ship.getShipType();
        Date prodDate = ship.getProdDate();
        Boolean isUsed = ship.getUsed();
        Double speed = ship.getSpeed();
        Integer crewSize = ship.getCrewSize();

        if (name != null) oldShip.setName(name);
        if (planet != null) oldShip.setPlanet(planet);
        if (shipType != null) oldShip.setShipType(shipType);
        if (prodDate != null) oldShip.setProdDate(prodDate);
        if (isUsed != null) oldShip.setUsed(isUsed);
        if (speed != null) oldShip.setSpeed(speed);
        if (crewSize != null) oldShip.setCrewSize(crewSize);

        oldShip.addedInit();
        cosmoportRepository.save(oldShip);
        return oldShip;
    }

    @Override
    public boolean delete(long id) {
        if (!cosmoportRepository.existsById(id)) return false;
        cosmoportRepository.deleteById(id);
        return true;
    }

    /**
     * Сервисный метод
     * Возвращает отфильтрованный список всех кораблей в базе
     */
    private Stream<Ship> shipFilter(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {
        return cosmoportRepository.findAll()
                .stream()
                .filter(x -> name == null || x.getName().matches(".*" + name + ".*"))
                .filter(x -> planet == null || x.getPlanet().matches(".*" + planet + ".*"))
                .filter(x -> shipType == null || x.getShipType() == shipType)
                .filter(x -> after == null || x.getProdDate().getTime() >= after)
                .filter(x -> before == null || x.getProdDate().getTime() <= before)
                .filter(x -> isUsed == null || x.getUsed() == isUsed)
                .filter(x -> minSpeed == null || x.getSpeed() >= minSpeed)
                .filter(x -> maxSpeed == null || x.getSpeed() <= maxSpeed)
                .filter(x -> minCrewSize == null || x.getCrewSize() >= minCrewSize)
                .filter(x -> maxCrewSize == null || x.getCrewSize() <= maxCrewSize)
                .filter(x -> minRating == null || x.getRating() >= minRating)
                .filter(x -> maxRating == null || x.getRating() <= maxRating);
    }
}
