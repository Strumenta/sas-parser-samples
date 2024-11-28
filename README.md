# Strumenta SAS Parser Samples

This is a collection of examples of usage of Strumenta's parser for the SAS language.

Note that the SAS parser is a commercial product and it's required to build and run these examples.

You can find a tutorial on [how to use the SAS Parser on Strumenta's website](https://tomassetti.me/how-to-use-the-sas-parser/).

## Strumenta Playground

On the [Strumenta Playground](https://playground.strumenta.com/sas) you can see some code samples and their translation into an AST,
an Abstract Syntax Tree that applications can analyze to extract information about a SAS program.

## Example Usage (Java)

At https://github.com/Strumenta/sas-parser-samples/blob/main/src/main/java/com/strumenta/sas/samples/Covid19NYT.java you can
find a Java code sample that uses the parser to print some information about a SAS file and the structure of the code. 

## Eclipse Ecore/EMF

To parse a SAS file and obtain an Ecore representation of it run:
```shell
java -jar sas-parser-<version>-jar-with-dependencies.jar <input.sas> emf <output.xmi> 
```
This will create two files:
 * `metamodel.xmi` containing the definition of the AST classes (that won't change between runs of the same version of the tool)
 * `output.xml` containing the actual AST objects

You should then be able to import both into Eclipse.
