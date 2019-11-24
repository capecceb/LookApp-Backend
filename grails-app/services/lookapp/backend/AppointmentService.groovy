package lookapp.backend

import grails.gorm.transactions.Transactional
import jline.internal.Log

@Transactional
class AppointmentService {

    def MailService

    Appointment save(Appointment appointment, String local, Date beginDate, List<Service> services,
             Integer clientId, Integer professionalId,Integer branchId) {
        int duration
        BigDecimal totalPrice=0
        BigDecimal totalPay=0
        Float discount

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate)
        calendar.add(Calendar.MINUTE, duration)
        Date endDate = calendar.getTime()

        appointment.dayHour = beginDate
        appointment.endDate = endDate
        appointment.local = local
        appointment.branch = Branch.get(branchId)

        appointment.services = new ArrayList<>()
        for (Integer serviceId : services) {
            Service service = Service.get(serviceId)
            if (service == null) {
                throw new BadRequestException("Invalid service id")
            }
            discount = addPromotionsAndVerifyDiscounts(appointment,beginDate,service)
            duration += service.duration
            totalPrice += service.price
            totalPay += service.price * discount
            appointment.services.add(service)
        }
        appointment.totalPrice = totalPrice
        appointment.totalToPay = totalPay

        if (clientId != null) {
            appointment.client = getClient(clientId)
        }
        List<Professional> professionals = availableProfessionals(appointment.id, beginDate, endDate,appointment.branch)
        if (professionals.size() == 0) {
            throw new BadRequestException("there are no free professionals")
        }
        if (professionalId != null) {
            appointment.professional = validateProfessional(professionalId,appointment.id,beginDate,endDate,appointment.branch)
        }
        appointment.status = AppointmentStatus.OPEN
        appointment.save()
        return appointment
    }

    List<Professional> availableProfessionals(Long appointmentId, Date beginDate, Date endDate,Branch branch) {
        def res = [:]

        Calendar calendarBegin = Calendar.getInstance()
        calendarBegin.setTime(beginDate)
        int dayOfWeek = calendarBegin.get(Calendar.DAY_OF_WEEK);
        Day day = Day.byId(dayOfWeek)

        Calendar calendarEnd = Calendar.getInstance()
        calendarEnd.setTime(endDate)
        //looking for professional that working this day
        def professionCriteria = Professional.createCriteria()
        List<Professional> professionals = professionCriteria.list {
            eq("status",ProfessionalStatus.ACTIVE)
            eq("branch",branch)
            workingHours {
                eq("days", day)
                lte("beginHour", calendarBegin.get(Calendar.HOUR_OF_DAY))
                gte("endHour", calendarEnd.get(Calendar.HOUR_OF_DAY))
            }
        }

        def appointmentCriteria = Appointment.createCriteria()
        List<Appointment> appointmentList = appointmentCriteria.list {
            lte("dayHour", endDate)
            gt("endDate", beginDate)
            eq("branch",branch)
            eq("status",AppointmentStatus.OPEN)
            if (appointmentId != null) {
                ne("id", appointmentId)
            }
        }
        List<Professional> resultProfessionals = new ArrayList<>()
        if (professionals.size() <= appointmentList.size()) {
            return resultProfessionals
        }

        for (Professional professional : professionals) {
            boolean found = false
            for (Appointment appointment : appointmentList) {
                if (appointment.professional != null && professional.id == appointment.professional.id) found = true
                break
            }
            if (!found) resultProfessionals.add(professional)
        }
        return resultProfessionals
    }

    /*
    * returns a list of appointments of a professional in a certain period of time
    */
    List<Appointment> searchAppointments(Long professionalId,Date beginDate,Date endDate){
        def appointmentCriteria = Appointment.createCriteria()
        List<Appointment> appointmentList = appointmentCriteria.list {
            if(beginDate!=null){
                gte("dayHour", beginDate)
            }
            if(endDate!=null) {
                lte("endDate", endDate)
            }
            professional{
                eq("id",professionalId)
            }
        }
        return appointmentList
    }

    private Client getClient(Integer clientId){
        Client client = Client.get(clientId)
        if (client == null) {
            throw new BadRequestException("Invalid client id")
        }
        return client
    }

    private Professional validateProfessional(Integer professionalId,Long appointmentId,Date beginDate, Date endDate,Branch branch){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate)
        int beginHour=calendar.get(Calendar.HOUR_OF_DAY)
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Day day = Day.byId(dayOfWeek)
        calendar.setTime(endDate)
        int endHour=calendar.get(Calendar.HOUR_OF_DAY)

        Professional professional = Professional.get(professionalId)
        if (professional == null) {
            throw new BadRequestException("Invalid professional id")
        }
        if (professional.status != ProfessionalStatus.ACTIVE) {
            throw new BadRequestException("Error the professional isn't active")
        }
        boolean isWorking=false
        for(WorkingHour workingHour:professional.workingHours){
            if(workingHour.days==day){
                if(workingHour.beginHour<=beginHour && workingHour.endHour>=endHour){
                    isWorking=true
                }
            }
        }
        if(!isWorking){
            throw new BadRequestException("The professional does not work at that time")
        }
        def criteria = Appointment.createCriteria()
        List<Appointment> appointmentList = criteria.list {
            lte("dayHour", endDate)
            gt("endDate", beginDate)
            eq("status", AppointmentStatus.OPEN)
            eq("branch",branch)
            eq("professional", professional)
            if (appointmentId != null) {
                ne("id", appointmentId)
            }
        }
        if (appointmentList.size() > 0) {
            throw new BadRequestException("professional is busy")
        }
        return professional
    }

    def expireAppointments() {

        Calendar cal=Calendar.getInstance()
        cal.setTime(new Date())
        cal.add(Calendar.HOUR,-1)

        List<Appointment> appointments=Appointment.findAllByStatusAndEndDateLessThan(AppointmentStatus.OPEN,cal.getTime())
        Log.info("appointments to expire ${appointments.size()}")
        for(Appointment appointment:appointments){
            Log.info("appointment ${appointment.id} EXPIRED")
            appointment.status=AppointmentStatus.EXPIRED
            appointment.save()
        }
    }
  
    private Float addPromotionsAndVerifyDiscounts(Appointment appointment, Date beginDate, Service service){
        Float discount=100
        Date now=new Date()
        def promotionCriteria = Promotion.createCriteria()
        List<Promotion> promotions = promotionCriteria.list {
            lte("startDate", beginDate)
            gte("endDate", beginDate)
            eq("status",PromotionStatus.ACTIVE)
            services{
                eq("id",service.id)
            }
        }
        appointment.promotions=new ArrayList<>()
        for (Promotion promotion : promotions) {
            if(promotion.type==PromotionType.DISCOUNT) {
                discount = discount - promotion.discount
            }
            appointment.promotions.add(promotion)
        }
        if(discount<0) discount=0
        return discount/100
    }
}
