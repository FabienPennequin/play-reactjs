package js

import java.io._

object Resources {

  def reactjs = new InputStreamReader(getClass().getResourceAsStream("react.js"))
  def server = new InputStreamReader(getClass().getResourceAsStream("server.js"))

}
