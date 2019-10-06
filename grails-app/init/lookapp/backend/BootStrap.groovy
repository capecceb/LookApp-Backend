package lookapp.backend

class BootStrap {

    def init = { servletContext ->
        test {
            User.withNewTransaction {
                Rol administrador = new Rol(name: "Administrador").save()
                User pedro = new User()
                pedro.username = "pedro"
                pedro.password = "pedro"
                pedro.roles = new ArrayList()
                pedro.roles.add(administrador)
                pedro.save()
            }
            Service.withNewTransaction {
                Service service=new Service()
                service.name="corte"
                service.duration=60
                service.cost=200
                service.save()
            }
            Professional.withNewTransaction {
                Professional professional = new Professional()
                professional.name = "nicolas"
                professional.status = ProfessionalStatus.ACTIVE
                professional.lastName = "lastName"
                professional.phone = "1159891578"
                professional.email = "niico786@gmail.com"
                professional.save()
            }
        }
        development {
            User.withNewTransaction {

                Rol administrador = new Rol(name: "Administrador").save()
                Rol supervidor = new Rol(name: "Supervisor").save()
                Rol contador = new Rol(name: "Contador").save()

                User pedro = new User()
                pedro.username = "pedro"
                pedro.password = "pedro"
                pedro.roles = new ArrayList()
                pedro.roles.add(administrador)
                pedro.save()

                User bruno = new User()
                bruno.username = "bruno"
                bruno.roles = new ArrayList<Rol>()
                bruno.roles.add(supervidor)
                bruno.save()

                User nico = new User()
                nico.username = "nicolas"
                nico.roles = new ArrayList<Rol>()
                nico.roles.add(contador)
                nico.save()

                User jere = new User()
                jere.username = "jeremias"
                jere.roles = new ArrayList<Rol>()
                jere.roles.add(contador)
                jere.save()

                Service service=new Service()
                service.name="corte"
                service.duration=60
                service.cost=200
                service.save()

                Service service2 =new Service()
                service2.name="tintura"
                service2.duration=60
                service2.cost=250
                service2.save()

                Professional professional=new Professional()
                professional.name="nicolas"
                professional.services=new ArrayList<Service>()
                professional.services.add(service)
                professional.status=ProfessionalStatus.ACTIVE
                professional.save()

                Client cliente = new Client()
                cliente.name = "cliente1"
                cliente.surname = "apellido"
                cliente.DNI= "151515"
                cliente.primaryPhone= 12312
                cliente.save()

                Appointment turn=new Appointment()
                turn.id = 1
                turn.local="casa"
                turn.client = cliente
                turn.dayHour= new Date()
                turn.services=new ArrayList<Service>()
                turn.services.add(service)
                turn.appointmentStatus = AppointmentStatus.OPEN
                turn.save()

                Appointment turn1=new Appointment()
                turn1.local="San Miguel"
                turn1.client = cliente
                turn1.dayHour= new Date()
                turn1.services=new ArrayList<Service>()
                turn1.services.add(service2)
                turn1.appointmentStatus = AppointmentStatus.OPEN
                turn1.save()

            }
        }
    }
    def destroy = {
    }
}
