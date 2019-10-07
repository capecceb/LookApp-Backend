package lookapp.backend

enum Day {
    SUNDAY(1),MONDAY(2),TUESDAY(3),WEDNESDAY(4),THURSDAY(5),FRIDAY(6),SATURDAY(7)
    private final int value
    Day(int value){
        this.value=value
    }
    static Day byId(int id) {
        values().find { it.value == id }
    }
}

