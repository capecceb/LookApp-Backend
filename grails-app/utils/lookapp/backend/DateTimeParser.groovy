package lookapp.backend

import java.text.SimpleDateFormat
import java.time.Instant

class DateTimeParser {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    public static Date parseSearchFormat(String input){
        return sdf.parse(input)
    }

    public static Date parse(String input){
        return Date.from(Instant.parse(input))
    }
}
