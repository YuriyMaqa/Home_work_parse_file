package guru.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Me {
    public String name;
    public String surname;
    @SerializedName("favorite_music")
    public List<String> favoriteMusic;
    public Address address;
}
