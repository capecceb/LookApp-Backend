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
            }
        }
    }
    def destroy = {
    }
}
