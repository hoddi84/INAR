/**
 * Created by Tryggvi on 03/11/2016.
 */
enum RaceType {
    Orc,
    Elf,
    Goblin,
    Troll,
    Human
}

public class Race {

    Features features;

    public Race(RaceType raceType) {
        switch (raceType) {
            case Orc:
                features = new Features(Height.Large, SkinColor.Green, Ears.Pointy);
                break;
            case Elf:
                features = new Features(Height.Large, SkinColor.Yellow, Ears.Pointy);
                break;
            case Goblin:
                features = new Features(Height.Short, SkinColor.Green, Ears.Pointy);
                break;
            case Troll:
                features = new Features(Height.Large, SkinColor.Green, Ears.Cabbage);
                break;
            case Human:
                features = new Features(Height.Medium, SkinColor.Yellow, Ears.Round);
                break;
        }
    }
}