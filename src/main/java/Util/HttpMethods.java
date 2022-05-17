/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import EntidadesSettings.SettingsDoctor;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import controller.CitaVerController;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.Response;
import okhttp3.internal.http2.Settings;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alexis
 */
public class HttpMethods {

    Gson json = new Gson();
    HttpClient httpclient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    final String url = "http://localhost:8080/";

    public <T> List<T> getList(Class<T> generico, String metodo) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        List<T> listGenericos = new ArrayList<T>();
        List<T> listGenericos2 = new ArrayList<T>();
        HttpRequest requestPosts = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + metodo)).build();
        try {
            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());

            listGenericos = json.fromJson(response.body(), type);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (T listGenerico : listGenericos) {
            listGenericos2.add(json.fromJson(json.toJson(listGenerico), generico));
        }
        return listGenericos2;
    }

    public <T> List<T> getCitaByFecha(Class<T> generico, String metodo, String fecha) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        List<T> listGenericos = new ArrayList<T>();
        List<T> listGenericos2 = new ArrayList<T>();

        HttpRequest requestPosts = HttpRequest.newBuilder().GET()
                .uri(URI.create(url + metodo + "/" + fecha)).build();
        List<SettingsDoctor> LIST;
        try {

            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());
            listGenericos = json.fromJson(response.body(), type);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (T listGenerico : listGenericos) {
            listGenericos2.add(json.fromJson(json.toJson(listGenerico), generico));
        }

        return listGenericos2;
    }

    public <T> List<T> getCitaFilter(Class<T> generico, String metodo, JSONObject citaAtributesJson) {

        Type type = new TypeToken<List<T>>() {
        }.getType();
        List<T> listGenericos = new ArrayList<T>();
        List<T> listGenericos2 = new ArrayList<T>();
        HttpRequest requestPosts = HttpRequest.newBuilder().header("Content-type", "application/json").POST(HttpRequest.BodyPublishers.ofString(citaAtributesJson.toString()))
                .uri(URI.create(url + metodo)).build();
        List<SettingsDoctor> LIST;
        try {

            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            listGenericos = json.fromJson(response.body(), type);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (T listGenerico : listGenericos) {
            listGenericos2.add(json.fromJson(json.toJson(listGenerico), generico));
        }

        return listGenericos2;
    }

    public <T> String AddObject(Class<T> generico, Object objeto, String metodo) {
        T obj = (T) objeto;
        String jsonResponse = json.toJson(obj);
        HttpRequest requestPosts = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(jsonResponse))
                .uri(URI.create(url + metodo)).build();
        String responseRPTA = "fail";
        try {

            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());
            responseRPTA = response.body();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseRPTA;

    }

    public <T> String UpdateObject(Class<T> generico, Object objeto, String metodo) {
        T obj = (T) objeto;
        String jsonResponse = json.toJson(obj);
        HttpRequest requestPosts = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(jsonResponse))
                .uri(URI.create(url + metodo)).build();
        String responseRPTA = "fail";
        try {

            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());
            responseRPTA = response.body();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseRPTA;
    }

    public <T> String DeleteObject(Class<T> generico, String metodo, String var) {
        HttpRequest requestPosts = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + metodo + "/" + var)).build();
        List<SettingsDoctor> LIST;
        try {

            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "ok";
    }

}
