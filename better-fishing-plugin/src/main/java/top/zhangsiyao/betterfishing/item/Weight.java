package top.zhangsiyao.betterfishing.item;

import java.util.HashMap;

public class Weight extends HashMap<String,String> {

    public Integer getWeight(){
        return Integer.parseInt(super.get("weight"));
    }

    public String getName(){
        return super.get("name");
    }
}
