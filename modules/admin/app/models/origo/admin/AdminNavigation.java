package models.origo.admin;

import main.origo.core.Navigation;

public class AdminNavigation implements Navigation<AdminNavigation> {

    private String key;
    private String text;
    private String link;
    private int weight;

    public AdminNavigation(String key, String text, String link, int weight) {
        this.key = key;
        this.text = text;
        this.link = link;
        this.weight = weight;
    }

    @Override
    public String getReferenceId() {
        return key;
    }

    @Override
    public String getSection() {
        return "admin";
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String type() {
        return "admin";
    }

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }
}
