package baidu;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransApi {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private String appid;
    private String securityKey;
    private Map<String, String> params;

    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    public void getTransResultAsync(CallbackFun callbackFun) {
        if(params == null) {
            callbackFun.onFailure();
            return;
        }
        BaiduTransResponse.generateService().translate(params).enqueue(new Callback<BaiduTransResponse>() {
            @Override
            public void onResponse(Call<BaiduTransResponse> call, Response<BaiduTransResponse> response) {
                if(response.body().getTransResult() != null && response.body().getTransResult().size() != 0) {
                    callbackFun.onSuccess(response.body().getTransResult().get(0).getDst());
                } else {
                    Log.e("TransApi", response.body().getError_msg());
                    callbackFun.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaiduTransResponse> call, Throwable t) {
                t.printStackTrace();
                callbackFun.onFailure();
            }
        });
    }

    public String getTransResult() throws IOException {
        if(params == null) {
            return null;
        }
        Response<BaiduTransResponse> response = BaiduTransResponse.generateService().translate(params).execute();
        if(response.body().getTransResult().size() != 0) {
            return response.body().getTransResult().get(0).getDst();
        }
        return null;
    }

    public void setTask(String query, String from, String to) {
        params = buildParams(query, from, to);
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appid + query + salt + securityKey; // 加密前的原文
        params.put("sign", MD5.md5(src));

        return params;
    }

}
