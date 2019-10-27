package lookapp.backend

class BootStrap {

    def init = { servletContext ->
        test {
            addData()
        }
        development {
           addData()
        }
    }

    private addData(){
        User.withNewTransaction {

            Branch branch = new Branch()
            branch.name = "Hair&Head_Pacheco"
            branch.address = "Henry Ford 1234"
            branch.status = BranchStatus.ACTIVE            
            branch.professionals = new ArrayList<Professional>()
            branch.users = new ArrayList<User>()
            branch.save()

            Rol administrador = new Rol(name: "Administrador").save()
            Rol supervidor = new Rol(name: "Supervisor").save()
            Rol contador = new Rol(name: "Contador").save()

            User pedro = new User()
            pedro.fullName = "pedro"
            pedro.email = "juan@yahoo.com"
            pedro.password = "pedro"
            pedro.roles = new ArrayList()
            pedro.roles.add(administrador)
            pedro.status = UserStatus.ACTIVE
            pedro.save()

            User bruno = new User()
            bruno.fullName = "bruno"
            bruno.email = "juan@yahoo.com"
            bruno.roles = new ArrayList<Rol>()
            bruno.roles.add(supervidor)
            bruno.status = UserStatus.ACTIVE
            bruno.save()

            User nico = new User()
            nico.fullName = "nico"
            nico.email = "nicolas@yahoo.com"
            nico.roles = new ArrayList<Rol>()
            nico.roles.add(contador)
            nico.status = UserStatus.INACTIVE
            nico.branch = branch
            nico.save()

            branch.users.add(nico)

            User jere = new User()
            jere.fullName = "jere"
            jere.email = "jeremias@yahoo.com"
            jere.roles = new ArrayList<Rol>()
            jere.roles.add(contador)
            jere.status = UserStatus.INACTIVE
            jere.save()

            Service service=new Service()
            service.name="corte"
            service.duration=60
            service.price=200
            service.save()

            Service service2 =new Service()
            service2.name="tintura"
            service2.duration=60
            service2.price=250
            service2.save()

            Service service3 =new Service()
            service3.name="peinado"
            service3.duration=30
            service3.price=150
            service3.save()

            WorkingHour workingHourMonday=new WorkingHour();
            workingHourMonday.days=Day.MONDAY
            workingHourMonday.beginHour=9
            workingHourMonday.endHour=18
            workingHourMonday.save()

            WorkingHour workingHourTuesday=new WorkingHour();
            workingHourTuesday.days=Day.TUESDAY
            workingHourTuesday.beginHour=9
            workingHourTuesday.endHour=18
            workingHourTuesday.save()

            WorkingHour workingHourWednesday=new WorkingHour();
            workingHourWednesday.days=Day.WEDNESDAY
            workingHourWednesday.beginHour=9
            workingHourWednesday.endHour=18
            workingHourWednesday.save()

            WorkingHour workingHourThursday=new WorkingHour();
            workingHourThursday.days=Day.THURSDAY
            workingHourThursday.beginHour=9
            workingHourThursday.endHour=18
            workingHourThursday.save()

            WorkingHour workingHourFriday=new WorkingHour();
            workingHourFriday.days=Day.FRIDAY
            workingHourFriday.beginHour=9
            workingHourFriday.endHour=18
            workingHourFriday.save()

            WorkingHour workingHourSaturday=new WorkingHour();
            workingHourSaturday.days=Day.SATURDAY
            workingHourSaturday.beginHour=9
            workingHourSaturday.endHour=18
            workingHourSaturday.save()

            WorkingHour workingHourSunday=new WorkingHour();
            workingHourSunday.days=Day.SUNDAY
            workingHourSunday.beginHour=9
            workingHourSunday.endHour=18
            workingHourSunday.save()

            Professional professional=new Professional()
            professional.name="Pedro"
            professional.lastName = "apellido"
            professional.phone = "1245789655"
            professional.email = "pedro@gmail.com"
            professional.services=new ArrayList<Service>()
            professional.services.add(service)
            professional.status=ProfessionalStatus.ACTIVE
            professional.workingHours=new ArrayList<WorkingHour>()
            professional.workingHours.add(workingHourMonday)
            professional.workingHours.add(workingHourSaturday)
            professional.branch = branch
            professional.save()

            branch.professionals.add(professional)

            Client cliente = new Client()
            cliente.name = "cliente1"
            cliente.lastName = "apellido"
            cliente.DNI= "151515"
            cliente.email= "cliente@gmail.com"
            cliente.primaryPhone= 12312
            cliente.secondPhone= 15151666
            cliente.status = ClientStatus.VIP
            cliente.save()

            Client cliente1 = new Client()
            cliente1.name = "cliente2"
            cliente1.lastName = "apellido1"
            cliente1.DNI= "1312315"
            cliente1.email= "otroCliente@gmail.com"
            cliente1.primaryPhone= 12312
            cliente1.secondPhone= 123333
            cliente1.status = ClientStatus.NORMAL
            cliente1.save()

            Appointment turn=new Appointment()
            turn.local="casa"
            turn.client = cliente
            Calendar cal = Calendar.getInstance()
            cal.set(2019,9,29)
            turn.dayHour= cal.getTime()
            turn.services=new ArrayList<Service>()
            turn.services.add(service)
            turn.status = AppointmentStatus.OPEN
            turn.save()

            Appointment turn1=new Appointment()
            turn1.local="San Miguel"
            turn1.client = cliente1
            Calendar cal1 = Calendar.getInstance()
            cal1.set(2019,9,28)
            turn1.dayHour= cal1.getTime()
            turn1.services=new ArrayList<Service>()
            turn1.services.add(service2)
            turn1.status = AppointmentStatus.OPEN
            turn1.save()

            Appointment turn2=new Appointment()
            turn2.local="San Miguel"
            Calendar cal2 = Calendar.getInstance()
            cal2.set(2019,9, 30)
            turn2.dayHour= cal2.getTime()
            turn2.services=new ArrayList<Service>()
            turn2.services.add(service3)
            turn2.status = AppointmentStatus.OPEN
            turn2.save()

            Professional professional1 = new Professional()
            professional1.name = "nicolas"
            professional1.status = ProfessionalStatus.ACTIVE
            professional1.lastName = "lastName"
            professional1.phone = "1159891578"
            professional1.email = "niico786@gmail.com"
            professional.services=new ArrayList<Service>()
            professional.services.add(service3)
            professional1.workingHours=new ArrayList<WorkingHour>()
            professional1.workingHours.add(workingHourMonday)
            professional1.workingHours.add(workingHourSaturday)
            professional1.workingHours.add(workingHourFriday)
            professional1.branch = branch
            professional1.save()

            branch.professionals.add(professional1)
            branch.save(flush:true)

            Appointment turn3=new Appointment()
            turn3.local="San Miguel"
            Calendar cal3 = Calendar.getInstance()
            cal2.set(2019,9, 30)
            turn3.dayHour= cal2.getTime()
            turn3.professional= professional
            turn3.services=new ArrayList<Service>()
            turn3.services.add(service3)
            turn3.status = AppointmentStatus.OPEN
            turn3.save()

        }
    }

    def destroy = {
    }
}
