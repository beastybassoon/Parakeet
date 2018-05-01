package com.ndsmith3.parakeet.lexer

import com.ndsmith3.parakeet.exception.UnexpectedCharacterException

import scala.annotation.tailrec

object Lexer {
  def tokenize(input: String): List[Token] = {
    @tailrec
    def tokenizeInput(currString: String, currTokens: List[Option[Token]] = Nil): List[Token] = {
      lazy val (token, newString) = getToken(currString)
      if (currString.isEmpty) currTokens.flatten else tokenizeInput(newString, currTokens :+ token)
    }

    tokenizeInput(input)
  }

  private def getToken(str: String): (Option[Token], String) = str.head match {
    case ' '                  => (None, str.tail)
    case '+'                  => (Some(AddToken), str.tail)
    case '-'                  => (Some(SubtractToken), str.tail)
    case '*'                  => (Some(MultiplyToken), str.tail)
    case '/'                  => (Some(DivideToken), str.tail)
    case '%'                  => (Some(ModulusToken), str.tail)
    case '^'                  => (Some(PowerToken), str.tail)
    case '('                  => (Some(LeftParenthesisToken), str.tail)
    case ')'                  => (Some(RightParenthesisToken), str.tail)
    case '"'                  => parseString(str)
    case '.'                  => parseFloat(str, "")
    case char if char.isDigit => parseNumber(str)
    case char                 => throw new UnexpectedCharacterException(char)
  }

  private def parseString(str: String): (Option[StringToken], String) = {
    def scan(currStr: String, currStringValue: String = ""): (Option[StringToken], String) = currStr.head match {
      case '"'  => (Some(StringToken(currStringValue)), currStr.tail)
      case char => scan(currStr.tail, currStringValue + char)
    }

    // Scan on tail to avoid the first quotation mark
    scan(str.tail)
  }

  private def parseNumber(str: String): (Option[NumericToken], String) = {
    @tailrec
    def scan(currStr: String, currIntString: String = ""): (Option[NumericToken], String) =
      if (isCompleteNumber(currStr)) (Some(IntegerToken(currIntString.toInt)), currStr)
      else if (isFloat(currStr)) parseFloat(currStr, currIntString)
      else scan(currStr.tail, currIntString + currStr.head)

    scan(str)
  }

  private def isCompleteNumber(str: String): Boolean = str.isEmpty || !str.head.isDigit && str.head != '.'
  private def isFloat(str: String): Boolean          = str.head == '.'

  private def parseFloat(str: String, floatString: String): (Option[FloatToken], String) = {
    @tailrec
    def scan(currStr: String, currFloatString: String = floatString): (Option[FloatToken], String) =
      if (isCompleteNumber(currStr)) (Some(FloatToken(currFloatString.toDouble)), currStr)
      else scan(currStr.tail, currFloatString + currStr.head)

    scan(str)
  }
}
