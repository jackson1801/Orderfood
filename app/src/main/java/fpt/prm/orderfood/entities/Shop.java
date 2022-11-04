package fpt.prm.orderfood.entities;


import java.io.Serializable;
import java.util.List;

public class Shop implements Serializable {
    private String key, CategoriesID, Image, Location, Name, OpeningHours, Rating;
    private List<Food> foods;

    public Shop() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategoriesID() {
        return CategoriesID;
    }

    public void setCategoriesID(String categoriesID) {
        CategoriesID = categoriesID;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOpeningHours() {
        return OpeningHours;
    }

    public void setOpeningHours(String openingHours) {
        OpeningHours = openingHours;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }
}
