package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "dinnerEvent")
public class DinnerEvent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "dish", nullable = false)
    private String dish;

    @Column(name = "price_pr_person", nullable = false)
    private BigDecimal pricePerPerson;

    @OneToMany(mappedBy = "dinnerEvent")
    private Set<Assignment> assignments = new LinkedHashSet<>();

    public DinnerEvent() {}

    public DinnerEvent(Timestamp time, String location, String dish, BigDecimal pricePerPerson) {
        this.time = time;
        this.location = location;
        this.dish = dish;
        this.pricePerPerson = pricePerPerson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public BigDecimal getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(BigDecimal pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public Set<Assignment> getAssignments() {
        return assignments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DinnerEvent that = (DinnerEvent) o;
        return id.equals(that.id) && time.equals(that.time) && location.equals(that.location)
                && dish.equals(that.dish) && pricePerPerson.equals(that.pricePerPerson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, location, dish, pricePerPerson);
    }
}