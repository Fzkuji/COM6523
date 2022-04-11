package util;

/**
 * Use Reflection to load class.
 */

public class ReflectionClassReader extends AbstractClassReader<Class<?>> {

    @Override
    protected String getPrefix() {
        return ".";
    }

    @Override
    protected Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("Unexpected ClassNotFoundException loading class '"
                    + className + "'");
        }
        catch (Error e){
            return null;
        }
    }

}
