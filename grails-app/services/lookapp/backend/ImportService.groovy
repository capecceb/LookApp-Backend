package lookapp.backend

import jline.internal.Log

class ImportService {

    def deleteTables(){
        Payment.withNewTransaction {
            Payment.deleteAll(Payment.findAll())
        }
        Accountancy.withNewTransaction {
            def accounting=Accountancy.findAll()
            for(Accountancy accountancy:accounting){
                accountancy.accountMovements=null
                accountancy.save()
            }
        }
        AccountMovement.withNewTransaction {
            AccountMovement.deleteAll(AccountMovement.findAll())
        }
        Appointment.withNewTransaction {
            Appointment.deleteAll(Appointment.findAll())
        }
        Client.withNewTransaction {
            Client.deleteAll(Client.findAll())
        }
        Accountancy.withNewTransaction {
            Accountancy.deleteAll(Accountancy.findAll())
        }
        Config.withNewTransaction {
            Config.deleteAll(Config.findAll())
        }
        Payment.withNewTransaction {
            Payment.deleteAll(Payment.findAll())
        }
        Professional.withNewTransaction {
            Professional.deleteAll(Professional.findAll())
        }
        ProfessionalReport.withNewTransaction {
            ProfessionalReport.deleteAll(ProfessionalReport.findAll())
        }
        Promotion.withNewTransaction {
            Promotion.deleteAll(Promotion.findAll())
        }
        Service.withNewTransaction {
            Service.deleteAll(Service.findAll())
        }
        ServiceReport.withNewTransaction {
            ServiceReport.deleteAll(ServiceReport.findAll())
        }
        WorkingHour.withNewTransaction {
            WorkingHour.deleteAll(WorkingHour.findAll())
        }
        User.withNewTransaction {
            User.deleteAll(User.findAll())
        }
        Rol.withNewTransaction {
            Rol.deleteAll(Rol.findAll())
        }
        Branch.withNewTransaction {
            Branch.deleteAll(Branch.findAll())
        }

    }

