package com.vkatit.cinema.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private LocalDateTime date;
    @Min(value = 1, message = "Value must be greater than 0")
    @Max(value = 50, message = "Value must be less than 51")
    private int seat;
    private String movieName;
    private Viewer viewer;
}
