package filemergesort.stringtransformer;

import filemergesort.exception.DataTransformErrorException;

public class S2ITransformer implements Transformer <String, Integer> {
    @Override
    public Integer transform(String data) throws DataTransformErrorException {
        Integer value = null;
        try {
            value = Integer.valueOf(data);
        } catch (NumberFormatException e) {
            throw new DataTransformErrorException("String can't be transform to integer: " + e.getMessage());
        }
        return value;
    }
}