    def importData(def params){
        if(params.configs){
            for(def object:params.configs){
                Config config=object
                config.save()
                object.newId=config.id
            }
        }
        if(params.branches){
            for(def object:params.branches){
                Branch branch=object
                branch.save()
                object.newId=branch.id
            }
        }
        if(params.roles){
            for(def object:params.roles){
                Rol rol=object
                rol.save()
                object.newId=rol.id
            }
        }
        if(params.services){
            for(def object:params.services){
                Service service=object
                service.save()
                object.newId=service.id
            }
        }
        if(params.workingHours){
            for(def object:params.workingHours){
                WorkingHour workingHour=object
                workingHour.save()
                object.newId=workingHour.id
            }
        }
        if(params.users){
            for(def object:params.users){
                User user=object
                if(object.branch!=null){
                    def branch=params.branches.find { element -> element.id == object.branch.id}
                    user.branch=Branch.get(branch.newId)
                }
                user.roles.clear()
                if(object.roles!=null){
                    def roles=params.roles.findAll {
                        element -> element.id in object.roles.id
                    }
                    for(def rol:roles){
                        user.roles.add(Rol.get(rol.newId))
                    }
                }
                user.save()
            }
        }
        if(params.professionals){
            for(def object:params.professionals){
                Professional professional=object
                if(object.branch!=null){
                    def branch=params.branches.find { element -> element.id == object.branch.id}
                    professional.branch=Branch.get(branch.newId)
                }
                if(professional.services!=null) professional.services.clear()
                if(object.services!=null){
                    def services=params.services.findAll {
                        element -> element.id in object.services.id
                    }
                    for(def service:services){
                        professional.services.add(Service.get(service.newId))
                    }
                }
                if(professional.workingHours!=null) professional.workingHours.clear()
                if(object.workingHours!=null){
                    def workingHours=params.workingHours.findAll {
                        element -> element.id in object.workingHours.id
                    }
                    for(def workingHour:workingHours){
                        professional.workingHours.add(WorkingHour.get(workingHour.newId))
                    }
                }
                Professional.withNewTransaction {
                    professional.save()
                }
                object.newId=professional.id
            }
        }
        if(params.promotions){
            for(def object:params.promotions){
                Promotion promotion=object
                promotion.startDate=object.startDate?DateTimeParser.parse(object.startDate):null
                promotion.endDate=object.endDate?DateTimeParser.parse(object.endDate):null
                if(promotion.services!=null) promotion.services.clear()
                if(object.services!=null){
                    def services=params.services.findAll {
                        element -> element.id in object.services.id
                    }
                    for(def service:services){
                        promotion.services.add(Service.get(service.newId))
                    }
                }
                Promotion.withNewTransaction {
                    promotion.save()
                }
                object.newId=promotion.id
            }
        }
        if(params.appointments){
            for(def object:params.appointments){
                Appointment appointment=object
                appointment.dayHour=object.dayHour?DateTimeParser.parse(object.dayHour):null
                appointment.endDate=object.endDate?DateTimeParser.parse(object.endDate):null
                appointment.payments=null
                if(object.branch!=null){
                    def branch=params.branches.find { element -> element.id == object.branch.id}
                    appointment.branch=Branch.get(branch.newId)
                }
                if(object.professional!=null){
                    def professional=params.professionals.find { element -> element.id == object.professional.id}
                    appointment.professional=Professional.get(professional.newId)
                }
                if(object.client!=null){
                    def client=params.clients.find { element -> element.id == object.client.id}
                    appointment.client=Client.get(client.newId)
                }
                if(appointment.services!=null) appointment.services.clear()
                if(object.services!=null){
                    def services=params.services.findAll {
                        element -> element.id in object.services.id
                    }
                    for(def service:services){
                        appointment.services.add(Service.get(service.newId))
                    }
                }
                if(appointment.promotions!=null) appointment.promotions.clear()
                if(object.promotions!=null){
                    def promotions=params.promotions.findAll {
                        element -> element.id in object.promotions.id
                    }
                    for(def promotion:promotions){
                        appointment.promotions.add(Promotion.get(promotion.newId))
                    }
                }
                Appointment.withNewTransaction {
                    appointment.save()
                }
                object.newId=appointment.id
            }
        }
        if(params.payments){
            for(def object:params.payments){
                Payment payment=object
                if(object.appointment!=null){
                    def appointment=params.appointments.find { element -> element.id == object.appointment.id}
                    payment.appointment=Appointment.get(appointment.newId)
                }
                payment.save()
                object.newId=payment.id
            }
        }
        if(params.accountMovements){
            for(def object:params.accountMovements){
                AccountMovement accountMovement=object
                if(object.appointments!=null){
                    def appointment=params.appointments.find { element -> element.id == object.appointment.id}
                    accountMovement.appointment=Appointment.get(appointment.newId)
                }
                accountMovement.save()
                object.newId=accountMovement.id
            }
        }
        if(params.accountancy){
            for(def object:params.accountancy){
                Accountancy accountancy=object
                if(accountancy.accountMovements!=null) accountancy.accountMovements.clear()
                if(object.accountMovements!=null){
                    def accountMovements=params.accountMovements.findAll {
                        element -> element.id in object.accountMovements.id
                    }
                    for(def accountMovement:accountMovements){
                        accountancy.accountMovements.add(AccountMovement.get(accountMovement.newId))
                    }
                }
                accountancy.save()
                object.newId=accountancy.id
            }
        }
        if(params.clients){
            for(def object:params.clients){
                Client client=object
                if(object.accountancy!=null){
                    def accountancy=params.accountancy.find { element -> element.id == object.accountancy.id}
                    client.accountancy=Accountancy.get(accountancy.newId)
                }
                client.save()
                object.newId=client.id
            }
        }
        if(params.serviceReports){
            for(def object:params.serviceReports){
                ServiceReport serviceReport=object
                if(object.service!=null){
                    def service=params.services.find { element -> element.id == object.service.id}
                    serviceReport.service=Service.get(service.newId)
                }
                serviceReport.save()
                object.newId=serviceReport.id
            }
        }
        if(params.professionalReports){
            for(def object:params.professionalReports){
                ProfessionalReport professionalReport=object
                if(object.professional!=null){
                    def professional=params.professionals.find { element -> element.id == object.professional.id}
                    professionalReport.professional=Professional.get(professional.newId)
                }
                professionalReport.save()
                object.newId=professionalReport.id
            }
        }
    }
}
