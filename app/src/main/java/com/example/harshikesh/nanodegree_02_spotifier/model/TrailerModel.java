package com.example.harshikesh.nanodegree_02_spotifier.model;

/**
 * Created by harshikesh.kumar on 01/02/16.
 */
public class TrailerModel {

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  String id = "id";
  String iso_639_1 = "iso";
  String key = "key";

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getIso_639_1() {
    return iso_639_1;
  }

  public void setIso_639_1(String iso_639_1) {
    this.iso_639_1 = iso_639_1;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  String name = "name";
  String site = "site";
  String size = "size";
  String type = "type";
}
