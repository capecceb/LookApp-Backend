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

    private addData() {
        User.withNewTransaction {

            Branch branch = new Branch()
            branch.name = "Hair&Head_Pacheco"
            branch.location = "Pacheco"
            branch.street_name = "Henry Ford"
            branch.street_number = 1234
            branch.status = BranchStatus.ACTIVE
            branch.professionals = new ArrayList<Professional>()
            branch.users = new ArrayList<User>()
            branch.save()

            Branch branch2 = new Branch()
            branch2.name = "Hair&Head_San_Miguel"
            branch2.location = "San Miguel"
            branch2.street_name = "Paunero"
            branch2.street_number = 5678
            branch2.status = BranchStatus.ACTIVE
            branch2.professionals = new ArrayList<Professional>()
            branch2.users = new ArrayList<User>()
            branch2.save()

            Rol administrador = new Rol(name: "Administrador").save()
            Rol supervidor = new Rol(name: "Supervisor").save()
            Rol contador = new Rol(name: "Contador").save()

            User pedro = new User()
            pedro.name = "pedro"
            pedro.email = "juan@yahoo.com"
            pedro.password = "pedro"
            pedro.roles = new ArrayList()
            pedro.roles.add(administrador)
            pedro.status = UserStatus.ACTIVE
            pedro.branch = branch2
            pedro.save()

            branch2.users.add(pedro)

            User bruno = new User()
            bruno.name = "bruno"
            bruno.email = "bruno@yahoo.com"
            bruno.roles = new ArrayList<Rol>()
            bruno.roles.add(supervidor)
            bruno.status = UserStatus.ACTIVE
            bruno.branch = branch2
            bruno.save()

            branch2.users.add(bruno)

            User nico = new User()
            nico.name = "nico"
            nico.email = "nicolas@yahoo.com"
            nico.roles = new ArrayList<Rol>()
            nico.roles.add(contador)
            nico.status = UserStatus.INACTIVE
            nico.branch = branch
            nico.save()

            branch.users.add(nico)

            User jere = new User()
            jere.name = "jere"
            jere.email = "jeremias@yahoo.com"
            jere.roles = new ArrayList<Rol>()
            jere.roles.add(contador)
            jere.status = UserStatus.ACTIVE
            jere.branch = null
            jere.password = "test"
            jere.save()

            Service service = new Service()
            service.name = "corte"
            service.duration = 60
            service.price = 200
            service.save()

            Service service2 = new Service()
            service2.name = "tintura"
            service2.duration = 60
            service2.price = 250
            service2.save()

            Service service3 = new Service()
            service3.name = "peinado"
            service3.duration = 30
            service3.price = 150
            service3.save()

            WorkingHour workingHourMonday = new WorkingHour();
            workingHourMonday.days = Day.MONDAY
            workingHourMonday.beginHour = 9
            workingHourMonday.endHour = 18
            workingHourMonday.save()

            WorkingHour workingHourTuesday = new WorkingHour();
            workingHourTuesday.days = Day.TUESDAY
            workingHourTuesday.beginHour = 9
            workingHourTuesday.endHour = 18
            workingHourTuesday.save()

            WorkingHour workingHourWednesday = new WorkingHour();
            workingHourWednesday.days = Day.WEDNESDAY
            workingHourWednesday.beginHour = 9
            workingHourWednesday.endHour = 18
            workingHourWednesday.save()

            WorkingHour workingHourThursday = new WorkingHour();
            workingHourThursday.days = Day.THURSDAY
            workingHourThursday.beginHour = 9
            workingHourThursday.endHour = 18
            workingHourThursday.save()

            WorkingHour workingHourFriday = new WorkingHour();
            workingHourFriday.days = Day.FRIDAY
            workingHourFriday.beginHour = 9
            workingHourFriday.endHour = 18
            workingHourFriday.save()

            WorkingHour workingHourSaturday = new WorkingHour();
            workingHourSaturday.days = Day.SATURDAY
            workingHourSaturday.beginHour = 9
            workingHourSaturday.endHour = 18
            workingHourSaturday.save()

            WorkingHour workingHourSunday = new WorkingHour();
            workingHourSunday.days = Day.SUNDAY
            workingHourSunday.beginHour = 9
            workingHourSunday.endHour = 18
            workingHourSunday.save()

            Professional professional = new Professional()
            professional.name = "Pedro"
            professional.lastName = "apellido"
            professional.phone = "1245789655"
            professional.email = "pedro@gmail.com"
            professional.services = new ArrayList<Service>()
            professional.services.add(service)
            professional.status = ProfessionalStatus.ACTIVE
            professional.workingHours = new ArrayList<WorkingHour>()
            professional.workingHours.add(workingHourMonday)
            professional.workingHours.add(workingHourSaturday)
            professional.branch = branch
            professional.save()

            branch.professionals.add(professional)
            branch.save(flush: true)

            Client cliente = new Client()
            cliente.name = "cliente1"
            cliente.lastName = "apellido"
            cliente.DNI = "151515"
            cliente.email = "cliente@gmail.com"
            cliente.primaryPhone = 12312
            cliente.secondPhone = 15151666
            cliente.status = ClientStatus.VIP
            cliente.points = 100
            cliente.save()

            Client cliente1 = new Client()
            cliente1.name = "cliente2"
            cliente1.lastName = "apellido1"
            cliente1.DNI = "1312315"
            cliente1.email = "otroCliente@gmail.com"
            cliente1.primaryPhone = 12312
            cliente1.secondPhone = 123333
            cliente1.status = ClientStatus.NORMAL
            cliente1.points = 1000
            cliente1.save()

            Client cliente2 = new Client()
            cliente2.name = "clien"
            cliente2.lastName = "sinPuntos"
            cliente2.DNI= "1312315"
            cliente2.email= "otroCliente@gmail.com"
            cliente2.primaryPhone= 12312
            cliente2.secondPhone= 123333
            cliente2.status = ClientStatus.NORMAL
            cliente2.points = 0
            cliente2.save()

            Appointment turn = new Appointment()
            turn.local = "casa"
            turn.client = cliente
            Calendar cal = Calendar.getInstance()
            cal.set(2019, 9, 29)
            turn.dayHour = cal.getTime()
            turn.services = new ArrayList<Service>()
            turn.services.add(service)
            turn.status = AppointmentStatus.OPEN
            turn.branch = branch
            turn.save()

            Appointment turn1 = new Appointment()
            turn1.local = "San Miguel"
            turn1.client = cliente1
            Calendar cal1 = Calendar.getInstance()
            cal1.set(2019, 9, 28)
            turn1.dayHour = cal1.getTime()
            turn1.services = new ArrayList<Service>()
            turn1.services.add(service2)
            turn1.status = AppointmentStatus.OPEN
            turn1.branch = branch
            turn1.save()

            Appointment turn2 = new Appointment()
            turn2.local = "San Miguel"
            Calendar cal2 = Calendar.getInstance()
            cal2.set(2019,9, 30)
            turn2.client =cliente1
            turn2.dayHour= cal2.getTime()
            turn2.services=new ArrayList<Service>()
            turn2.services.add(service3)
            turn2.status = AppointmentStatus.OPEN
            turn2.branch = branch
            turn2.save()

            Professional professional1 = new Professional()
            professional1.name = "nicolas"
            professional1.status = ProfessionalStatus.ACTIVE
            professional1.lastName = "lastName"
            professional1.phone = "1159891578"
            professional1.email = "niico786@gmail.com"
            professional.services = new ArrayList<Service>()
            professional.services.add(service3)
            professional1.workingHours = new ArrayList<WorkingHour>()
            professional1.workingHours.add(workingHourMonday)
            professional1.workingHours.add(workingHourSaturday)
            professional1.workingHours.add(workingHourFriday)
            professional1.branch = branch2
            professional1.save()

            branch2.professionals.add(professional1)
            branch2.save(flush: true)

            Appointment turn3 = new Appointment()
            turn3.local = "San Miguel"
            Calendar cal3 = Calendar.getInstance()
            cal2.set(2019, 9, 30)
            turn3.dayHour = cal2.getTime()
            turn3.professional = professional
            turn3.services = new ArrayList<Service>()
            turn3.services.add(service3)
            turn3.status = AppointmentStatus.OPEN
            turn3.branch = branch
            turn3.save()

            Appointment turn4=new Appointment()
            turn4.local="San Miguel"
            Calendar cal4 = Calendar.getInstance()
            cal4.set(2019,10, 30)
            turn4.client =cliente1
            turn4.dayHour= cal4.getTime()
            turn4.services=new ArrayList<Service>()
            turn4.services.add(service3)
            turn4.status = AppointmentStatus.OPEN
            turn4.branch = branch
            turn4.save()

            Appointment turn5=new Appointment()
            turn5.local="San Miguel"
            Calendar cal5 = Calendar.getInstance()
            cal5.set(2019,10, 29)
            turn5.client =cliente1
            turn5.dayHour= cal5.getTime()
            turn5.services=new ArrayList<Service>()
            turn5.services.add(service3)
            turn5.status = AppointmentStatus.OPEN
            turn5.branch = branch
            turn5.save()

            Appointment turn6=new Appointment()
            turn6.local="San Miguel"
            Calendar cal6 = Calendar.getInstance()
            cal6.set(2019,10, 28)
            turn6.client =cliente
            turn6.dayHour= cal6.getTime()
            turn6.services=new ArrayList<Service>()
            turn6.services.add(service3)
            turn6.status = AppointmentStatus.OPEN
            turn6.branch = branch
            turn6.save()

            Appointment turn7=new Appointment()
            turn7.local="San Miguel"
            Calendar cal7 = Calendar.getInstance()
            cal7.set(2019,10, 27)
            turn7.dayHour= cal7.getTime()
            turn7.services=new ArrayList<Service>()
            turn7.services.add(service3)
            turn7.status = AppointmentStatus.OPEN
            turn7.save()

            Config configChangePay=new Config(key: "changePay",value:"1").save()
            Config configChangePurchase=new Config(key: "changePurchase",value:"2").save()
        }
    }

    def destroy = {
    }
}
