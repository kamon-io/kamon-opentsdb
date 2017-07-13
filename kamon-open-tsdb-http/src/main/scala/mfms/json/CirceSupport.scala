package mfms.json

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import akka.util.ByteString
import io.circe._

/*
  * Automatic to and from JSON marshalling/unmarshalling using an in-scope *Circe* protocol.
  *
  * To use automatic codec derivation, user need to import `circe.generic.auto._`.
  */
object CirceSupport extends CirceSupport

/*
  * JSON marshalling/unmarshalling using an in-scope *Circe* protocol.
  *
  * To use automatic codec derivation, user need to import `io.circe.generic.auto._`
  */
trait CirceSupport {

    private val jsonStringUnmarshaller =
        Unmarshaller.byteStringUnmarshaller
          .forContentTypes(`application/json`)
          .mapWithCharset {
              case (ByteString.empty, _) ⇒ throw Unmarshaller.NoContentException
              case (data, charset) ⇒ data.decodeString(charset.nioCharset.name)
          }

    private val jsonStringMarshaller = Marshaller.stringMarshaller(`application/json`)

    /*
      * HTTP entity ⇒ `A`
      *
      * @param decoder decoder for `A`, probably created by `circe.generic`
      * @tparam T type to decode
      * @return unmarshaller for `A`
      */
    implicit def circeUnmarshaller[T](implicit decoder: Decoder[T]): FromEntityUnmarshaller[T] = jsonStringUnmarshaller.map(jawn.decode(_).fold(throw _, identity))

    /*
      * `A` ⇒ HTTP entity
      *
      * @param encoder encoder for `A`, probably created by `circe.generic`
      * @param printer pretty printer function
      * @tparam T type to encode
      * @return marshaller for any `A` value
      */
    implicit def circeToEntityMarshaller[T](implicit encoder: Encoder[T], printer: Json ⇒ String = Printer.noSpaces.pretty): ToEntityMarshaller[T] = jsonStringMarshaller.compose(printer).compose(encoder.apply)
}
