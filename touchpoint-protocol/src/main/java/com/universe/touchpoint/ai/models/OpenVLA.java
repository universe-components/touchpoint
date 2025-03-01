package com.universe.touchpoint.ai.models;

import com.universe.touchpoint.ai.AIModel;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.context.TouchPoint;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class OpenVLA extends AIModel<Retrofit, OpenVLA.ActionRequest, OpenVLA.OpenVLAAPI, OpenVLA.ActionResponse> {

    public OpenVLA(LangModelConfig config) {
        super(new Retrofit.Builder()
                .baseUrl(config.getApiHost())
                .addConverterFactory(GsonConverterFactory.create())
                .build(), config);
    }

    @Override
    public void createCompletion() {
        this.completionService = this.client.create(OpenVLAAPI.class);
    }

    @Override
    public ActionResponse predict(ActionRequest request) {
        try {
            Response<ActionResponse> response = completionService.predictAction(request).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            // 处理请求异常
            e.printStackTrace();
        }
        return null;
    }

    public interface OpenVLAAPI {
        @POST("/act")
        Call<ActionResponse> predictAction(@Body ActionRequest request);
    }

    public static class ActionRequest extends TouchPoint {
        private final byte[][][] image;
        private final String instruction;

        public ActionRequest(byte[][][] image, String instruction) {
            this.image = image;
            this.instruction = instruction;
        }

        public byte[][][] getImage() {
            return image;
        }

        public String getInstruction() {
            return instruction;
        }
    }

    public static class ActionResponse extends TouchPoint {
        private String action;

        public String getAction() {
            return action;
        }
    }

}
