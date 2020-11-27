package org.randomizer.model;


public class Game {

    private String name;
    private String background_image;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return  "name: " + name + '\n' +
                "backgroundImage: " + background_image + '\n' +
                "description: " + description + '\n';
    }
}
