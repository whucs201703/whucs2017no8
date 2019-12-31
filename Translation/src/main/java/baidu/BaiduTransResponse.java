package baidu;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public class BaiduTransResponse {
    public static interface BaiduTransService{
        @GET("api/trans/vip/translate")
        Call<BaiduTransResponse> translate(@QueryMap Map<String,String> params);
    }
    public static BaiduTransService generateService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.fanyi.baidu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(BaiduTransService.class);
    }
    private String from;
    private String to;
        private List<TransResult> trans_result;

    private String error_code;
    private String error_msg;
    public static class TransResult {
        private String src;
        private String dst;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getDst() {
            return dst;
        }

        public void setDst(String dst) {
            this.dst = dst;
        }
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<TransResult> getTransResult() {
        return trans_result;
    }

    public void setTransResult(List<TransResult> trans_result) {
        this.trans_result = trans_result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
