package min.taskflow.task.entity;

public enum Priority {
    LOW,
    MEDIUM,
    HIGH;

    public static Priority from(String value) {
        //TODO: 수정 요망
        if (value == null) {
            throw new IllegalArgumentException("Priority value cannot be null");
        }
        return switch (value) {
            case "LOW" -> LOW;
            case "MEDIUM" -> MEDIUM;
            case "HIGH" -> HIGH;
            //TODO: 수정 요망
            default -> throw new IllegalArgumentException("Unknown priority: " + value);
        };
    }
}
