package filemergesort.stringtransformer;

import filemergesort.exception.DataTransformErrorException;

public interface Transformer <I, O> {
    O transform(I data) throws DataTransformErrorException;
}
