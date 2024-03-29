package lookapp.backend

import grails.gorm.transactions.Transactional

@Transactional
class ProfessionalReportService {
    def generate(Date fromDate, Date toDate, Integer branchId) {

        List<ProfessionalReport> reports = new ArrayList<>();
        Branch branch = null;
        ArrayList<Appointment> appointments = Appointment.list()
        def payments = new ArrayList<>()
        if(branchId != null) {
            branch =  Branch.get(branchId)
        }

        if(toDate != null) {
            for (int i = 0; i < appointments.size(); i++) {
                if(appointments.get(i).dayHour > toDate) {
                    appointments.remove(i)
                    i--
                }
            }
        }
        if(fromDate != null) {
            for (int i = 0; i < appointments.size(); i++) {
                if(appointments.get(i).dayHour < fromDate) {
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
                boolean exist = false
                if(reports.size() == 0) {
                    reports.add(newReport)
                }else{
                    for(ProfessionalReport report in reports) {
                        if(report.professional == newReport.professional) {
                            exist = true
                            report.totalAmount += newReport.totalAmount
                        }
                    }
                    if(!exist){
                        reports.add(newReport)
                    }
                }
            }
        }
        return reports
    }
}