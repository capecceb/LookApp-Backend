package lookapp.backend

import grails.gorm.transactions.Transactional

@Transactional
class ServiceReportService {
    def generate(Date fromDate, Date toDate, Integer branchId) {

        List<ServiceReport> reports = new ArrayList<>();
        Branch branch = null;
        ArrayList<Appointment> appointments = Appointment.list()
        def services = new ArrayList<>()

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
            String status = appointment.getStatus()

            if(status == "PAID"){
                services = appointment.services
                for (Service service in services) {

                    ServiceReport newReport = new ServiceReport()
                    newReport.setService(service)
                    newReport.setTotalAmount(service.price)
                    newReport.quantity = 1

                    boolean exist = false
                    if (reports.size() == 0) {
                        reports.add(newReport)
                    } else {
                        for(ServiceReport report in reports){
                            if(report.service == newReport.service){
                                exist = true
                                report.totalAmount += newReport.totalAmount
                                report.quantity ++
                            }
                        }
                        if(!exist){
                            reports.add(newReport)
                        }
                    }
                }
            }
        }
        return reports
    }
}
