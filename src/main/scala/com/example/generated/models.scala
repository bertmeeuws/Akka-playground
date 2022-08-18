package com.example.generated.models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Actor.schema, Actordetails.schema, Movie.schema, Movieactormapping.schema, Movielocations.schema, Streamingprovidermapping.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Actor
   *  @param actorId Database column actor_id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar) */
  case class ActorRow(actorId: Long, name: String)
  /** GetResult implicit for fetching ActorRow objects using plain SQL queries */
  implicit def GetResultActorRow(implicit e0: GR[Long], e1: GR[String]): GR[ActorRow] = GR{
    prs => import prs._
      ActorRow.tupled((<<[Long], <<[String]))
  }
  /** Table description of table Actor. Objects of this class serve as prototypes for rows in queries. */
  class Actor(_tableTag: Tag) extends profile.api.Table[ActorRow](_tableTag, Some("movies"), "Actor") {
    def * = (actorId, name) <> (ActorRow.tupled, ActorRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(actorId), Rep.Some(name))).shaped.<>({r=>import r._; _1.map(_=> ActorRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column actor_id SqlType(bigserial), AutoInc, PrimaryKey */
    val actorId: Rep[Long] = column[Long]("actor_id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar) */
    val name: Rep[String] = column[String]("name")
  }
  /** Collection-like TableQuery object for table Actor */
  lazy val Actor = new TableQuery(tag => new Actor(tag))

  /** Entity class storing rows of table Actordetails
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param actorId Database column actor_id SqlType(int8)
   *  @param personalInfo Database column personal_info SqlType(jsonb), Length(2147483647,false) */
  case class ActordetailsRow(id: Long, actorId: Long, personalInfo: String)
  /** GetResult implicit for fetching ActordetailsRow objects using plain SQL queries */
  implicit def GetResultActordetailsRow(implicit e0: GR[Long], e1: GR[String]): GR[ActordetailsRow] = GR{
    prs => import prs._
      ActordetailsRow.tupled((<<[Long], <<[Long], <<[String]))
  }
  /** Table description of table ActorDetails. Objects of this class serve as prototypes for rows in queries. */
  class Actordetails(_tableTag: Tag) extends profile.api.Table[ActordetailsRow](_tableTag, Some("movies"), "ActorDetails") {
    def * = (id, actorId, personalInfo) <> (ActordetailsRow.tupled, ActordetailsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(actorId), Rep.Some(personalInfo))).shaped.<>({r=>import r._; _1.map(_=> ActordetailsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column actor_id SqlType(int8) */
    val actorId: Rep[Long] = column[Long]("actor_id")
    /** Database column personal_info SqlType(jsonb), Length(2147483647,false) */
    val personalInfo: Rep[String] = column[String]("personal_info", O.Length(2147483647,varying=false))
  }
  /** Collection-like TableQuery object for table Actordetails */
  lazy val Actordetails = new TableQuery(tag => new Actordetails(tag))

  /** Entity class storing rows of table Movie
   *  @param movieId Database column movie_id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar)
   *  @param releaseDate Database column release_date SqlType(date)
   *  @param lengthInMin Database column length_in_min SqlType(int4) */
  case class MovieRow(movieId: Long, name: String, releaseDate: java.sql.Date, lengthInMin: Int)
  /** GetResult implicit for fetching MovieRow objects using plain SQL queries */
  implicit def GetResultMovieRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Date], e3: GR[Int]): GR[MovieRow] = GR{
    prs => import prs._
      MovieRow.tupled((<<[Long], <<[String], <<[java.sql.Date], <<[Int]))
  }
  /** Table description of table Movie. Objects of this class serve as prototypes for rows in queries. */
  class Movie(_tableTag: Tag) extends profile.api.Table[MovieRow](_tableTag, Some("movies"), "Movie") {
    def * = (movieId, name, releaseDate, lengthInMin) <> (MovieRow.tupled, MovieRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(movieId), Rep.Some(name), Rep.Some(releaseDate), Rep.Some(lengthInMin))).shaped.<>({r=>import r._; _1.map(_=> MovieRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column movie_id SqlType(bigserial), AutoInc, PrimaryKey */
    val movieId: Rep[Long] = column[Long]("movie_id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar) */
    val name: Rep[String] = column[String]("name")
    /** Database column release_date SqlType(date) */
    val releaseDate: Rep[java.sql.Date] = column[java.sql.Date]("release_date")
    /** Database column length_in_min SqlType(int4) */
    val lengthInMin: Rep[Int] = column[Int]("length_in_min")
  }
  /** Collection-like TableQuery object for table Movie */
  lazy val Movie = new TableQuery(tag => new Movie(tag))

  /** Entity class storing rows of table Movieactormapping
   *  @param movieActorId Database column movie_actor_id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param movieId Database column movie_id SqlType(int8)
   *  @param actorId Database column actor_id SqlType(int8) */
  case class MovieactormappingRow(movieActorId: Long, movieId: Long, actorId: Long)
  /** GetResult implicit for fetching MovieactormappingRow objects using plain SQL queries */
  implicit def GetResultMovieactormappingRow(implicit e0: GR[Long]): GR[MovieactormappingRow] = GR{
    prs => import prs._
      MovieactormappingRow.tupled((<<[Long], <<[Long], <<[Long]))
  }
  /** Table description of table MovieActorMapping. Objects of this class serve as prototypes for rows in queries. */
  class Movieactormapping(_tableTag: Tag) extends profile.api.Table[MovieactormappingRow](_tableTag, Some("movies"), "MovieActorMapping") {
    def * = (movieActorId, movieId, actorId) <> (MovieactormappingRow.tupled, MovieactormappingRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(movieActorId), Rep.Some(movieId), Rep.Some(actorId))).shaped.<>({r=>import r._; _1.map(_=> MovieactormappingRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column movie_actor_id SqlType(bigserial), AutoInc, PrimaryKey */
    val movieActorId: Rep[Long] = column[Long]("movie_actor_id", O.AutoInc, O.PrimaryKey)
    /** Database column movie_id SqlType(int8) */
    val movieId: Rep[Long] = column[Long]("movie_id")
    /** Database column actor_id SqlType(int8) */
    val actorId: Rep[Long] = column[Long]("actor_id")
  }
  /** Collection-like TableQuery object for table Movieactormapping */
  lazy val Movieactormapping = new TableQuery(tag => new Movieactormapping(tag))

  /** Entity class storing rows of table Movielocations
   *  @param movieLocationId Database column movie_location_id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param movieId Database column movie_id SqlType(int8)
   *  @param locations Database column locations SqlType(_text), Length(2147483647,false) */
  case class MovielocationsRow(movieLocationId: Long, movieId: Long, locations: String)
  /** GetResult implicit for fetching MovielocationsRow objects using plain SQL queries */
  implicit def GetResultMovielocationsRow(implicit e0: GR[Long], e1: GR[String]): GR[MovielocationsRow] = GR{
    prs => import prs._
      MovielocationsRow.tupled((<<[Long], <<[Long], <<[String]))
  }
  /** Table description of table MovieLocations. Objects of this class serve as prototypes for rows in queries. */
  class Movielocations(_tableTag: Tag) extends profile.api.Table[MovielocationsRow](_tableTag, Some("movies"), "MovieLocations") {
    def * = (movieLocationId, movieId, locations) <> (MovielocationsRow.tupled, MovielocationsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(movieLocationId), Rep.Some(movieId), Rep.Some(locations))).shaped.<>({r=>import r._; _1.map(_=> MovielocationsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column movie_location_id SqlType(bigserial), AutoInc, PrimaryKey */
    val movieLocationId: Rep[Long] = column[Long]("movie_location_id", O.AutoInc, O.PrimaryKey)
    /** Database column movie_id SqlType(int8) */
    val movieId: Rep[Long] = column[Long]("movie_id")
    /** Database column locations SqlType(_text), Length(2147483647,false) */
    val locations: Rep[String] = column[String]("locations", O.Length(2147483647,varying=false))
  }
  /** Collection-like TableQuery object for table Movielocations */
  lazy val Movielocations = new TableQuery(tag => new Movielocations(tag))

  /** Entity class storing rows of table Streamingprovidermapping
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param movieId Database column movie_id SqlType(int8)
   *  @param streamingProvider Database column streaming_provider SqlType(varchar) */
  case class StreamingprovidermappingRow(id: Long, movieId: Long, streamingProvider: String)
  /** GetResult implicit for fetching StreamingprovidermappingRow objects using plain SQL queries */
  implicit def GetResultStreamingprovidermappingRow(implicit e0: GR[Long], e1: GR[String]): GR[StreamingprovidermappingRow] = GR{
    prs => import prs._
      StreamingprovidermappingRow.tupled((<<[Long], <<[Long], <<[String]))
  }
  /** Table description of table StreamingProviderMapping. Objects of this class serve as prototypes for rows in queries. */
  class Streamingprovidermapping(_tableTag: Tag) extends profile.api.Table[StreamingprovidermappingRow](_tableTag, Some("movies"), "StreamingProviderMapping") {
    def * = (id, movieId, streamingProvider) <> (StreamingprovidermappingRow.tupled, StreamingprovidermappingRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(movieId), Rep.Some(streamingProvider))).shaped.<>({r=>import r._; _1.map(_=> StreamingprovidermappingRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column movie_id SqlType(int8) */
    val movieId: Rep[Long] = column[Long]("movie_id")
    /** Database column streaming_provider SqlType(varchar) */
    val streamingProvider: Rep[String] = column[String]("streaming_provider")
  }
  /** Collection-like TableQuery object for table Streamingprovidermapping */
  lazy val Streamingprovidermapping = new TableQuery(tag => new Streamingprovidermapping(tag))
}
