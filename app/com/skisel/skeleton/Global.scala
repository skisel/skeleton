package com.skisel.skeleton

import com.typesafe.config.ConfigFactory
import play.api.GlobalSettings

object Global extends GlobalSettings {
  val config = ConfigFactory load

}