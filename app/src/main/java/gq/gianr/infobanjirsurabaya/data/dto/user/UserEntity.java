package gq.gianr.infobanjirsurabaya.data.dto.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by j on 17/03/2017.
 */

public class UserEntity {
    @SerializedName("picture")
    @Expose
    private PictureEntity pictureEntity;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;

    /**
     * @return The pictureEntity
     */
    public PictureEntity getPictureEntity() {
        return pictureEntity;
    }

    /**
     * @param pictureEntity The pictureEntity
     */
    public void setPictureEntity(PictureEntity pictureEntity) {
        this.pictureEntity = pictureEntity;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }
}
