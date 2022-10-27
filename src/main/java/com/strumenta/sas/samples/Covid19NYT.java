package com.strumenta.sas.samples;

import com.strumenta.kolasu.javalib.Traversing;
import com.strumenta.kolasu.model.Node;
import com.strumenta.kolasu.model.Processing;
import com.strumenta.kolasu.parsing.ParsingResult;
import com.strumenta.sas.SASLanguage;
import com.strumenta.sas.ast.SourceFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Covid19NYT {

    public static final String INDENTATION = "  ";

    public static void main(String[] args) throws IOException {
        String url = args.length > 0 ?
                args[0] :
                "https://raw.githubusercontent.com/sassoftware/covid-19-sas/master/Data/import-data-nyt.sas";
        try(InputStream inputStream = new URL(url).openStream()) {
            //We could parse directly from the input stream, without storing the code into a string, however we later
            //extract some text from the code string for demonstration purposes.
            //The parser keeps the entire text in memory anyway, as long as there are live references to the parse tree.
            String code = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            SASLanguage sas = new SASLanguage();
            sas.optimizeForSpeed(); // Or sas.optimizeForMemory();
            System.out.print("Parsing " + url + "...");
            ParsingResult<SourceFile> result = sas.parse(code, true, true);

            if(result.getCorrect()) {
               System.out.println(" no issues found.");
            } else {
                System.out.println(" there are issues.");
            }
            result.getIssues().forEach(i -> {
                switch (i.getSeverity()) {
                    case INFO:
                        System.out.println("INFO: " + i.getMessage() + (i.getPosition() != null ? " @ " + i.getPosition() : ""));
                        break;
                    case WARNING:
                        System.err.println("WARNING: " + i.getMessage() + (i.getPosition() != null ? " @ " + i.getPosition() : ""));
                        break;
                    case ERROR:
                        System.err.println("ERROR: " + i.getMessage() + (i.getPosition() != null ? " @ " + i.getPosition() : ""));
                        break;
                }
            });

            System.out.println();

            Traversing.walk(result.getRoot()).forEach(node -> {
                for(Node parent = node.getParent(); parent != null; parent = parent.getParent()) {
                    System.out.print(INDENTATION);
                }
                System.out.println(
                        node.getClass().getName().substring("com.strumenta.sas.ast.".length()) +
                        " @ " + node.getPosition() +
                        " with text \"" + StringUtils.abbreviate(node.getPosition().text(code), 30) + "\"");
                Processing.processProperties(
                        node,
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
