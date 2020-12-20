package app.tulz

import app.tulz.validation.ops.signal.SignalOfBooleanValidationOps
import app.tulz.validation.ops.signal.SignalOfStringValidationOps
import app.tulz.validation.ops.stream.StreamOfBooleanValidationOps
import app.tulz.validation.ops.stream.StreamOfStringValidationOps
import cats.data.NonEmptyChain
import com.raquo.laminar.api.L._

package object validation {

  type ValidationError   = String
  type ValidatedValue[T] = Either[NonEmptyChain[ValidationError], T]
  type Validation[T]     = T => Either[NonEmptyChain[ValidationError], T]

  implicit def syntaxStreamOfStringValidation(stream: EventStream[String]): StreamOfStringValidationOps = new StreamOfStringValidationOps(stream)

  implicit def syntaxSignalOfStringValidation(signal: Signal[String]): SignalOfStringValidationOps = new SignalOfStringValidationOps(signal)

  implicit def syntaxStreamOfBooleanValidation(stream: EventStream[Boolean]): StreamOfBooleanValidationOps = new StreamOfBooleanValidationOps(stream)

  implicit def syntaxSignalOfBooleanValidation(signal: Signal[Boolean]): SignalOfBooleanValidationOps = new SignalOfBooleanValidationOps(signal)

  val V: Validations.type = Validations

}