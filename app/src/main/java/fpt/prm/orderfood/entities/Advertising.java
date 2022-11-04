package fpt.prm.orderfood.entities;

public class Advertising {
    private String key,
            Content,
            Image,
            Title;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Advertising() {
    }

    @Override
    public String toString() {
        return "Advertising{" +
                "key='" + key + '\'' +
                ", Content='" + Content + '\'' +
                ", Image='" + Image + '\'' +
                ", Title='" + Title + '\'' +
                '}';
    }
}
