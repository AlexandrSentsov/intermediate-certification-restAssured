import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Snow {
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonProperty("1h")
    private int _1h;

    public int get_1h() {
        return _1h;
    }

    public void set_1h(int _1h) {
        this._1h = _1h;
    }
}
