package aor.proj3t.dto;

public class TrainingYearStatDTO {
    public int year;
    public long minutes;
    
    public TrainingYearStatDTO(int year, long minutes) {
        this.year = year;
        this.minutes = minutes;
    }

    public int getYear() {
        return year;
    }
    public long getMinutes() {
        return minutes;
    }

} 
