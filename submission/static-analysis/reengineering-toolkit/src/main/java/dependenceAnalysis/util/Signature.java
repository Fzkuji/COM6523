package dependenceAnalysis.util;

/**
 * Created by neilwalkinshaw on 26/07/2018.
 */
public class Signature {

    protected String owner;
    protected String method;
    protected String signature;

    public Signature(String owner, String method, String signature) {
        this.owner = owner;
        this.method = method;
        this.signature = signature;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Signature)) return false;

        Signature signature1 = (Signature) o;

        if (owner != null ? !owner.equals(signature1.owner) : signature1.owner != null) return false;
        if (method != null ? !method.equals(signature1.method) : signature1.method != null) return false;
        return signature != null ? signature.equals(signature1.signature) : signature1.signature == null;

    }

    @Override
    public int hashCode() {
        int result = owner != null ? owner.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (signature != null ? signature.hashCode() : 0);
        return result;
    }

    public String toString(){
        return owner+"."+method;
    }
}
