package lookapp.backend

class BootStrap {

    def init = { servletContext ->
        test {
            addData()
        }
        development {
            addData()
            AppointmentJob.run()
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
            cliente.name = "Jeremias"
            cliente.lastName = "Vallejo"
            cliente.DNI = "151515"
            cliente.email = "jerevallejo@gmail.com"
            cliente.primaryPhone = 12312
            cliente.secondPhone = 15151666
            cliente.status = ClientStatus.VIP
            cliente.points = 100
            cliente.accountancy = new Accountancy()
            cliente.accountancy.accountMovements = new ArrayList<AccountMovement>()
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
            cliente1.accountancy = new Accountancy()
            cliente1.accountancy.accountMovements = new ArrayList<AccountMovement>()
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
            cliente2.accountancy = new Accountancy()
            cliente2.accountancy.accountMovements = new ArrayList<AccountMovement>()
            cliente2.save()

            Client cliente3 = new Client()
            cliente3.name = "cliente3"
            cliente3.lastName = "apellido3"
            cliente3.DNI= "14725836"
            cliente3.email= "otroCliente3@gmail.com"
            cliente3.primaryPhone= 14586
            cliente3.secondPhone= 164583
            cliente3.status = ClientStatus.NORMAL
            cliente3.points = 0
            cliente3.accountancy = new Accountancy()
            cliente3.accountancy.accountMovements = new ArrayList<AccountMovement>()
            cliente3.save()

            Client cliente4 = new Client()
            cliente4.name = "cliente4"
            cliente4.lastName = "apellido4"
            cliente4.DNI= "32465819"
            cliente4.email= "otrocliente4@gmail.com"
            cliente4.primaryPhone= 123546
            cliente4.secondPhone= 1564984
            cliente4.status = ClientStatus.NORMAL
            cliente4.points = 0
            cliente4.accountancy = new Accountancy()
            cliente4.accountancy.accountMovements = new ArrayList<AccountMovement>()
            cliente4.save()

            Client cliente5 = new Client()
            cliente5.name = "cliente5"
            cliente5.lastName = "apellido5"
            cliente5.DNI= "32465819"
            cliente5.email= "otrocliente5@gmail.com"
            cliente5.primaryPhone= 123546
            cliente5.secondPhone= 1564984
            cliente5.status = ClientStatus.NORMAL
            cliente5.points = 0
            cliente5.accountancy = new Accountancy()
            cliente5.accountancy.accountMovements = new ArrayList<AccountMovement>()
            cliente5.save()

            Client cliente6 = new Client()
            cliente6.name = "cliente6"
            cliente6.lastName = "apellido6"
            cliente6.DNI= "32465819"
            cliente6.email= "otrocliente6@gmail.com"
            cliente6.primaryPhone= 123546
            cliente6.secondPhone= 1564984
            cliente6.status = ClientStatus.NORMAL
            cliente6.points = 0
            cliente6.accountancy = new Accountancy()
            cliente6.accountancy.accountMovements = new ArrayList<AccountMovement>()
            cliente6.save()

            Client cliente7 = new Client()
            cliente7.name = "cliente7"
            cliente7.lastName = "apellido7"
            cliente7.DNI= "32465819"
            cliente7.email= "otrocliente7@gmail.com"
            cliente7.primaryPhone= 123546
            cliente7.secondPhone= 1564984
            cliente7.status = ClientStatus.NORMAL
            cliente7.points = 0
            cliente7.accountancy = new Accountancy()
            cliente7.accountancy.accountMovements = new ArrayList<AccountMovement>()
            cliente7.save()

            Appointment turn = new Appointment()
            turn.local = "casa"
            turn.client = cliente
            Calendar cal = Calendar.getInstance()
            cal.set(2019, 9, 29,9,0,0)
            turn.dayHour = cal.getTime()
            cal.add(Calendar.MINUTE,60)
            turn.endDate = cal.getTime()
            turn.services = new ArrayList<Service>()
            turn.services.add(service)
            turn.status = AppointmentStatus.OPEN
            turn.branch = branch
            turn.totalPrice = 200
            turn.totalToPay = 200
            turn.save()

            Appointment turn1 = new Appointment()
            turn1.local = "San Miguel"
            turn1.client = cliente1
            Calendar cal1 = Calendar.getInstance()
            cal1.set(2019, 9, 28,9,0,0)
            turn1.dayHour = cal1.getTime()
            turn1.services = new ArrayList<Service>()
            turn1.services.add(service2)
            turn1.status = AppointmentStatus.OPEN
            turn1.branch = branch
            turn1.totalPrice = 250
            turn1.totalToPay = 250
            turn1.save()

            Appointment turn2 = new Appointment()
            turn2.local = "San Miguel"
            Calendar cal2 = Calendar.getInstance()
            cal2.set(2019,9, 30,9,0,0)
            turn2.client =cliente1
            turn2.dayHour= cal2.getTime()
            turn2.services=new ArrayList<Service>()
            turn2.services.add(service3)
            turn2.status = AppointmentStatus.OPEN
            turn2.branch = branch
            turn2.totalPrice = 150
            turn2.totalToPay = 150
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
            professional1.branch = branch
            professional1.save()

            branch2.professionals.add(professional1)
            branch2.save(flush: true)

            Appointment turn3 = new Appointment()
            turn3.local = "San Miguel"
            Calendar cal3 = Calendar.getInstance()
            cal3.set(2019, 9, 30,9,0,0)
            turn3.dayHour = cal2.getTime()
            turn3.professional = professional
            turn3.services = new ArrayList<Service>()
            turn3.services.add(service3)
            turn3.status = AppointmentStatus.OPEN
            turn3.branch = branch2
            turn3.totalPrice = 150
            turn3.totalToPay = 150
            turn3.save()

            Appointment turn4=new Appointment()
            turn4.local="San Miguel"
            Calendar cal4 = Calendar.getInstance()
            cal4.set(2019,9, 30,9,0,0)
            turn4.client =cliente1
            turn4.dayHour= cal4.getTime()
            turn4.services=new ArrayList<Service>()
            turn4.services.add(service3)
            turn4.status = AppointmentStatus.OPEN
            turn4.branch = branch
            turn4.totalPrice = 150
            turn4.totalToPay = 150
            turn4.save()

            Appointment turn5=new Appointment()
            turn5.local="San Miguel"
            Calendar cal5 = Calendar.getInstance()
            cal5.set(2019,9, 29,9,0,0)
            turn5.client =cliente1
            turn5.dayHour= cal5.getTime()
            turn5.services=new ArrayList<Service>()
            turn5.services.add(service3)
            turn5.status = AppointmentStatus.OPEN
            turn5.branch = branch
            turn5.totalPrice = 150
            turn5.totalToPay = 150
            turn5.save()

            Appointment turn6=new Appointment()
            turn6.local="San Miguel"
            Calendar cal6 = Calendar.getInstance()
            cal6.set(2019,9, 28,9,0,0)
            turn6.client =cliente
            turn6.dayHour= cal6.getTime()
            turn6.services=new ArrayList<Service>()
            turn6.services.add(service3)
            turn6.status = AppointmentStatus.OPEN
            turn6.branch = branch
            turn6.totalPrice = 150
            turn6.totalToPay = 150
            turn6.save()

            Appointment turn7=new Appointment()
            turn7.local="???"
            Calendar cal7 = Calendar.getInstance()
            cal7.set(2019,9, 27,9,0,0)
            turn7.dayHour= cal7.getTime()
            turn7.services=new ArrayList<Service>()
            turn7.services.add(service3)
            turn7.status = AppointmentStatus.OPEN
            turn7.branch = branch
            turn7.totalPrice = 150
            turn7.totalToPay = 150
            turn7.save()


            Appointment turn8=new Appointment()
            turn8.local="San Miguel8"
            Calendar cal8 = Calendar.getInstance()
            cal8.set(2019,10, 13,9,0,0)
            turn8.dayHour= cal8.getTime()
            turn8.professional = professional
            turn8.services=new ArrayList<Service>()
            turn8.services.add(service3)
            turn8.services.add(service2)
            turn8.status = AppointmentStatus.OPEN
            turn8.branch = branch
            turn8.totalPrice = 400
            turn8.totalToPay = 400
            turn8.save()

            Payment payment = new Payment()
            payment.appointment = turn8
            payment.currency= "ARG"
            payment.amount = 400
            payment.save()

            turn8.payments= new ArrayList<Payment>()
            turn8.payments.add(payment)
            turn8.status =AppointmentStatus.PAID
            turn8.save()

            Appointment turn9=new Appointment()
            turn9.local="San Miguel9"
            Calendar cal9 = Calendar.getInstance()
            cal9.set(2019,10, 10,9,0,0)
            turn9.dayHour= cal9.getTime()
            turn9.professional = professional1
            turn9.services=new ArrayList<Service>()
            turn9.services.add(service3)
            turn9.status = AppointmentStatus.OPEN
            turn9.branch = branch2
            turn9.totalPrice = 150
            turn9.totalToPay = 150
            turn9.save()

            Payment payment1 = new Payment()
            payment1.appointment = turn9
            payment1.currency= "ARG"
            payment1.amount = 150
            payment1.save()

            turn9.payments= new ArrayList<Payment>()
            turn9.payments.add(payment1)
            turn9.status =AppointmentStatus.PAID
            turn9.save()

            Appointment turn10=new Appointment()
            turn10.local="San Miguel10"
            Calendar cal10 = Calendar.getInstance()
            cal10.set(2019,10, 05,9,0,0)
            turn10.dayHour= cal10.getTime()
            turn10.professional = professional
            turn10.services=new ArrayList<Service>()
            turn10.services.add(service3)
            turn10.services.add(service2)
            turn10.services.add(service)
            turn10.status = AppointmentStatus.OPEN
            turn10.branch = branch
            turn10.totalPrice = 500
            turn10.totalToPay = 500
            turn10.save()

            Payment payment3 = new Payment()
            payment3.appointment = turn10
            payment3.currency= "ARG"
            payment3.amount = 600
            payment3.save()

            turn10.payments= new ArrayList<Payment>()
            turn10.payments.add(payment3)
            turn10.status =AppointmentStatus.PAID
            turn10.save()

            Appointment turn11=new Appointment()
            turn11.local="San Migue11"
            Calendar cal11 = Calendar.getInstance()
            cal11.set(2019,10, 25,9,0,0)
            turn11.dayHour= cal11.getTime()
            turn11.professional = professional1
            turn11.services=new ArrayList<Service>()
            turn11.services.add(service)
            turn11.status = AppointmentStatus.OPEN
            turn11.branch = branch2
            turn11.totalPrice = 500
            turn11.totalToPay = 500
            turn11.save()

            Appointment turn12=new Appointment()
            turn12.local="San Migue12"
            Calendar cal12 = Calendar.getInstance()
            cal11.set(2019,10, 25,9,0,0)
            turn12.dayHour= cal12.getTime()
            turn12.professional = professional1
            turn12.services=new ArrayList<Service>()
            turn12.services.add(service)
            turn12.status = AppointmentStatus.OPEN
            turn12.branch = branch2
            turn12.totalPrice = 100
            turn12.totalToPay = 100
            turn12.client = cliente3
            turn12.save()

            Appointment turn13=new Appointment()
            turn13.local="San Migue13"
            Calendar cal13 = Calendar.getInstance()
            cal11.set(2019,10, 25,9,0,0)
            turn13.dayHour= cal13.getTime()
            turn13.professional = professional1
            turn13.services=new ArrayList<Service>()
            turn13.services.add(service)
            turn13.status = AppointmentStatus.OPEN
            turn13.branch = branch2
            turn13.totalPrice = 100
            turn13.totalToPay = 100
            turn13.client = cliente4
            turn13.save()

            Appointment turn14=new Appointment()
            turn14.local="San Migue13"
            Calendar cal14 = Calendar.getInstance()
            cal11.set(2019,10, 25,9,0,0)
            turn14.dayHour= cal14.getTime()
            turn14.professional = professional1
            turn14.services=new ArrayList<Service>()
            turn14.services.add(service)
            turn14.status = AppointmentStatus.OPEN
            turn14.branch = branch2
            turn14.totalPrice = 100
            turn14.totalToPay = 100
            turn14.client = cliente5
            turn14.save()

            Appointment turn15=new Appointment()
            turn15.local="San Migue13"
            Calendar cal15 = Calendar.getInstance()
            cal11.set(2019,10, 25,9,0,0)
            turn15.dayHour= cal15.getTime()
            turn15.professional = professional1
            turn15.services=new ArrayList<Service>()
            turn15.services.add(service)
            turn15.status = AppointmentStatus.OPEN
            turn15.branch = branch2
            turn15.totalPrice = 100
            turn15.totalToPay = 100
            turn15.client = cliente6
            turn15.save()

            Appointment turn16=new Appointment()
            turn16.local="San Migue13"
            Calendar cal16 = Calendar.getInstance()
            cal11.set(2019,10, 25,9,0,0)
            turn16.dayHour= cal16.getTime()
            turn16.professional = professional1
            turn16.services=new ArrayList<Service>()
            turn16.services.add(service)
            turn16.status = AppointmentStatus.OPEN
            turn16.branch = branch2
            turn16.totalPrice = 100
            turn16.totalToPay = 100
            turn16.client = cliente7
            turn16.save()

            Appointment turn17=new Appointment()
            turn17.local="San Migue13"
            Calendar cal17 = Calendar.getInstance()
            cal11.set(2019,10, 25,9,0,0)
            turn17.dayHour= cal17.getTime()
            turn17.professional = professional1
            turn17.services=new ArrayList<Service>()
            turn17.services.add(service)
            turn17.status = AppointmentStatus.OPEN
            turn17.branch = branch2
            turn17.totalPrice = 100
            turn17.totalToPay = 100
            turn17.save()

            Appointment turn18=new Appointment()
            turn18.local="San Migue18"
            Calendar cal18 = Calendar.getInstance()
            cal11.set(2019,10, 25,9,0,0)
            turn18.dayHour= cal17.getTime()
            turn18.professional = professional1
            turn18.services=new ArrayList<Service>()
            turn18.services.add(service)
            turn18.status = AppointmentStatus.OPEN
            turn18.branch = branch2
            turn18.totalPrice = 100
            turn18.totalToPay = 100
            turn18.save()

            Payment payment4 = new Payment()
            payment4.appointment = turn11
            payment4.currency= "ARG"
            payment4.amount = 200
            payment4.save()

            turn11.payments= new ArrayList<Payment>()
            turn11.payments.add(payment4)
            turn11.status =AppointmentStatus.PAID
            turn11.save()


            Promotion promotion=new Promotion()
            promotion.name="Descuentos"
            promotion.status=PromotionStatus.ACTIVE
            promotion.type=PromotionType.DISCOUNT
            cal.set(2018,1, 1)
            promotion.startDate=cal.getTime()
            cal.set(2048,1, 1)
            promotion.endDate=cal.getTime()
            promotion.discount=20
            promotion.services=new ArrayList<Service>()
            promotion.services.add(service)
            promotion.save()

            Promotion promotionExpired=new Promotion()
            promotionExpired.name="Descuentos expirados"
            promotionExpired.status=PromotionStatus.ACTIVE
            promotionExpired.type=PromotionType.DISCOUNT
            cal.set(2018,1, 1,9,0,0)
            promotionExpired.startDate=cal.getTime()
            cal.set(2018,1, 2,9,0,0)
            promotionExpired.endDate=cal.getTime()
            promotionExpired.discount=10
            promotionExpired.services=new ArrayList<Service>()
            promotionExpired.services.add(service)
            promotionExpired.save()

            Promotion promotionPoint=new Promotion()
            promotionPoint.name="Promoción de Puntos"
            promotionPoint.status=PromotionStatus.ACTIVE
            promotionPoint.type=PromotionType.POINT
            cal.set(2018,1, 1,9,0,0)
            promotionPoint.startDate=cal.getTime()
            cal.set(2048,1, 2,9,0,0)
            promotionPoint.endDate=cal.getTime()
            promotionPoint.pointFactor=2
            promotionPoint.services=new ArrayList<Service>()
            promotionPoint.services.add(service)
            promotionPoint.save()

            Config configChangePay=new Config(key: "changePay",value:"1").save()
            Config configChangePurchase=new Config(key: "changePurchase",value:"2").save()
        }
    }

    def destroy = {
    }
}