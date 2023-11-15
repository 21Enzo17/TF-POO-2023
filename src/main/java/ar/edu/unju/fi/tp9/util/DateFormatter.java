package ar.edu.unju.fi.tp9.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class DateFormatter {

    public String transformarFechaNatural(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
    
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
    
        return dateTime.format(outputFormatter);
    }

    public LocalDateTime fechDateTime(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        return LocalDateTime.parse(input, inputFormatter);
    }
}
