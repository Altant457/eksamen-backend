package dtos;

import entities.DinnerEvent;

import java.util.ArrayList;
import java.util.List;

public class DinnerEventDTOs {
    private final List<DinnerEventDTO> all = new ArrayList<>();

    public DinnerEventDTOs(List<DinnerEvent> dinnerEvents) {
        dinnerEvents.forEach(dinnerEvent -> all.add(new DinnerEventDTO(dinnerEvent)));
    }
}