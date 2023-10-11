package top.zhangsiyao.betterfishing.utils;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class MaterialUtils {

    private static Map<String, Material> materials=new HashMap<>();

    static {
        initMaterials();
    }

    public static void initMaterials(){
        for(Material material:Material.values()){
            String name=material.name();
            name=name.replaceAll("_","").toUpperCase();
            materials.put(name,material);
        }
    }


    public static Material getMaterial(String name){
        Material material=Material.getMaterial(name);
        if(material!=null){
            return material;
        }
        if(materials.containsKey(name)){
            return materials.get(name);
        }
        return Material.AIR;
    }


}
