package Compilation;

import DataSet.LinkedChain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Interpreter {
    /**
     * Converts a file from Brainstorm to Java
     * @param inputFilename the filepath for the input Brainstorm
     * @param filename the desired filename for the output <code>.java</code> file.
     *                 Note that if a file with this name and filetype already exists,
     *                 it will be overwritten. The Java class will be given the same name.
     * @param methodName the name of the methodName in the Java class
     * @param fileProtection the protection level of the class
     * @return the Java methodName in a <code>.java</code> file.
     */
    public static File brainstormToJava(String inputFilename,
                                        String filename,
                                        String methodName,
                                        Permission fileProtection) {
        File javaFile = new File(filename + ".java");
        try (Scanner scanner = new Scanner(new File(inputFilename))) {
            char[] inputBrainstorm = scanner.nextLine().toCharArray();
            scanner.close();
            boolean usesScannerInput = false;
            for(char c : inputBrainstorm) {
                usesScannerInput |= c == ',';
            }
            FileWriter writer = new FileWriter(javaFile);
            writer.write(fileProtection.getTag() + "class " + filename + " {\n");
            writer.write("\t" + fileProtection.tag + "static void " + methodName + "() {\n");
            if(usesScannerInput) {
                writer.write("\t\tScanner scanner = new Scanner(System.in);\n");
            }
            int loopCounter = 0;
            for(char c : inputBrainstorm) {
                StringBuilder nextLine = new StringBuilder();
//                switch (c) {
//                    case '>'
//                }
                nextLine.append("\n");
                writer.write(nextLine.toString());
            }
            if(usesScannerInput) {
                writer.write("\t\tscanner.close();\n");
            }
            writer.write("\t}\n");
            writer.write("}\n");
            writer.close();
            return javaFile;
        } catch (IOException e) {
            javaFile.delete();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Simulates a Brainstorm file to determine bounds on the binary tape used
     * @param instructions the String of instruction characters, written in Brainstorm
     * @return an array with the following information:
     *          - The size of the array
     *          - The starting index
     */
    public static int[] simulateBrainstorm(String instructions) {
        int index = 0, max = 0, layer = 0, stopLayer = 0;
        return new int[]{max + 1, index};
    }

    /**
     * Designates the protection level of a given programming component
     */
    public enum Permission {
        PUBLIC(0, "public "),
        PACKAGE_PROTECTED(1, ""),
        PROTECTED(2, "protected "),
        PRIVATE(3, "private ");

        private int permission;
        private String tag;

        /**
         * Creates a new <code>Permission</code> object
         * @param permission the level of protection granted (0-3)
         * @param tag the in-code name of the permission level
         */
        Permission(int permission, String tag) {
            this.permission = permission;
            this.tag = tag;
        }

        /**
         * Gets the numerical protection level of this Permission object
         * @return <code>this.permission</code>
         */
        public int getProtection() {
            return this.permission;
        }

        /**
         * Gets the code tag for this Permission object
         * @return <code>this.tag</code>
         */
        public String getTag() {
            return this.tag;
        }
    }
}
