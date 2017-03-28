package gq.gianr.infobanjirsurabaya.data.dto.posts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j on 17/03/2017.
 */

public class PostsDataEntity {
    @SerializedName("data")
    @Expose
    private List<PostEntity> data = new ArrayList<>();
    @SerializedName("paging")
    @Expose
    private PagingEntity pagingEntity;

    /**
     * @return The data
     */
    public List<PostEntity> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<PostEntity> data) {
        this.data = data;
    }

    /**
     * @return The pagingEntity
     */
    public PagingEntity getPagingEntity() {
        return pagingEntity;
    }

    /**
     * @param pagingEntity The pagingEntity
     */
    public void setPagingEntity(PagingEntity pagingEntity) {
        this.pagingEntity = pagingEntity;
    }

}
