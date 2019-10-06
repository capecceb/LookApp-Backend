package lookapp.backend

class BootStrap {

    def init = { servletContext ->
        test {
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

                Service service3 =new Service()
                service3.name="peinado"
                service3.duration=30
                service3.cost=150
                service3.save()

                Professional professional=new Professional()
                professional.name="Pedro"
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

                Client cliente1 = new Client()
                cliente1.name = "cliente2"
                cliente1.surname = "apellido1"
                cliente1.DNI= "1312315"
                cliente1.primaryPhone= 12312
                cliente1.secondPhone= 123333
                cliente1.save()

                Appointment turn=new Appointment()
                turn.local="casa"
                turn.client = cliente
                Calendar cal = Calendar.getInstance()
                cal.set(2019,9,29)
                turn.dayHour= cal.getTime()
                turn.services=new ArrayList<Service>()
                turn.services.add(service)
                turn.appointmentStatus = AppointmentStatus.OPEN
                turn.save()

                Appointment turn1=new Appointment()
                turn1.local="San Miguel"
                turn1.client = cliente1
                Calendar cal1 = Calendar.getInstance()
                cal1.set(2019,9,28)
                turn1.dayHour= cal1.getTime()
                turn1.services=new ArrayList<Service>()
                turn1.services.add(service2)
                turn1.appointmentStatus = AppointmentStatus.OPEN
                turn1.save()

                Appointment turn2=new Appointment()
                turn2.local="San Miguel"
                Calendar cal2 = Calendar.getInstance()
                cal2.set(2019,9, 30)
                turn2.dayHour= cal2.getTime()
                turn2.services=new ArrayList<Service>()
                turn2.services.add(service3)
                turn2.appointmentStatus = AppointmentStatus.OPEN
                turn2.save()

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

                Service service3 =new Service()
                service3.name="peinado"
                service3.duration=30
                service3.cost=150
                service3.save()

                Professional professional=new Professional()
                professional.name="Pedro"
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

                Client cliente1 = new Client()
                cliente1.name = "cliente2"
                cliente1.surname = "apellido1"
                cliente1.DNI= "1312315"
                cliente1.primaryPhone= 12312
                cliente1.secondPhone= 123333
                cliente1.save()

                Appointment turn=new Appointment()
                turn.local="casa"
                turn.client = cliente
                Calendar cal = Calendar.getInstance()
                cal.set(2019,9,29)
                turn.dayHour= cal.getTime()
                turn.services=new ArrayList<Service>()
                turn.services.add(service)
                turn.appointmentStatus = AppointmentStatus.OPEN
                turn.save()

                Appointment turn1=new Appointment()
                turn1.local="San Miguel"
                turn1.client = cliente1
                Calendar cal1 = Calendar.getInstance()
                cal1.set(2019,9,28)
                turn1.dayHour= cal1.getTime()
                turn1.services=new ArrayList<Service>()
                turn1.services.add(service2)
                turn1.appointmentStatus = AppointmentStatus.OPEN
                turn1.save()

                Appointment turn2=new Appointment()
                turn2.local="San Miguel"
                Calendar cal2 = Calendar.getInstance()
                cal2.set(2019,9, 30)
                turn2.dayHour= cal2.getTime()
                turn2.services=new ArrayList<Service>()
                turn2.services.add(service3)
                turn2.appointmentStatus = AppointmentStatus.OPEN
                turn2.save()

            }
        }
    }
    def destroy = {
    }
}
