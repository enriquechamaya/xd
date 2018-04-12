package trismegistoplanilla.utilities;

import java.io.Serializable;
import org.json.JSONObject;

public class ResponseHelper implements Serializable {

    private static final long serialVersionUID = 1468112838795237760L;

    private boolean status;
    private String message;
    private JSONObject data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

}
