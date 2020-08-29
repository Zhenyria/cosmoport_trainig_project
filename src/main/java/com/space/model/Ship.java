package com.space.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonAutoDetect
@JsonPropertyOrder({"id", "name", "planet", "shipType", "prodDate", "isUsed", "speed", "crewSize", "rating"})
@Entity
@Table(name = "ship")
public class Ship implements Serializable {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonProperty("name")
    @Column(name = "name")
    private String name;

    @JsonProperty("planet")
    @Column(name = "planet")
    private String planet;

    @JsonProperty("shipType")
    @Column(name = "shipType")
    @Enumerated(EnumType.STRING)
    private ShipType shipType;

    @JsonProperty("prodDate")
    @Column(name = "prodDate")
    private Date prodDate;

    @JsonProperty("isUsed")
    @Column(name = "isUsed")
    private Boolean isUsed;

    @JsonProperty("speed")
    @Column(name = "speed")
    private Double speed;

    @JsonProperty("crewSize")
    @Column(name = "crewSize")
    private Integer crewSize;

    @JsonProperty("rating")
    @Column(name = "rating")
    private Double rating;

    public Ship() {
    }

    /**
     * Дополнительная иницализация и округление полей
     */
    public void addedInit() {
        this.speed = doubleRound(speed);
        this.rating = doubleRound(makeRating());
    }

    /**
     * Инициализация рейтинга
     * @return рейтинг
     */
    private Double makeRating() {
        final int actualYear = 3019;
        int prodYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(prodDate));
        return (80 * speed * (isUsed ? 0.5 : 1)) / (actualYear - prodYear + 1);
    }

    // Математическое округление до сотых
    private Double doubleRound(Double num) {
        return Math.round(num * 100.0) / 100.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
