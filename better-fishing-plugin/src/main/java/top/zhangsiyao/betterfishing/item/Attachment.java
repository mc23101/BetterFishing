package top.zhangsiyao.betterfishing.item;

import lombok.Data;

import java.io.File;
import java.io.Serializable;

@Data
public class Attachment implements Serializable {

    private final String name;

    private final File file;

    public Attachment(String name, File file) {
        this.name = name;
        this.file = file;
    }

}
