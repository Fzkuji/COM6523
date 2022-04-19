package dynamicAnalysis.agent;

import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class Transformer implements ClassFileTransformer{

    protected String packagePrefix;

    public Transformer(String args){
        this.packagePrefix = args;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));

            CtBehavior[] methods = cl.getDeclaredBehaviors();
            for (int i = 0; i < methods.length; i++) {
                if(!cl.getPackageName().startsWith(packagePrefix))
                    continue;
                if(!cl.isPrimitive() && !cl.isInterface() && cl.getDeclaringClass()==null && cl.getEnclosingBehavior()==null) {
                    if (methods[i].isEmpty() == false) {
                        changeMethod(methods[i], cl.getName());
                    }
                }
            }
            classfileBuffer = cl.toBytecode();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cl != null) {
                cl.detach();
            }
        }
        return classfileBuffer;
    }

    private void changeMethod(CtBehavior method, String className) throws NotFoundException, CannotCompileException {
        if (!Modifier.isNative(method.getModifiers()) && !Modifier.isAbstract(method.getModifiers())) {
            String insertString = "java.util.logging.Logger.getLogger(\""+className+"\")" +
                    ".info(Integer.toString(Thread.currentThread().getStackTrace().length));";
            method.insertBefore(insertString);

        }
    }

}
