package com.ndsmith3.parakeet.ast

trait AbstractSyntaxTree
trait Primitive extends AbstractSyntaxTree {
  val typeName: String
}

case class Assignment(constantName: String, value: AbstractSyntaxTree) extends AbstractSyntaxTree
case class ID(constantName: String)                                    extends AbstractSyntaxTree
case class CompoundStatement(statements: List[AbstractSyntaxTree])     extends AbstractSyntaxTree
case class TypeDeclaration(constantName: String, typeName: String)     extends AbstractSyntaxTree
