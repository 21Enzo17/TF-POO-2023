package ar.edu.unju.fi.tp9.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateFormatter {

    /**
     * Este metodo transforma una fecha de tipo LocalDateTime a String con formato dd/MM/yyyy - HH:mm
     * @param input
     * @return
     */
    public String transformarFechaNatural(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
    
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
    
        return dateTime.format(outputFormatter);
    }

    /**
     * Este metodo transforma una fecha de tipo String con formato dd/MM/yyyy - HH:mm a LocalDateTime con formato yyyy-MM-dd'T'HH:mm
     * @param input
     * @return
     */
    public LocalDateTime fechDateTime(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        return LocalDateTime.parse(input, inputFormatter);
    }
}
