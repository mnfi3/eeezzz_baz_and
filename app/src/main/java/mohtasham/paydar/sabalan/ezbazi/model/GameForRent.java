package mohtasham.paydar.sabalan.ezbazi.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mohtasham.paydar.sabalan.ezbazi.model.common.Photo;
import mohtasham.paydar.sabalan.ezbazi.model.common.Video;

public class GameForRent {
  private int id;
  private int game_info_id;
  private int price;
  private int is_pending;
  private String name;
  private int console_type_id;
  private int age_class;
  private String production_date;
  private String region;
  private int can_play_online;
  private String company_name;
  private String description;
  private ArrayList<Photo> photos;
  private ArrayList<Video> videos;

  public static class Parser{

    public static GameForRent parse(JSONObject jsonObject){
      GameForRent game = new GameForRent();
      try {
        JSONObject game_info_obj= new JSONObject();
        game_info_obj = jsonObject.getJSONObject("game_info");

        game.setId(jsonObject.getInt("id"));
        game.setGame_info_id(jsonObject.getInt("game_info_id"));
        game.setPrice(jsonObject.getInt("game_price"));
        game.setIs_pending(jsonObject.getInt("is_pending"));

        //game_info
        game.setName(game_info_obj.getString("name"));
        game.setConsole_type_id(game_info_obj.getInt("console_type_id"));
        game.setAge_class(game_info_obj.getInt("age_class"));
        game.setProduction_date(game_info_obj.getString("production_date"));
        game.setRegion(game_info_obj.getString("region"));
        game.setCan_play_online(game_info_obj.getInt("can_play_online"));
        game.setCompany_name(game_info_obj.getString("company_name"));
        game.setDescription(game_info_obj.getString("description"));
        JSONArray photoArray = game_info_obj.getJSONArray("photos");
        ArrayList<Photo> photos = new ArrayList<>();
       for (int i=0 ; i<photoArray.length() ; i++){
         Photo photo;
         photo = Photo.Parser.parse(photoArray.getJSONObject(i));
         photos.add(photo);
       }
       game.setPhotos(photos);

        JSONArray videoArray = game_info_obj.getJSONArray("videos");
        ArrayList<Video> videos = new ArrayList<>();
        for (int i=0 ; i<videoArray.length() ; i++){
          Video video;
          video = Video.Parser.parse(videoArray.getJSONObject(i));
          videos.add(video);
        }
        game.setVideos(videos);

      } catch (JSONException e) {
        e.printStackTrace();
      }
      return game;
    }
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getGame_info_id() {
    return game_info_id;
  }

  public void setGame_info_id(int game_info_id) {
    this.game_info_id = game_info_id;
  }


  public int isIs_pending() {
    return is_pending;
  }

  public void setIs_pending(int is_pending) {
    this.is_pending = is_pending;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getConsole_type_id() {
    return console_type_id;
  }

  public void setConsole_type_id(int console_type_id) {
    this.console_type_id = console_type_id;
  }

  public int getAge_class() {
    return age_class;
  }

  public void setAge_class(int age_class) {
    this.age_class = age_class;
  }

  public String getProduction_date() {
    return production_date;
  }

  public void setProduction_date(String production_date) {
    this.production_date = production_date;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public int isCan_play_online() {
    return can_play_online;
  }

  public void setCan_play_online(int can_play_online) {
    this.can_play_online = can_play_online;
  }

  public String getCompany_name() {
    return company_name;
  }

  public void setCompany_name(String company_name) {
    this.company_name = company_name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ArrayList<Photo> getPhotos() {
    return photos;
  }

  public void setPhotos(ArrayList<Photo> photos) {
    this.photos = photos;
  }

  public ArrayList<Video> getVideos() {
    return videos;
  }

  public void setVideos(ArrayList<Video> videos) {
    this.videos = videos;
  }


}
