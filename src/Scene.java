public class Scene {
    String name;
    String[] replicas;
    String backgroundImage;
    String[] choices;
    Scene[] scenesChoices;
    Scene next;

    public Scene(String name, String[] replicas, String backgroundImage,
                 String[] choices, Scene[] scenesChoices, Scene next) {
        this.name = name;
        this.replicas = replicas;
        this.backgroundImage = backgroundImage;
        this.choices = choices;
        this.scenesChoices = scenesChoices;
        this.next = next;
    }
}
