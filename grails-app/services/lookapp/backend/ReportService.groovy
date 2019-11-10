package lookapp.backend

import grails.gorm.transactions.Transactional

@Transactional
class ReportService {
    def generate(Date fromDate, Date toDate, Integer branchId) {

        //List<ProfessionalReport> reports
         List<ProfessionalReport> reports = new ArrayList<>();
        //HashSet<ProfessionalReport> reports = new HashSet<ProfessionalReport>();

        Branch branch = null;

        ArrayList<Appointment> appointments = Appointment.list()
        def payments = new ArrayList<>()
        if(branchId != null) {
            branch =  Branch.get(branchId)
        }

        if(toDate != null) {
            for (int i = 0; i < appointments.size(); i++) {
               if(appointments.get(i).getDayHour() < toDate) {
                   appointments.remove(i)
                   i--
               }
            }
        }
        if(fromDate != null) {
            for (int i = 0; i < appointments.size(); i++) {
                if(appointments.get(i).getDayHour() > fromDate) {
                    appointments.remove(i)
                    i--
                }
            }
        }
        if(branch != null) {
            for (int i = 0; i < appointments.size(); i++) {
                if( appointments.get(i).getBranch().getName() != branch.getName()) {
                    appointments.remove(i)
                    i--
                }
            }
        }

        for(Appointment appointment in appointments){

            BigDecimal totalCost = 0;

            ProfessionalReport newReport = new ProfessionalReport()
            String status = appointment.getStatus()
            if(status == "PAID"){

                payments = appointment.payments
                for (Payment payment in payments) {
                    totalCost += payment.amount
                }
                Professional profecional = appointment.getProfessional()
                newReport.setProfessional(profecional)
                newReport.setTotalAmount(totalCost)

                reports.add(newReport)

            }
//me falta ver como los agrego sin repetir a la lisata que voy a enviar.
        }
        return reports
    }
}
