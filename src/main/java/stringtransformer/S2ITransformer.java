package stringtransformer;

public class S2ITransformer implements Transformer <String, Integer> {
    @Override
    public Integer transform(String data) {
        return Integer.valueOf(data);
    }
}
