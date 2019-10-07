package lookapp.backend

class Appointment {

    Date dayHour
    Date endDate
    String local
    Client client
    Professional professional

    Date dateCreated
    Date lastUpdated
    AppointmentStatus status
    static hasMany = [services: Service]
    static constraints = {
        client(nullable:true)
        professional(nullable:true)
        endDate(nullable:true)
    }

    def beforeInsert() {
        status=AppointmentStatus.OPEN
        Calendar calendar=Calendar.getInstance()
        calendar.setTime(dayHour)
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        endDate=calendar.getTime()
    }
    def beforeUpdate() {
        Calendar calendar=Calendar.getInstance()
        calendar.setTime(dayHour)
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        endDate=calendar.getTime()
    }
}
