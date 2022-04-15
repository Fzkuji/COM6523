package dynamicAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SoftwareReconnaissance {
    public static void main(String[] args) {
        Set<String> positiveMethods = readMethods(args[0]);
        Set<String> negativeMethods = readMethods(args[1]);
        positiveMethods.removeAll(negativeMethods);
        for(String method : positiveMethods) {
            System.out.println(method);
        }
    }

    public static Set<String> readMethods(String arg) {
        Set<String> methods = new HashSet<>();
        try(BufferedReader br = new BufferedReader(new FileReader(arg))) {
            String line;
            while ((line =  br.readLine()) != null) {
                methods.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return methods;
    }
}
