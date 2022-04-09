package structuralAnalysis.metrics;

public abstract class Metric<T> {

    protected T element;

    protected Double value;

    public Metric(T element){
        this.element = element;
    }

    protected abstract Double computeMetric();

    public  Double getValue(){
        return computeMetric();
    }


}
