package mfms.http.errors

case class ErrorFormatter(code: Int, message: String, details: Option[String], trace: Option[String])
