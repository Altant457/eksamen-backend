package dtos;

import entities.Assignment;
import entities.DinnerEvent;
import entities.Member;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link DinnerEvent} entity
 */
public class DinnerEventDTO implements Serializable {
    private final Long id;
    private final Timestamp time;
    private final String location;
    private final String dish;
    private final BigDecimal pricePerPerson;
    private final int assignments;

    public DinnerEventDTO(DinnerEvent dinnerEvent) {
        this.id = dinnerEvent.getId();
        this.time = dinnerEvent.getTime();
        this.location = dinnerEvent.getLocation();
        this.dish = dinnerEvent.getDish();
        this.pricePerPerson = dinnerEvent.getPricePerPerson();
        int temp = 0;
        for (Assignment assignment : dinnerEvent.getAssignments()) {
            temp += assignment.getMembers().size();
        }
        this.assignments = temp;
    }

    public static List<DinnerEventDTO> getDTOs(List<DinnerEvent> dinnerEvents) {
        List<DinnerEventDTO> all = new ArrayList<>();
        dinnerEvents.forEach(dinnerEvent -> all.add(new DinnerEventDTO(dinnerEvent)));
        return all;
    }

    public Long getId() {
        return id;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getDish() {
        return dish;
    }

    public BigDecimal getPricePerPerson() {
        return pricePerPerson;
    }

    public int getAssignments() {
        return assignments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DinnerEventDTO entity = (DinnerEventDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.time, entity.time) &&
                Objects.equals(this.location, entity.location) &&
                Objects.equals(this.dish, entity.dish) &&
                Objects.equals(this.pricePerPerson, entity.pricePerPerson) &&
                Objects.equals(this.assignments, entity.assignments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, location, dish, pricePerPerson, assignments);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "time = " + time + ", " +
                "location = " + location + ", " +
                "dish = " + dish + ", " +
                "pricePerPerson = " + pricePerPerson + ", " +
                "assignments = " + assignments + ")";
    }
}