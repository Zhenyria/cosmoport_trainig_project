package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import java.util.List;

public interface CosmoportService {

    /**
     * Возвращает количество кораблей с указанными параметрами
     */
    int count(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
              Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
              Double maxRating);

    /**
     * Возвращает список кораблей, подходящий по параметрам
     */
    List<Ship> getSome(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                       Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
                       Double maxRating, ShipOrder order, Integer pageNumber, Integer pageSize);

    /**
     * Создаёт новый корабль и помещает его в базу данных
     * @param ship новый корабль
     * @return true, если удалось создать корабль; false, если нет
     */
    boolean create(Ship ship);

    /**
     * Возвращает корабль по указанному id
     * @param id id корабля
     * @return обнаруженный корабль
     */
    Ship get(long id);

    /**
     * Обновляет запись о корабле
     * @param ship обновлённый корабль
     * @param id id корабля
     * @return true, если удалось обновить корабль; false, если корабля нет
     */
    Ship update(Ship ship, long id);

    /**
     * Удаляет корабль по указанному id
     * @param id id удаляемого корабля
     * @return true, если удалось удалить корабль; false, если корабля нет
     */
    boolean delete(long id);

}
