package util;

import dependenceAnalysis.interprocedural.CallGraph;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.io.InputStream;

/**
 * Use ASM to load class.
 */

public class ASMClassReader extends AbstractClassReader<ClassNode> {

    @Override
    protected String getPrefix() {
        return "/";
    }

    @Override
    protected ClassNode loadClass(String orig) {
        String className = "/"+orig+".class";
        InputStream in= CallGraph.class.getResourceAsStream(className);
        ClassNode cn = new ClassNode(Opcodes.ASM4);
        ClassReader classReader= null;
        try {
            classReader = new ClassReader(in);
            classReader.accept(cn, 0);
        } catch (IOException e) {
            System.out.println("FAILED" + className);
            e.printStackTrace();
        }
        return cn;
    }

}
