package accesData

import slick.jdbc.JdbcBackend.Database

trait DatabaseConfigModule {
  lazy val db = Database.forConfig("postgres")
}
