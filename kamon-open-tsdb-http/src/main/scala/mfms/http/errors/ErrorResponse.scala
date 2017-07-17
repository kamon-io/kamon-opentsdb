package mfms.http.errors

import kamon.opentsdb.DataPoint

case class ErrorResponse (failed: Int, success:Int, errors: Seq[ErrorResponse])

case class ErrorsDetails(dataPoint: DataPoint, error:String)
