package application.model;

/**
 * Created by alobb on 10/6/14.
 */
public interface NoteModifier {
    public static String NOTE_VALUE = "NOTE_VALUE";
    public static String NOTE_INDEX = "NOTE_INDEX";

    public void removeNote(int index);

    public void editNote(int index);
}
