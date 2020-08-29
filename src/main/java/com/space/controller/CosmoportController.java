package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.CosmoportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest/ships")
public class CosmoportController {
    private final CosmoportService service;

    @Autowired
    public CosmoportController(CosmoportService service) {
        this.service = service;
    }

    /**
     * Возвращает список кораблей, подходящих к указанным параметрам
     */
    @GetMapping(value = "")
    public ResponseEntity<List<Ship>> getSome(@RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "planet", required = false) String planet,
                                              @RequestParam(name = "shipType", required = false) ShipType shipType,
                                              @RequestParam(name = "after", required = false) Long after,
                                              @RequestParam(name = "before", required = false) Long before,
                                              @RequestParam(name = "isUsed", required = false) Boolean isUsed,
                                              @RequestParam(name = "minSpeed", required = false) Double minSpeed,
                                              @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
                                              @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
                                              @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
                                              @RequestParam(name = "minRating", required = false) Double minRating,
                                              @RequestParam(name = "maxRating", required = false) Double maxRating,
                                              @RequestParam(name = "order", defaultValue = "ID") ShipOrder order,
                                              @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                              @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        return new ResponseEntity<>(service.getSome(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating, order, pageNumber, pageSize), HttpStatus.OK);
    }

    /**
     * Возвращает количество кораблей, подходящих к указанным параметрам
     */
    @GetMapping(value = "/count")
    public ResponseEntity<Integer> count(@RequestParam(name = "name", required = false) String name,
                                         @RequestParam(name = "planet", required = false) String planet,
                                         @RequestParam(name = "shipType", required = false) ShipType shipType,
                                         @RequestParam(name = "after", required = false) Long after,
                                         @RequestParam(name = "before", required = false) Long before,
                                         @RequestParam(name = "isUsed", required = false) Boolean isUsed,
                                         @RequestParam(name = "minSpeed", required = false) Double minSpeed,
                                         @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
                                         @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
                                         @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
                                         @RequestParam(name = "minRating", required = false) Double minRating,
                                         @RequestParam(name = "maxRating", required = false) Double maxRating) {
        return new ResponseEntity<>(service.count(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating), HttpStatus.OK);
    }

    /**
     * Пытается найти корабль по указанному id.
     * Возвращает 400 ошибку, если id невалидный
     * Возвращает 404 ошибку, если корабль в базе не найден
     * Возвращает код 200 и корабль, если всё ок
     * @param id id корабля
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Ship> get(@PathVariable(name = "id") String id) {
        long numId = parseId(id);
        if (numId == -1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        final Ship ship = service.get(numId);
        return ship == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(ship, HttpStatus.OK);
    }

    /**
     * Пытается занести переданный корабль в базу.
     * Если это невозможно, возвращает 400 ошибку
     * Возвращает созданный корабль и код 200, если всё ок
     * @param ship переданный корабль
     */
    @PostMapping(value = "/")
    public ResponseEntity<Ship> create(@RequestBody Ship ship) {
        return service.create(ship) ? new ResponseEntity<>(service.get(ship.getId()), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Пытается обновить корабль по указанном id
     * Возвращает 400 ошибку, если id невалидный
     * Возвращает 404 ошибку, если корабль в базе не найден
     * Возвращает код 200 и обновлённый корабль, если всё ок
     * @param id id обновляемого корабля
     * @param ship обновляемый корабль
     */
    @PostMapping(value = "/{id}")
    public ResponseEntity<Ship> update(@PathVariable(name = "id") String id, @RequestBody Ship ship) {
        final long beforeDate = 26192246400000L;
        final long afterDate = 33103209600000L;
        long numId = parseId(id);
        String name = ship.getName();
        String planet = ship.getPlanet();
        Date prodDate = ship.getProdDate();
        Double speed = ship.getSpeed();
        Integer crewSize = ship.getCrewSize();
        if (numId == -1 ||
                name != null && (name.length() > 50 || name.equals("")) ||
                planet != null && planet.length() > 50 ||
                prodDate != null && (prodDate.getTime() < beforeDate || prodDate.getTime() > afterDate) ||
                speed != null && (speed < 0.01 || speed > 0.99) ||
                crewSize != null && (crewSize < 1 || crewSize > 9999)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        final Ship updatedShip = service.update(ship, numId);
        return updatedShip == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(updatedShip, HttpStatus.OK);
    }

    /**
     * Пытается удалить корабль по указанному id
     * Возвращает 400 ошибку, если id невалидный
     * Возвращает 404 ошибку, если такого корабля нет в базе
     * Возвращает код 200 и удаляет корабль из базы, если всё ок
     * @param id id корабля
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") String id) {
        long numId = parseId(id);
        if (numId == -1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return service.delete(numId) ? new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Парсит переданную строку на предмет id
     * Возвращает -1, если id невалидный
     * @param str переданная строка
     * @return распарсенный id
     */
    private long parseId(String str) {
        double numId;
        try {
            numId = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return -1;
        }
        if (numId <= 0 || (long)numId != numId) return -1;
        return (long)numId;
    }
}
