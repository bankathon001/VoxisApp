package com.bankathon.voxisapp.digilink;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.vision.digitalink.DigitalInkRecognition;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions;

import java.util.HashSet;
import java.util.Set;

/** Class to manage model downloading, deletion, and selection. */
public class ModelManager {

    private static final String TAG = "MLKD.ModelManager";
    private DigitalInkRecognitionModel model;
    private DigitalInkRecognizer recognizer;
    final RemoteModelManager remoteModelManager = RemoteModelManager.getInstance();

    public String setModel(String languageTag) {
        // Clear the old model and recognizer.
        model = null;
        if (recognizer != null) {
            recognizer.close();
        }
        recognizer = null;

        // Try to parse the languageTag and get a model from it.
        DigitalInkRecognitionModelIdentifier modelIdentifier;
        try {
            modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag(languageTag);
        } catch (MlKitException e) {
            Log.e(TAG, "Failed to parse language '" + languageTag + "'");
            return "";
        }
        if (modelIdentifier == null) {
            return "No model for language: " + languageTag;
        }

        // Initialize the model and recognizer.
        model = DigitalInkRecognitionModel.builder(modelIdentifier).build();
        recognizer =
                DigitalInkRecognition.getClient(DigitalInkRecognizerOptions.builder(model).build());
        Log.i(
                TAG,
                "Model set for language '"
                        + languageTag
                        + "' ('"
                        + modelIdentifier.getLanguageTag()
                        + "').");
        return "Model set for language: " + languageTag;
    }

    public DigitalInkRecognizer getRecognizer() {
        return recognizer;
    }

    public Task<Boolean> checkIsModelDownloaded() {
        return remoteModelManager.isModelDownloaded(model);
    }

    public Task<String> deleteActiveModel() {
        if (model == null) {
            Log.i(TAG, "Model not set");
            return Tasks.forResult("Model not set");
        }
        return checkIsModelDownloaded()
                .onSuccessTask(
                        new SuccessContinuation<Boolean, String>() {
                            @NonNull
                            @Override
                            public Task<String> then(@Nullable Boolean result) throws Exception {
                                if (!result) {
                                    return Tasks.forResult("Model not downloaded yet");
                                }
                                return remoteModelManager
                                        .deleteDownloadedModel(model)
                                        .onSuccessTask(
                                                new SuccessContinuation<Void, String>() {
                                                    @NonNull
                                                    @Override
                                                    public Task<String> then(@Nullable Void aVoid) throws Exception {
                                                        Log.i(TAG, "Model successfully deleted");
                                                        return Tasks.forResult("Model successfully deleted");
                                                    }
                                                });
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error while model deletion: " + e);
                    }
                });
    }

    public Task<Set<String>> getDownloadedModelLanguages() {
        return remoteModelManager
                .getDownloadedModels(DigitalInkRecognitionModel.class)
                .onSuccessTask(
                        new SuccessContinuation<Set<DigitalInkRecognitionModel>, Set<String>>() {
                            @NonNull
                            @Override
                            public Task<Set<String>> then(@Nullable Set<DigitalInkRecognitionModel> remoteModels) throws Exception {
                                Set<String> result = new HashSet<>();
                                for (DigitalInkRecognitionModel model : remoteModels) {
                                    result.add(model.getModelIdentifier().getLanguageTag());
                                }
                                Log.i(TAG, "Downloaded models for languages:" + result);
                                return Tasks.forResult(result);
                            }
                        });
    }

    public Task<String> download() {
        if (model == null) {
            return Tasks.forResult("Model not selected.");
        }
        return remoteModelManager
                .download(model, new DownloadConditions.Builder().build())
                .onSuccessTask(
                        new SuccessContinuation<Void, String>() {
                            @NonNull
                            @Override
                            public Task<String> then(@Nullable Void aVoid) throws Exception {
                                Log.i(TAG, "Model download succeeded.");
                                return Tasks.forResult("Downloaded model successfully");
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error while downloading the model: " + e);
                    }
                });
    }
}