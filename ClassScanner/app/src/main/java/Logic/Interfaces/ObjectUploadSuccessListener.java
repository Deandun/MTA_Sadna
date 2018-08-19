package Logic.Interfaces;

import Logic.PictureAudioData;

// this interface was written just because in this android API we can't use the Consumer interface.
@FunctionalInterface
public interface ObjectUploadSuccessListener<T> {
    void onUploadSuccess(T uploadedObject);
}
