package gq.gianr.infobanjirsurabaya.data.dto.posts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by j on 17/03/2017.
 */

public class PagingEntity {
    @SerializedName("previous")
    @Expose
    private String previous;
    @SerializedName("next")
    @Expose
    private String next;

    /**
     * @return The previous
     */
    public String getPrevious() {
        return previous;
    }

    /**
     * @param previous The previous
     */
    public void setPrevious(String previous) {
        this.previous = previous;
    }

    /**
     * @return The next
     */
    public String getNext() {
        return next;
    }

    /**
     * @param next The next
     */
    public void setNext(String next) {
        this.next = next;
    }

}
