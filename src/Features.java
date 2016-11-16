import java.util.ArrayList;

/**
 * Created by brynjarolafsson on 03/11/16.
 */
enum Height {
    Short,
    Medium,
    Large
}
enum SkinColor {
    Blue,
    Green,
    Red,
    Yellow
}
enum Ears{
    Pointy,
    Round,
    Cabbage
}
public class Features {

    public Height height;
    public SkinColor skincolor;
    public Ears ears;

    public Features(Height height, SkinColor skincolor, Ears ears){
        this.height = height;
        this.skincolor = skincolor;
        this.ears = ears;
    }

    public ArrayList<String> getFeatures() {
        ArrayList<String> featureList = new ArrayList<>();
        featureList = new ArrayList<>();
        featureList.add(this.height.toString());
        featureList.add(this.skincolor.toString());
        featureList.add(this.ears.toString());
        return featureList;
    }
}