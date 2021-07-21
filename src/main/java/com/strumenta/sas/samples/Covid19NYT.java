package com.strumenta.sas.samples;

import com.strumenta.kolasu.model.Node;
import com.strumenta.kolasu.model.ProcessingKt;
import com.strumenta.kolasu.model.TraversingKt;
import com.strumenta.sas.ast.SasAstBuilder;
import com.strumenta.sas.parser.SASLexer;
import com.strumenta.sas.parser.SASParser;
import kotlin.collections.SetsKt;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;

public class Covid19NYT {

    public static final String INDENTATION = "    ";

    public static void main(String[] args) throws IOException {
        String url = "https://raw.githubusercontent.com/sassoftware/covid-19-sas/master/Data/import-data-nyt.sas";
        try(InputStream inputStream = new URL(url).openStream()) {
            SASLexer lexer = new SASLexer(CharStreams.fromStream(inputStream));
            SASParser parser = new SASParser(new CommonTokenStream(lexer));
            SASParser.SourceFileContext sourceFile = parser.sourceFile();
            Node astRoot = new SasAstBuilder().transform(sourceFile, null);
            TraversingKt.walk(astRoot).iterator().forEachRemaining(node -> {
                for(Node parent = node.getParent(); parent != null; parent = parent.getParent()) {
                    System.out.print(INDENTATION);
                }
                System.out.println("Traversing node " + node.getClass().getName() + " @ " + node.getPosition());
                ProcessingKt.processProperties(
                        node, SetsKt.setOf("parseTreeNode", "position", "specifiedPosition"),
                        p -> {
                            if(!p.getProvideNodes()) {
                                for (Node parent = node.getParent(); parent != null; parent = parent.getParent()) {
                                    System.out.print(INDENTATION);
                                }
                                System.out.print(INDENTATION);
                                System.out.println(p.getName() + " = " + p.getValue());
                            }
                            return null;
                        });
            });
        }
    }
}
