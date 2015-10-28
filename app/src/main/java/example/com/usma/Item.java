package example.com.usma;

/**
 * Created by Arnaud Rover on 18/10/15.
 */
public class Item {
    private NavigationMenu type;
    private String name;
    private String description;

    public Item(NavigationMenu type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public NavigationMenu getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
