package com.github.tototoshi.fixture.play

import javax.inject.Inject

import com.github.tototoshi.fixture.Fixture
import play.api.Configuration

class Fixtures @Inject() (configuration: Configuration) {

  private val configurationReader = new FixtureConfigurationReader(configuration)

  private val fixtureConfigurations = configurationReader.getFixtureConfigurations

  def allDatabaseNames: Seq[String] = configurationReader.getAllDatabaseNames

  def get(dbName: String): Option[Fixture] = {
    fixtureConfigurations.get(dbName) map { config =>
      val dbConfig = config.database
      val fixture = Fixture(dbConfig.driver, dbConfig.url, dbConfig.username, dbConfig.password).scriptLocation(config.scriptLocation)
      config.scriptPackage match {
        case Some(p) => fixture.scriptPackage(p).scripts(config.scripts)
        case None => fixture.scripts(config.scripts)
      }
    }
  }

}
