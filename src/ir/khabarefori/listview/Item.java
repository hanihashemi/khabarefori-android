package ir.khabarefori.listview;

/**
 * Created by hani on 1/24/14.
 */
public class Item {
    private String title;
    private String description;

    public Item(String title, String description) {
        super();
        this.title = title;
        this.description = description;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getShortDescription()
    {
        String text = description.trim();
        String[] texts = text.split(" ");
        String result = "";

        for (int i=0; i<30 ; i++)
            result+= texts[i] + " ";

        return result + "...";
    }
}
