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

                Professional professional=new Professional()
                professional.name="Pedro"
                professional.services=new ArrayList<Service>()
                professional.services.add(service)
                professional.status=ProfessionalStatus.ACTIVE
                professional.save()

                Appointment turn=new Appointment()
                turn.local="casa"
                turn.dayHour= new Date()
                turn.services=new ArrayList<Service>()
                turn.services.add(service)

                turn.save()
            }
        }
    }
    def destroy = {
    }
}
