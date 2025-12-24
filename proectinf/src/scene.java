public class Scene {
    String name; // название сцены (сцена 1, сцена 2A, ...)
    String[] replicas;  // реплики - все предложения сцены
    String backgroundImage; // фон картинка
    String[] choices; // опциональное ветвление сцены
    Scene[] scenesChoices; // куда переходить, если есть ветвление
    Scene next; // следующая сцена, если нет ветвления

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
