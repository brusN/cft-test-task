package stringtransformer;

public interface Transformer <I, O> {
    O transform(I data);
}
