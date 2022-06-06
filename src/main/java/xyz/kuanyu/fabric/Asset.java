package xyz.kuanyu.fabric;

import lombok.Data;

@Data
public class Asset {

    String docType; //docType is used to distinguish the various types of objects in state database

    String id;

    String color;

    Integer size;

    String owner;

    Integer value;
}