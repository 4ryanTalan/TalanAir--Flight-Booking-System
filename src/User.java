import java.awt.*;

class User {
    String name, role;
    int miles;

    public User(String name, String role) {
        this.name = name;
        this.role = role;
        this.miles = 1632;
    }

    public String getTier() {
        if (miles >= 50000) return "Platinum";
        if (miles >= 20000) return "Gold";
        if (miles >= 5000) return "Silver";
        return "Blue";
    }

    public Color getTierColor() {
        if (miles >= 50000) return new Color(160, 200, 220);
        if (miles >= 20000) return new Color(255, 185, 0);
        if (miles >= 5000) return new Color(160, 160, 160);
        return new Color(0, 122, 255);
    }
}
