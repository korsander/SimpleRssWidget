package ru.korsander.simplersswidget.businesslayer.objects;

/**
 * Created by korsander on 24.08.16.
 */
public class ExtendedSetting extends Setting {
    public String desc;

    public ExtendedSetting(String title, String desc) {
        super(title);
        this.desc = desc;
    }
}
